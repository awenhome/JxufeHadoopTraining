package edu.hadoop.mr.demo.stuproblem.demo_stu_avg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AvgApp {
    public static void main(String[] args) throws Exception{
        if (args == null || args.length < 2){
            args= new String[]{"/inputdata/demo_stu_avg","/outputdata/filejoin"};
           // throw  new Exception("参数不足，需要两个参数！");
        }
        //1.新建配置对象,为配置对象设置文件系统
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //2.设置job属性
        Job job = Job.getInstance(conf,"AvgApp");
        job.setJarByClass(AvgApp.class);
        //3.设置数据输入路径
        Path inpath = new Path(args[0]);
        FileInputFormat.addInputPath(job,inpath);
        //4.设置Job的输入格式
        job.setMapperClass(MyMapper.class);
        //5.设置Job执行的Reducer类和输出的K-V类型
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //6.递归删除输入目录
        FileSystem.get(conf).delete(new Path(args[1]),true);
        //7.设置数据输出路径
        Path outPath=new Path(args[1]);
        FileOutputFormat.setOutputPath(job,outPath);
        //8.MapReduce作业完成后退出系统
        System.exit(job.waitForCompletion(true) ? 0:1);
    }
}
