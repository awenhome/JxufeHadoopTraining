package edu.hadoop.mr.demo.diyioformat.maxfloat;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxReducer extends Reducer<NullWritable, FloatWritable, NullWritable, FloatWritable>{
	@Override
	protected void reduce(NullWritable key, Iterable<FloatWritable> values,
			Context context)
			throws IOException, InterruptedException {
		float max = 0;
		for(FloatWritable val:values) {
			if(val.get() > max) {
				max = val.get();
			}
		}
		context.write(NullWritable.get(), new FloatWritable(max));
	}
}
