package edu.hadoop.mr.demo.diyioformat.news;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author 彭文忠
 * @简介 测试类：测试自定义输入输出类是否起作用了。
 *
 */
public class PrintNewsInfo {
	public static final IntWritable ONE = new IntWritable(1);

	public static class UserInfoMapper extends Mapper<Text, NewsInfo, Text, NewsInfo> {
		@Override
		protected void map(Text key, NewsInfo value, Mapper<Text, NewsInfo, Text, NewsInfo>.Context context)
				throws IOException, InterruptedException {
			super.map(key, value, context);
		}
	}

	public static void main(String[] args) {
		try {
			System.setProperty("HADOOP_USER_NAME", "jxufe");
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "NewsInfo");

			job.setJarByClass(PrintNewsInfo.class);
			job.setMapperClass(UserInfoMapper.class);

			// 定制输入格式：
			job.setInputFormatClass(NewsTextInputFormat.class);
			// 定制输出格式：
			job.setOutputFormatClass(NewsInfoTextOutputFormat.class);

			job.setMapOutputKeyClass(Text.class);
			// 用的自己定义的数据类型
			job.setMapOutputValueClass(NewsInfo.class);

//			FileInputFormat.addInputPath(job, new Path("hdfs://master:9000/user/hadoop/datainput/news_small_2.txt"));
			FileInputFormat.addInputPath(job, new Path("hdfs://hadoop01:9000/news_datainput/news_small_3.txt"));
			FileOutputFormat.setOutputPath(job,
					new Path("hdfs://hadoop01:9000/news_dataoutput/news_small_3/" + System.currentTimeMillis() + ".txt"));
			System.exit(job.waitForCompletion(true) ? 0 : 1);// 执行job

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}