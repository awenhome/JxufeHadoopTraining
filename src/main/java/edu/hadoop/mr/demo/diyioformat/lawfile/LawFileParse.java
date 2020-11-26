package edu.hadoop.mr.demo.diyioformat.lawfile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author 彭文忠
 * @简介 读入law_100文件，将文件中每个法律案例按照id：idvalue,title:titleualue,content:
 *     contentvalue输出
 */
public class LawFileParse {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "law parse");
		job.setJarByClass(LawFileParse.class);
		job.setMapperClass(TokenizerMapper.class);
		// job.setReducerClass(LawFileParse.IntSumReducer.class);
		// job.setNumReduceTasks(2);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path("/law_in_test"));
		FileSystem fileSystem = FileSystem.get(new URI("/law_out_test"), // 如果输出文件路径存在则删除
				new Configuration());
		Path path = new Path("/law_out_test");
		if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, new Path("/law_out_test"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class TokenizerMapper extends Mapper<Object, Text, IntWritable, Text> {
		int i = 0;
		private static IntWritable data = new IntWritable();
		StringBuffer oneLawSuitStrBuff = new StringBuffer();
		Configuration conf = new Configuration();
		FileSystem fileSystem;
		FSDataOutputStream out;
		String hdfsPath = "/law_out_test/law_100_out.txt";
		@Override
		public void setup(Context context) {
			try {
				fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
				out = fileSystem.create(new Path(hdfsPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void map(Object key, Text value, Mapper<Object, Text, IntWritable, Text>.Context context)
				throws IOException, InterruptedException {
			if (value.toString().equals("id|title|body")) {
				return;
			}
			// System.out.println(i+"map输入:" + key + ":" + value);
			String valueStr = value.toString();

			data.set(Integer.parseInt(key + ""));

			if (valueStr.matches("[0-9]{1,}\\|[\\s\\S]{0,}") && oneLawSuitStrBuff.length() != 0) { // 如果匹配到是以“数字+|”开头
				// 開始處理這個法律官司內容
				i++;
				String[] oneLawSuits = oneLawSuitStrBuff.toString().split("\\|");
				String id = oneLawSuits[0];
				String title = oneLawSuits[1];
				String lawContent = oneLawSuits[2];
				if (i < 10) {
					System.out.println(i + "id:" + id);
					System.out.println(i + "title" + title);
					System.out.println(i + "lawContent:" + lawContent);
					
				}
				String inStr = "id:"+id+"\ntitle:"+title+"\ncontent:"+lawContent+"\n";
				InputStream   in_withcode   =   new   ByteArrayInputStream(inStr.getBytes("UTF-8"));  
				IOUtils.copyBytes(in_withcode, out, 4096, false); 
				oneLawSuitStrBuff = new StringBuffer();
			}
			oneLawSuitStrBuff.append(value.toString()+"\n");

			// context.write(data, value);
		}
	}

	// public static class IntSumReducer extends Reducer<Text, Text, Text, Text>
	// {
	//
	// public void reduce(Text key, Iterable<IntWritable> values,
	// Reducer<Text, IntWritable, Text, IntWritable>.Context context)
	// throws IOException, InterruptedException {
	// }
	// }

}
