package edu.hadoop.mr.demo.demo_stu_avg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class AvgApp {
    public static void main(String[] args) throws  Exception{
        if (args == null || args.length < 2) {
            args=new String[]{"/inputdata/demo_stu_avg","/outdata/demo_stu_avg"};
        }
        //1.新建配置对象，为配置对象设置文件系统
        Configuration conf = new Configuration() ;
//        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //2.设置Job属性
        Job job = Job.getInstance(conf, "AvgApp");
        job.setJarByClass(AvgApp.class);
        //3.设置数据输入路径
        Path inPath = new Path(args[0]);
        FileInputFormat.addInputPath(job,inPath);
        //4.设置Job执行的Mapper类
        job.setMapperClass(MyMapper.class);
        //5.设置Job执行的Reducer类和输出K-V类型
        job.setReducerClass(MyReducer.class);
        job.setNumReduceTasks(1);  //默认
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //6.递归删除数据目录
        FileSystem.get(conf).delete(new Path(args[1]),true);
        //7.设置数据输出路径
        Path outPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job,outPath);
        //8.MapReduce作业完成后退出系统
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
