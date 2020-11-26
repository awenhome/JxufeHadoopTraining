package edu.hadoop.mr.demo.diyioformat.news;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * @author 彭文忠
 * @简介  重写输入InputFormat
 */
public class NewsTextInputFormat extends FileInputFormat<Text, NewsInfo> {
	@Override
	public RecordReader<Text, NewsInfo> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		context.setStatus(split.toString());
		NewsInfoRecordReader userInforRecordReader = new NewsInfoRecordReader(context.getConfiguration() );
		return userInforRecordReader;
	}
}