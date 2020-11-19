package edu.hadoop.mr.demo;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @author 彭文忠
 * 执行顺序：
 * 1.Map Task读文件{TextInputFormat(-->RecordReader-->read())}一次读取一行，返回（Key，Value）
 * 2.Map执行：将上一步获取的(key，value)键值对经过Mapper的map方法逻辑处理成新的(k,v)键值对，通过context.write输出到OutputCollector收集器
Shuffle开始:
 * 3.OutputCollector把收集到的（k,v）键值对写入到环形缓冲区中，环形缓冲区默认大小为100M，只写80%（阈值）。缓冲区达到80%开始溢写文件，触发spill溢写操作；
 *      3.1.分区Partitioner；
 *      3.2.排序Sort(先按分区排，再按key排序)；
 *      3.3.Combiner在Map端进行局部Value合并
 * 4.Spill溢出多个文件合并Merge（采用归并排序进行合并，合并后还是有序）
 * 5.到Reduce端再进行Merge（采用归并排序进行合并，合并后还是有序），合并成大文件
Shuffle结束
 * 6.Reduce执行
 * 7.最后通过OutputFormat方法将结果数据写出到输出文件夹中
 * 注：环形缓冲区的大小可以通过在mapred-site.xml中设置mapreduce.task.io.sort.mb的值来改变，默认是100M。Map端溢出的时候会先调用Combiner组件，逻辑
 * 和Reduce是一样的，合并，相同的key对应的value值相加，这样传送效率高，不用一下子传好多相同的key，在数据量非常大的时候，这样的优化可以节省很多网络宽带和
 * 本地磁盘IO流的读写。
 */
public class WordCountDemo {
    //Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
    public static class WcMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        //Ctrl+o：先将光标放在本类的任意位置，按快捷键Ctrl+o便可快速得到父类中的所有方法：

        public WcMapper() {
            super();
        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\\s+");   //将句子按空格切分，正则表达式\\s表示空格
            if(strs.length>0){
                for(String word:strs){
                    context.write(new Text(word),new IntWritable(1));
                }
            }
        }
    }
    public static class WcReduce extends Reducer<Text,IntWritable,Text,IntWritable> {
        public WcReduce() {
            super();
        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
//            int sum = 0 ;
//            for(IntWritable count:values){
//                sum+=count.get();
//            }
//            context.write(key,new IntWritable(sum));
            int sum = 0;
            System.out.print("Reduce:"+key.toString()+"迭代中的value值：[");
            StringBuffer strBuf = new StringBuffer();
            for(IntWritable item:values){
                strBuf.append(item.toString()+",");
                sum+=item.get();
            }
            //去掉最后的逗号
            System.out.print(strBuf.toString().substring(0,strBuf.toString().length()-1)+"]");
            System.out.println(",Reduce后输出的value值："+sum);
            context.write(key, new IntWritable(sum));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }

    public static class WcCombiner extends Reducer<Text,IntWritable,Text,IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            System.out.print("Combiner:"+key.toString()+"迭代中的value值：[");
            StringBuffer strBuf = new StringBuffer();
            for(IntWritable item:values){
                strBuf.append(item.toString()+",");
                sum+=item.get();
            }
            //去掉最后的逗号
            System.out.print(strBuf.toString().substring(0,strBuf.toString().length()-1)+"]");
            System.out.println(",Combiner后输出的value值："+sum);
            context.write(key, new IntWritable(sum));
        }
    }
    public static class WcPartitioner extends Partitioner<Text,IntWritable>{
        @Override
        public int getPartition(Text key, IntWritable value, int numPartitions) {
            try{
                String keyStr=key.toString();
                char word = keyStr.charAt(0);
                System.out.println("numPartitions:"+numPartitions);
                int partnum = 0;
                if((word>='a'&&word<='d')/*||(word>='A'&&word<='D')*/){
                    partnum =  0;
                }else if(word>='d'&&word<='m'){
                    partnum =  1;
                }else if(word>'m'&&word<='p'){
                    partnum =  2;
                }else if(word>'p'&&word<='z'){
                    partnum =  3;
                }
//		partnum=partnum%numPartitions;
                System.out.println("word-partnum:"+key.toString()+"-"+partnum);
                return partnum;
            }catch(Exception e){
                System.out.println("error word-partnum:"+key.toString()+"-"+0);
                return 0;
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String inputPathStr = "/wordcount_in",outputPathStr = "/output/day1112_03";
        if(args.length>=2){
            inputPathStr = args[0];
            outputPathStr = args[1];
        }
        Configuration conf= new Configuration();
        conf.set("mapreduce.job.jar", "./target/JxufeHadoopTraining-1.0-SNAPSHOT.jar");
        Job job = Job.getInstance(conf);

        //设置驱动类
        job.setJobName("WordCountDemo");
        job.setJarByClass(WordCountDemo.class);
        //设置Map
        job.setMapperClass(WcMapper.class);
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path(inputPathStr));

        //设置Reduce
        job.setReducerClass(WcReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(4);
        FileSystem fileSystem = FileSystem.get(new Configuration());
        if (fileSystem.exists(new Path(outputPathStr))) {
            fileSystem.delete(new Path(outputPathStr), true);
        }
        TextOutputFormat.setOutputPath(job,new Path(outputPathStr));

        //设置其他步骤
        job.setCombinerClass(WcCombiner.class);   //对比效果
        job.setPartitionerClass(WcPartitioner.class);




        //启动
        System.out.println(job.waitForCompletion(true)?"SUCCESS":"FAILED");



    }
}
