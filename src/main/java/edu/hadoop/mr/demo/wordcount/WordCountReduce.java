package edu.hadoop.mr.demo.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/**
 * @author hadoop
 * @知识点 hadoop writable类转化java基础类用get方法：IntWritable.get();
 */
public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable>{
	/**
	 * @param key :one word ,example:a
	 * @param value :EG: <1,1,1,1,1,1>
	 */
	@Override
	protected void reduce(Text key, Iterable<IntWritable> value,
			Context context) throws IOException, InterruptedException {
		int sum = 0;
		System.out.print("Reduce:"+key.toString()+"迭代中的value值：[");
		StringBuffer strBuf = new StringBuffer();
		for(IntWritable item:value){
			strBuf.append(item.toString()+",");
			sum+=item.get();
		}
		//去掉最后的逗号
		System.out.print(strBuf.toString().substring(0,strBuf.toString().length()-1)+"]");
		System.out.println(",Reduce后输出的value值："+sum);
		context.write(key, new IntWritable(sum));
		
	}
}
