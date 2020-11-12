package edu.hadoop.mr.demo;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @author 彭文忠
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
            String[] strs = value.toString().split(" ");
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
            int sum = 0 ;
            for(IntWritable count:values){
                sum+=count.get();
            }
            context.write(key,new IntWritable(sum));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String inputPathStr = "/wordcount_in",outputPathStr = "/output/day1112_01";
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
        job.setNumReduceTasks(2);
        FileSystem fileSystem = FileSystem.get(new Configuration());
        if (fileSystem.exists(new Path(outputPathStr))) {
            fileSystem.delete(new Path(outputPathStr), true);
        }
        TextOutputFormat.setOutputPath(job,new Path(outputPathStr));

        //启动
        System.out.println(job.waitForCompletion(true)?"SUCCESS":"FAILED");



    }
}
