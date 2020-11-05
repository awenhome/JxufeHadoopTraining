package edu.hadoop.mr.demo.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author hadoop
 * @describe mapper类
 * @参数1 KEYIN 文件读取偏移量  LONG  ==>LongWritable
 * VALUEIN  一行文本（a b c f e )  String ==>Text
 * KEYOUT: a   Text.toString.split
 * VALUEOUT: 1
 * hadoop Writale接口的类转String 用：toString();
 * java转hadoop序列花   new Text(String)   new IntWritable(int)
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//key 号行还是偏移量
		System.out.println("map keyin:"+key.toString());
		System.out.println("map valuein:"+value.toString());
		//以下是java代码
		String valueStr = value.toString();
		String[] words = valueStr.split(" ");
		System.out.println("map out:");
		for(String oneWord:words){
			context.write(new Text(oneWord) , new IntWritable(1));
			System.out.println("	["+oneWord+","+"1] ");
		}
	}
}
