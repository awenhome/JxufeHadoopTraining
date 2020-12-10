package edu.hadoop.mr.demo.demo4_9_join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class JoinApp {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            args = new String[]{"/inputdata/filejoin","/outputdata/filejoin"};
//            throw new Exception("参数不足，需要两个参数！");
        }
        //1.新建配置对象，为配置对象设置文件系统
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //2.设置Job属性
        Job job = Job.getInstance(conf,"JoinApp");
        job.setJarByClass(JoinApp.class);
        //3.设置数据输入路径
        Path inPath = new Path(args[0]);
        FileInputFormat.addInputPath(job,inPath);
        //4.设置Job执行的Mapper类
        job.setMapperClass(MyMapper.class);
        //5.设置Job执行的Reducer类和输出K-V类型
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //6.递归删除输出目录
        FileSystem.get(conf).delete(new Path(args[1]),true);
        //7.设置数据输出路径
        Path outPath = new Path(args[1]);
        FileSystem.get(conf).delete(outPath,true);
        FileOutputFormat.setOutputPath(job, outPath);
        //8.MapReduce作业完成后退出系统
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
