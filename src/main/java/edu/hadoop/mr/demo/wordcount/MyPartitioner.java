package edu.hadoop.mr.demo.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartitioner extends Partitioner<Text, IntWritable> {

	@Override
	public int getPartition(Text key, IntWritable value, int numPartitions) {
		try{
		String keyStr=key.toString();
		char word = keyStr.charAt(0);
		System.out.println("numPartitions:"+numPartitions);
//		System.out.println("word:"+word);
		int partnum = 0;
		if((word>='a'&&word<'c')/*||(word>='A'&&word<'M')*/){
			partnum =  0;
		}else if(word>='c'&&word<='m'){
			partnum =  1;
		}else if(word>'m'&&word<='p'){
			partnum =  2;
		}else if(word>'p'&&word<='z'){
			partnum =  3;
		}
//		partnum=partnum%numPartitions;
		System.out.println("word-partnum:"+key.toString()+"-"+partnum);
		return partnum;
//		return 0;
		}catch(Exception e){
			System.out.println("error word-partnum:"+key.toString()+"-"+0);
			return 0;
		}
	}

}
