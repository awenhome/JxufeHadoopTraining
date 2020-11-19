package edu.hadoop.mr.demo.diyinput.maxfloat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.log.Log4Json;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
/**
 * 
 *	IntWritable:起始下标
 *	ArrayWritable:数组
 */

public class MaxInputFormat extends InputFormat<IntWritable, ArrayWritable>{
	public static float[] values;
	private static final Log LOG = LogFactory.getLog(MaxInputFormat.class);
	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {
		// generate splits	随机生成100个0~1的小数，默认分成2个分片
		List<InputSplit> splits = new ArrayList<InputSplit>();
		int NUM = context.getConfiguration().getInt("NUMOFFINDMAX", 100);
		values = new float[NUM];
		Random random = new Random();
		for(int i=0; i<NUM; i++) {
			values[i] = random.nextFloat();
			LOG.info(values[i]);
		}
		//split
		int splitNum = context.getConfiguration().getInt("mapreduce.job.maps", 2); //切片的个数
		int beg = 0;
		int end = 0;
		int splitLen = (int)Math.floor(NUM/splitNum);//向下取整，每一分片的长度
		end = splitLen - 1;
		for(int i=0; i<splitNum; i++) {
			splits.add(new MaxSplit(beg,end));
			beg = end + 1;
			end = beg + splitLen - 1;
		}
		splits.add(new MaxSplit(beg,NUM-1));
		System.out.println("splits size=" + splits.size());
		return splits;
	}

	@Override
	public RecordReader<IntWritable, ArrayWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		//调用自定义的RecordReader
		return new MaxRecordReader();
	}

	

}
