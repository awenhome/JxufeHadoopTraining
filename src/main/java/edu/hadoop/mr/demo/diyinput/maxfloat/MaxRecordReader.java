package edu.hadoop.mr.demo.diyinput.maxfloat;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MaxRecordReader extends RecordReader<IntWritable, ArrayWritable>{
	private MaxSplit split = null;
	Configuration conf;
	private IntWritable key = null;
	private ArrayWritable value = null;
	private int c_start;	//当前块的起始位置
	private int c_stop;		//当前块的结束位置
	private int c_index;	//此次读取当前块的位置
	
	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		//拿到一个split，和上下文配置
		this.split = (MaxSplit) genericSplit;
		this.conf = context.getConfiguration();
		this.c_start = this.split.getStartIndex();
		this.c_stop = this.split.getEndIndex();
		this.c_index = this.c_start;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// 从split中解析出<IntWritable, ArrayWritable>的<K,V>对，搞清楚开始是什么状态，什么条件停止
		if(key == null) {
			key = new IntWritable();
		}
		if(value == null) {
			value = new ArrayWritable(FloatWritable.class);
		}
		if(c_index <= c_stop) {
			key.set(c_index);
			value = split.getFloatArray();
			c_index = c_stop + 1;
			return true;
		}else {		//读完了，停止
			return false;
		}
	}

	@Override
	public IntWritable getCurrentKey() throws IOException, InterruptedException {
		return this.key;
	}

	@Override
	public ArrayWritable getCurrentValue() throws IOException, InterruptedException {
		return this.value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return this.c_start == this.c_stop?0:1;
	}

	@Override
	public void close() throws IOException {
		
	}
	
}
