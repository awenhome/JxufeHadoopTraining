package edu.hadoop.mr.demo.diyioformat.maxfloat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class MaxSplit extends InputSplit implements Writable{
	private ArrayWritable floatArray = new ArrayWritable(FloatWritable.class);
	private int startIndex;
	private int endIndex;
	
	public MaxSplit() {
		
	}
	public MaxSplit(int start, int end) {
		this.startIndex = start;
		this.endIndex = end;
		int len = endIndex - startIndex + 1;
		int cindex = start;
		FloatWritable[] ret = new FloatWritable[len];
		for(int i=0; i<len; i++) {
			float f = MaxInputFormat.values[cindex];
			ret[i] = new FloatWritable(f);
			cindex++;
		}
		this.floatArray.set(ret);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.startIndex = in.readInt();
		this.endIndex = in.readInt();
		this.floatArray.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(startIndex);
		out.writeInt(endIndex);
		this.floatArray.write(out);
	}

	@Override
	public long getLength() throws IOException, InterruptedException {
		
		return (this.endIndex - this.startIndex + 1);
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		return new String[]{"localhost","hadoop01","hadoop02","hadoop03"};
	}
	
	public ArrayWritable getFloatArray() {
		return floatArray;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}

}
