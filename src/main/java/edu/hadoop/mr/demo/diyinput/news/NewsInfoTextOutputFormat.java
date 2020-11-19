package edu.hadoop.mr.demo.diyinput.news;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * @author 彭文忠
 * @简介 重写输出类格式
 */
public class NewsInfoTextOutputFormat<K, V> extends FileOutputFormat<K, V> {
	public static String SEPERATOR = "mapreduce.output.textoutputformat.separator";
	protected static class LineRecordWriter<K, V> extends RecordWriter<K, V> {
		private static final String utf8 = "UTF-8";
		private static final byte[] newline;
		static {
			try {
				newline = "\n".getBytes(utf8);
				//System.out.println(  "newline --> " + newline);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8 + " encoding");
			}
		}

		protected DataOutputStream out;
		private final byte[] keyValueSeparator;

		public LineRecordWriter(DataOutputStream out, String keyValueSeparator) {
			this.out = out;
			try {
				this.keyValueSeparator = keyValueSeparator.getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8 + " encoding");
			}
		}

		public LineRecordWriter(DataOutputStream out) {
			this(out, "\t");
		}

		private void writeObject(Object o) throws IOException {
			if(o instanceof NewsInfo){
				NewsInfo to = (NewsInfo) o;
				System.out.println(  "o instanceof NewsInfo  --> True : "+ to.toKeyValueString()  );
				out.write(to.toKeyValueString().getBytes(), 0, to.toKeyValueString().getBytes().length);
			}
			else if (o instanceof Text) {
				Text to = (Text) o;
				System.out.println(  "o instanceof Text  --> True : "+ to.toString()  );
				out.write(to.getBytes(), 0, to.getLength());
			} else {
				out.write(o.toString().getBytes(utf8));
				System.out.println( "o instanceof Text  --> false : "+ o.toString()  );
			}
		}

		public synchronized void write(K key, V value) throws IOException {
			boolean nullKey = key == null || key instanceof NullWritable;
			boolean nullValue = value == null || value instanceof NullWritable;
			System.out.println(  "nullKey--> "+nullKey +" ,  nullValue--> "+nullValue);
			if (nullKey && nullValue) {
				return;
			}
			System.out.println( " nullkey --> "+ nullKey + ", !nullkey -->"+nullKey);
			if (!nullKey) {
				writeObject(key);
			}
			System.out.println( "(nullKey || nullValue) --> " + (nullKey || nullValue) );
			if (!(nullKey || nullValue)) {
				out.write(keyValueSeparator);
			}
			if (!nullValue) {
				writeObject(value);
			}
			out.write(newline);
		}

		public synchronized void close(TaskAttemptContext context) throws IOException {
			out.close();
		}
	}

	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		boolean isCompressed = getCompressOutput(job);
//		String keyValueSeparator= conf.get(SEPERATOR, "---->");
		String keyValueSeparator= conf.get(SEPERATOR, "");
		System.out.println(  "keyValueSeparator---> "+keyValueSeparator);
		CompressionCodec codec = null;
		String extension = "";
		if (isCompressed) {
			Class<? extends CompressionCodec> codecClass = 
					getOutputCompressorClass(job, GzipCodec.class);
			codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
			extension = codec.getDefaultExtension();
		}
		Path file = getDefaultWorkFile(job, extension);
		System.out.println(  "file --> Path : "+ file  );
		FileSystem fs = file.getFileSystem(conf);
		
		if (!isCompressed) {
			FSDataOutputStream fileOut = fs.create(file, false);
			System.out.println( "if---isCompressed-->: "+fileOut);
			return new LineRecordWriter<K, V>(fileOut, keyValueSeparator);
		} else {
			FSDataOutputStream fileOut = fs.create(file, false);
			System.out.println( "else---isCompressed-->: "+fileOut);
			return new LineRecordWriter<K, V>(new DataOutputStream(codec.createOutputStream(fileOut)),keyValueSeparator);
		}
	}
}