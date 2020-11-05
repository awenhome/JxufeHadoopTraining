 package edu.hadoop.mr.demo.wordcount;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author hadoop
 * @描述 给用户Client提交任务（Job)
 * @准备工作
 * 1.把./localdata/txt/wordcount/wordcount.txt文件上传到hdfs的/wordcount_in目录下（shell或java代码均可）
 *  hdfs dfs -mkdir /wordcount_in
 *  hdfs dfs -put ./wordcount.txt(这里路劲要改) /wordcount_in
 * 2.如果要设置用户名，则在VM Options配置中设置：-DHADOOP_USER_NAME=jxufe
 * @参数参考  动态传参可以在Program arguments中传入:
 * 		输入为文件：/wordcount_in/wordcount.txt /wordcountresult
 * 		输入为文件夹：/wordcount_in/ /wordcountresult
 */
public class WordCountDriver {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		//配置信息,配置job任务
		Configuration conf= new Configuration();
		conf.set("mapreduce.job.jar", "./target/JxufeHadoopTraining-1.0-SNAPSHOT.jar");
		//hadoop1.0提交作业方式
//		JobConf jobConf = new JobConf(conf);
//		JobClient jobClient = new JobClient(conf);
//		jobClient.submitJob(jobConf);

		//hadoop2.0 提交作业方式
		Job job = Job.getInstance(conf);
		//设置驱动类
		job.setJarByClass(WordCountDriver.class);
		//设置map
		job.setMapperClass(WordCountMapper.class);
//		job.setPartitionerClass(MyPartitioner.class);
		//设置reducer
		job.setReducerClass(WordCountReduce.class);
		job.setCombinerClass(WordCountCombiner.class);

		//设置输出的key/value类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setNumReduceTasks(2);

		job.setInputFormatClass(TextInputFormat.class);
//		//Combiner小文件：大家可以传两个小文件测试下使用Combiner小文件和不使用时的执行效果
//		job.setInputFormatClass(CombineTextInputFormat.class);
//		CombineTextInputFormat.setMaxInputSplitSize(job, 2*1024);

		Path inputPath = new Path("/wordcount_in/wordcount.txt") ;
		Path outputDirPath = new Path("/wordcountresult");
		if(args.length>=2){
			inputPath = new Path(args[0]);
			outputDirPath = new Path(args[1]);
		}
		CombineTextInputFormat.addInputPath(job, inputPath);
		
		
		
		//设置输入文件/输出文件
//		TextInputFormat.addInputPath(job, new Path(args[0]));
//		FileInputFormat.addInputPath(job, new Path(args[0]));
//		FileInputFormat.setMaxInputSplitSize(job, 2000); //byte字节
//        FileInputFormat.setMinInputSplitSize(job, 200*1024*1024);

		
		//		FileInputFormat.setInputPaths(job, new Path("p1"),new Path("p2"));
		//Output路劲文件夹必须是不存在，为了方便测试，如果文件夹存在则删除
		FileSystem fileSystem = FileSystem.get(new Configuration());
		if (fileSystem.exists(outputDirPath)) {
			fileSystem.delete(outputDirPath, true);
		}
		FileOutputFormat.setOutputPath(job, outputDirPath);
		//提交作业到框架
//		job.submit();
		System.out.println(job.waitForCompletion(true)?"SUCCESS":"FAILED");
		
		
//		//job关联
//		JobControl jobControl = new JobControl("测试");
//		ControlledJob conJob1 = new ControlledJob(job.getConfiguration());
//		ControlledJob conJob2 = new ControlledJob(job.getConfiguration());
//		conJob2.addDependingJob(conJob1);
//		jobControl.addJob(conJob1);
//		jobControl.addJob(conJob2);
//		new Thread(jobControl).start();
		
		
	}
}
