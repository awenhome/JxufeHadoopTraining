package edu.hadoop.mr.demo.diyioformat.maxfloat;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxDriver {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf,"FindMaxValue");
		job.setJarByClass(MaxDriver.class);
		job.setMapperClass(MaxMapper.class);
		job.setReducerClass(MaxReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(FloatWritable.class);
		job.setInputFormatClass(MaxInputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/output/FindMaxValue1"));
		System.exit(job.waitForCompletion(true)?0:1);
	}

}
