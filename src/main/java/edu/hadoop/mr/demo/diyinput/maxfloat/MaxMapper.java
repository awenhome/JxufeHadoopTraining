package edu.hadoop.mr.demo.diyinput.maxfloat;

import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxMapper extends Mapper<IntWritable, ArrayWritable, NullWritable, FloatWritable>{
	@Override
	protected void map(IntWritable key, ArrayWritable value,
			Context context)
			throws IOException, InterruptedException {
		FloatWritable[] array = (FloatWritable[])value.toArray();
		float max = 0;
		for(FloatWritable num:array) {
			if(num.get() > max) {
				max = num.get();
			}
		}
		context.write(NullWritable.get(), new FloatWritable(max));
	}
}
