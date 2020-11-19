package edu.hadoop.mr.demo.diyinput.news;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

/**
 * @author 彭文忠
 * @简介 重写获得map输入记录类：从文件块到map的key-value方法
 */
public class NewsInfoRecordReader extends RecordReader<Text, NewsInfo> {
	public static final String KEY_VALUE_SEPERATOR = "mapreduce.input.keyvaluelinerecordreader.key.value.separator";

	private final LineRecordReader lineRecordReader;

	private byte separator = (byte) '\t';

	private Text innerValue;
	private Text key;

	private NewsInfo value;
	private StringBuffer strBuf = new StringBuffer();

	public Class getKeyClass() {
		return Text.class;
	}

	public NewsInfoRecordReader(Configuration conf) throws IOException {
		lineRecordReader = new LineRecordReader();
		String sepStr = conf.get(KEY_VALUE_SEPERATOR, "\t");
		this.separator = (byte) sepStr.charAt(0);
	}

	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException {
		lineRecordReader.initialize(genericSplit, context);
	}

	/**
	 * @简介  返回标示符号sep在utf的位置下标
	 * @說明  因爲我給的數據沒有key，每次以行value直接從0開始取
	 * @param utf
	 * @param start
	 * @param length
	 * @param sep
	 * @return
	 */
	public static int findSeparator(byte[] utf, int start, int length, byte sep) {
		for (int i = start; i < (start + length); i++) {
			if (utf[i] == sep) {
				return i;
			}
		}
		return -1; // 将这个截取标识符的位置给返回回去。
	}

	public static void setKeyValue(Text key, NewsInfo value, byte[] line, int lineLen, int pos) {
		if (pos == -1) {
//			key.set(line, 0, lineLen);
			value.setId(0);
			value.setUrl("");
			value.setAnthor("");
			value.setTitle("");
			value.setClicknum(0);
			value.setCrawldate("");
			value.setPublishtime("");
			value.setSourcecard("");
			value.setContent("");
		} else {
//			key.set(pos+""); // 设置键 从 第 0位置 到 截取标识符的位置
			Text text = new Text();
			text.set(line, pos , lineLen + pos ); // 这里默认是1行内容
			System.out.println("text的值： " + text);
			String[] str = text.toString().split("\\|\\|");

			value.setId(Integer.parseInt(str[0]));
			value.setUrl(str[1]);
			value.setAnthor(str[2]);
			value.setTitle(str[3]);
			value.setClicknum(Integer.parseInt(str[4]));
			value.setCrawldate(str[5]);
			value.setPublishtime(str[6]);
			value.setSourcecard(str[7]);
			value.setContent(str[8]);

			 System.out.println( "key--> " + key);
			 System.out.println( "value--> "+value +"\n\n");
		}
	}
	//一次就处理一行
//	public synchronized boolean nextKeyValue() throws IOException {
//		byte[] line = null;
//		int lineLen = -1;
//		if (key == null) {
//			key = new Text();
//		}
//		if (value == null) {
//			value = new NewsInfo();
//		}
//		if (lineRecordReader.nextKeyValue()) {
//			innerValue = lineRecordReader.getCurrentValue();  //默认是以行读取一行内容
//			line = innerValue.getBytes();
//			lineLen = innerValue.getLength();
//		} else {
//			return false;
//		}
//		
//		if (line == null||line.length==0) {
//			return false;
//		}
//
//		LongWritable pos = lineRecordReader.getCurrentKey();  //pos是每行開始的位置
//		key.set(pos.get()+"");   
//		setKeyValue(key, value, line, lineLen, 0);
//		return true;
//	}

	/**
	 * @author 彭文忠
	 * @簡介  如何一次護理多行
	 */
	public synchronized boolean nextKeyValue() throws IOException {
		byte[] line = null;
		int lineLen = -1;
		if (key == null) {
			key = new Text();
		}
		if (value == null) {
			value = new NewsInfo();
		}

		//處理一次讀入多行
		while (lineRecordReader.nextKeyValue()) {
			innerValue = lineRecordReader.getCurrentValue();  //默认是以行读取一行内容
			if(innerValue.toString().matches("[0-9]{1,}\\|\\|[\\s\\S]{0,}") && strBuf.length() != 0){
				
				//1.這行之前的內容即爲一條完整記錄，2.這行之後內容即下一行記錄的第一行
				line = strBuf.toString().getBytes();
				lineLen = line.length;
				if (line == null||line.length==0) {
					return false;
				}
				LongWritable pos = lineRecordReader.getCurrentKey();  //pos是每行開始的位置
//				key.set(pos.get()+"");   
				setKeyValue(key, value, line, lineLen, 0);
				//以上完成前一条记录了
				
				//以下开始当前一条记录
				strBuf=new StringBuffer();
				strBuf.append(innerValue);
				return true;
			}
			strBuf.append(innerValue);
//			key.set(lineRecordReader.getCurrentKey()+"\n");  //讀到文件尾端會返回空指針，顧在這裏取最後一行
		} 
		//最後所有數據處理完，文件讀到末尾則還要做一次
		//1.這行之前的內容即爲一條完整記錄，2.這行之後內容即下一行記錄的第一行
		line = strBuf.toString().getBytes();
		lineLen = line.length;
		if (line == null||line.length==0) {
			return false;
		}
		setKeyValue(key, value, line, lineLen, 0);
		strBuf=new StringBuffer();
		strBuf.append(innerValue);
		return true;
	}

	public Text getCurrentKey() {
		return key;
	}

	public NewsInfo getCurrentValue() {
		return value;
	}

	public float getProgress() throws IOException {
		return lineRecordReader.getProgress();
	}

	public synchronized void close() throws IOException {
		lineRecordReader.close();
	}

}