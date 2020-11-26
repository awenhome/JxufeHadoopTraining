package edu.hadoop.mr.demo.sortnumber;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @author 彭文忠
 * @date 2020/11/12
 * @说明 手把手教学敲代码：整数文件排序：文件中一个数字一行
 *       数字全局排序（降序）
 */
public class InvertSortNumberTest {
    /**
     * Map处理一个文件分块Split
     *
     * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     *     K1:KEYIN:每行开始下标 ==>Long==>序列化：LongWritable
     *     V1:VALUEIN：每行的内容 ==>String===>序列化：Text
     *     K2:KEYOUT:Map输出的KEY==>String===>Text
     *     V2:VALUEOUT:Map输出的VALUE==>Int===>IntWritable
     * Map接口用来处理文件中一行
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text,MyIntWritable, NullWritable>{
        //Ctrl+O快捷键，显示父类中有哪些方法
        int lineNumber;
        /**
         * @说明 在map方法执行前执行且仅执行一次
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            lineNumber = 0;
        }

        @Override
        protected void map(LongWritable keyIn, Text valueIn, Context context) throws IOException, InterruptedException {
            //TODO 处理一行内容，从<K1,V1>到List<K2,V2>
            lineNumber++;
            //valueIn例子：hello world world  ==><hello,1>,<world,1>;<world,1>
            String numberStr = valueIn.toString();  //空白字符切分
            context.write(new MyIntWritable(Integer.parseInt(numberStr.trim())),NullWritable.get());
            System.out.println("map out:"+numberStr+","+1);
        }

        /**
         * @说明 在map方法执行后执行且仅执行一次
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            System.out.println("文件片一共有行数："+lineNumber);
        }


    }

    /**
     * @说明  实现Reduce类
     * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
     *     KEYIN==Map输出的KEY==>Text
     *     VALUEIN==Map输出的Value==>IntWritable
     *     KEYOUT==Reduce输出的KEY==>Text
     *     VALUEOUT==Reduce输出的Value==>IntWritable
     */
    public static class WordCountReduce extends Reducer<MyIntWritable,NullWritable,MyIntWritable,NullWritable>{
        //Ctrl+O

        /**
         * @param key  单词
         * @param values 单词出现次数的数组：未Combiner格式如<1,1,1,1,1,1,...>；如果Combiner，格式如：<60,40,50></60,40,50>
         * @param context 上下文：用来write
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(MyIntWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key,NullWritable.get());
            System.out.println("Reduce out:"+key.toString());
        }
    }

//    /**
//     * @简介 自定义分区
//     * @分区规则  按Key(单词)的首字母来判断分区，规则：
//     *      分区0：<0
//     *      分区1：[0,30]
//     *      分区2：[31,70]
//     *      分区3:>70
//     */
    public static class WordCountPartitioner extends Partitioner<MyIntWritable,NullWritable>{
        @Override
        public int getPartition(MyIntWritable key, NullWritable value, int numPartitions) {
            try {
                System.out.println("WordCountPartitioner执行了");
                int number = key.get();
                if(number<50)
                    return 0%numPartitions;
                else
                    return 1%numPartitions;
            }catch(Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String inputPath = "/filesort",outputPath="/output/filesort";
        if(args.length>=2) {
            inputPath =  args[0];
            outputPath=args[1];
        }
        //1.Client提交Job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //2.设置任务的入口：驱动类
        job.setJarByClass(InvertSortNumberTest.class);

        //3.设置Mapper相关
        //设置输入文件路劲：add可以添加多个不同路劲的文件或文件夹
        TextInputFormat.addInputPath(job,new Path(inputPath));
//        TextInputFormat.addInputPath(job,new Path("/wc_in/"));
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(MyIntWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        //4.设置Reduce类
        job.setReducerClass(WordCountReduce.class);
        job.setNumReduceTasks(2);  //多少个Reduce任务
        job.setOutputKeyClass(MyIntWritable.class);
        job.setOutputValueClass(NullWritable.class);
        //5.设置job任务输出
        //输出路劲如果存在则删除
        FileSystem.get(conf).delete(new Path(outputPath),true);
        TextOutputFormat.setOutputPath(job,new Path(outputPath));

        //6.Job其它设置：Combiner||Partitioner
        job.setPartitionerClass(WordCountPartitioner.class);


        System.out.println(job.waitForCompletion(true)?"Job execute SUCCESS":"Job execute FAILED");  //sout

    }

}
