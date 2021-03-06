package edu.hadoop.mr.demo.classtest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
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
 * @说明 带学生实现WordCount程序
 输入文件：
hello world world   <K1,V1> K1=0,V1="hello world world"
world count hello   <K1,V1> K1=19,V1="world count hello"
 要求：统计文件中单词出现的次数
 */
public class WordCountTest001 {
    /**
     * Map()每次处理的是一行数据，例如：hello world world
     * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     *     K1==KEYIN：0===>Long===>LongWritable
     *     V1==VALUEIN：hello world world===>String===>Text
     *     (K1, V1) → list(K2, V2)
     *     K2==KEYOUT===>String===>Text      单词
     *     V2==VALUEOUT===>int===>IntWritable     1
     *      list(K2, V2)==>{<hello,1>,<world,1>,<world,1>}
     */
    public  static  class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable>{
        //Ctrl+O

        int lineNumber;
        /**
         * @描述 setup在Map执行前执行且仅仅执行一次
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            lineNumber = 0;
        }

        /**
         * @描述  while死循环，每次传入文件的一行直到文件结尾
         * @param key 行首字符在文件中的下标位置，Long类型
         * @param value 按行读取，每次传入一行内容
         * @param context
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            lineNumber++;
            //TODO (K1, V1) → list(K2, V2)
            //K1=0,V1="hello world world"
            System.out.print("map in value:"+value.toString()+"===>");
            String[] words = value.toString().split("\\s+");
            System.out.print("map out:");
            for(int i=0;i<words.length;i++){
                String word = words[i];
                context.write(new Text(word),new IntWritable(1));
                System.out.print("("+word+",1)\t");
            }
            System.out.println();
        }


        /**
         * @描述 cleanup在Map执行后执行且仅仅执行一次
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            System.out.println("该文件一共行数是："+lineNumber);
        }

        @Override
        public void run(Context context) throws IOException, InterruptedException {
            super.run(context);
        }
    }

    /**
     * @分区规则:按照单次得收个字母来分区
     *      分区0：首字母不在[a-z]以内的;
     *      分区1：首字母在[a,h]中;
     *      分区2：首字母在(h,z]中;
     */
    public static class WordCountPartitioner extends Partitioner<Text,IntWritable>{
        @Override
        public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
            try {
                System.out.println("执行了getPartition方法");
                String word = text.toString();
                char firstAlphabet = word.charAt(0);
                if(firstAlphabet>='a'&&firstAlphabet<='h'){
                    return 1%numPartitions;
                }else if(firstAlphabet>'h'&&firstAlphabet<='z'){
                    return 2%numPartitions;
                }else{
                    return 0%numPartitions;
                }
            }catch (Exception e){
//                e.printStackTrace();
                return 0%numPartitions;
            }
        }
    }
    /**
     * @说明  相当于在Map端发送之前先将key对应的value数值合并
     * @执行时间 在Map端Spill到文件时执行
     */
    public static class WordCountCombiner extends Reducer<Text,IntWritable,Text,IntWritable>{
        //CTRL+O

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //TODO 将单词key出现次数的列表values中的值进行累加，得到出现的总次数
            int countAll = 0;
            System.out.print("Combiner Key="+key.toString()+";values:<");
            for(IntWritable countOne:values){
                countAll+=countOne.get();
                System.out.print(countOne.get()+" ");
            }
            System.out.println(">");
            context.write(key,new IntWritable(countAll));
            System.out.println("Combiner out:("+key.toString()+","+countAll+")");
        }
    }

    /**
     * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
     *     Map:<K1,V1></K1,V1>==>List<K2,V2>
     *     Map的输出就是Reduce的输入
     *    K2==KEYIN===>单词==>String==>Text
     *    V2==VALUEIN==>类型是数字==>int==>IntWritable
     *    <K2,V2>==><K3,V3>
     *     KEYOUT===>单词==>Text
     *     VALUEOUT===>总次数==>IntWritable
     *
     */
    public static class WordCountReduce extends Reducer<Text,IntWritable,Text,IntWritable>{
        //CTRL+O

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //TODO 将单词key出现次数的列表values中的值进行累加，得到出现的总次数
            int countAll = 0;
            System.out.print("Key="+key.toString()+";values:<");
            for(IntWritable countOne:values){
                countAll+=countOne.get();
                System.out.print(countOne.get()+" ");
            }
            System.out.println(">");
            context.write(key,new IntWritable(countAll));
            System.out.println("Reduce out:("+key.toString()+","+countAll+")");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.Java Client:submit Job
        Configuration configuration = new Configuration();
//        configuration.set("fs.defaultFS","hdfs://hadoop01:9000");
        Job job = Job.getInstance(configuration);
        //2.设置驱动类
        job.setJarByClass(WordCountTest001.class);
        //3.Mapper端设置
        TextInputFormat.addInputPath(job,new Path("/wc_in"));
//        TextInputFormat.addInputPath(job,new Path("/stopword.txt"));
//        TextInputFormat.addInputPath(job,new Path("/wordcount_in/"));
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //4.Reduce设置
        job.setReducerClass(WordCountReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(3);

        //5.设置分区或者合并
        job.setPartitionerClass(WordCountPartitioner.class);
        job.setCombinerClass(WordCountCombiner.class);


        //输出文件夹如果存在则删除
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.delete(new Path("/output/wc_1"),true);
        TextOutputFormat.setOutputPath(job,new Path("/output/wc_1"));
        System.out.printf(job.waitForCompletion(true)?"Job execute SUCCESS":"Job execute Failed");


    }

}
