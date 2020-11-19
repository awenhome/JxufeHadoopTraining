package edu.hadoop.mr.demo.diyinput.news2;

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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * @author 彭文忠
 * @简介 读入新闻信息文件，将文件中每个内容转化为key-value值 contentvalue输出
 */
public class NewsFileParseAndReduce {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "news parse");
		job.setJarByClass(NewsFileParseAndReduce.class);
		job.setMapperClass(TokenizerMapper.class);
//		job.setReducerClass(NewsFileParseAndReduce.IntSumReducer.class);
		job.setNumReduceTasks(1); // Reduce任務個數，如果爲0，則不執行Reduce且map直接輸出結果不會按key進行排序
//		job.setMapOutputKeyClass(Text.class);
//		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NewsInfo.class);
		FileInputFormat.addInputPath(job, new Path("/news_datainput/news_small_3.txt"));
		FileSystem fileSystem = FileSystem.get(new URI("/news_out_maptest"), // 如果输出文件路径存在则删除
				new Configuration());
		Path path = new Path("/news_out_maptest");
		if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, new Path("/news_out_maptest"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class TokenizerMapper extends Mapper<Object, Text, Text, NewsInfo> {
		int i = 0;
		private Text outputKey = new Text();
		private Text outputValue = new Text();
		private NewsInfo news;
		StringBuffer oneLawSuitStrBuff = new StringBuffer();
		Configuration conf = new Configuration();
		FileSystem fileSystem;
		FSDataOutputStream out;
		String hdfsPath = "news_out_test/news_100_out.txt";

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

		public void map(Object key, Text value, Mapper<Object, Text, Text, NewsInfo>.Context context)
				throws IOException, InterruptedException {
			if (value.toString().equals("id|url|anthor|title|clicknum|crawldate|publishtime|sourcecard|content")) {
				return;
			}
			// System.out.println(i+"map输入:" + key + ":" + value);
			String valueStr = value.toString();

			if ((valueStr.matches("[0-9]{1,}\\|\\|[\\s\\S]{0,}") && oneLawSuitStrBuff.length() != 0)) { // 如果匹配到是以“数字+|”开头或者已經讀到文件尾端
				// 開始處理這個法律官司內容
				i++;
				String[] oneNewsSuits = oneLawSuitStrBuff.toString().split("\\|\\|");
				String id = oneNewsSuits[0].trim();
				String url = oneNewsSuits[1].trim();
				String anthor = oneNewsSuits[2].trim();
				String title = oneNewsSuits[3].trim();
				String clicknum = oneNewsSuits[4].trim();
				String crawldate = oneNewsSuits[5].trim();
				String publishtime = oneNewsSuits[6].trim();
				String sourcecard = oneNewsSuits[7].trim();
				String content = oneNewsSuits[8].trim();

				
				String inStr = "id:" + id + "\turl:" + url + "\tanthor:" + anthor + "\ttitle:" + title + "\tclicknum:"
						+ clicknum + "\tcrawldate:" + crawldate + "\tpublishtime:" + publishtime + "\tsourcecard:"
						+ sourcecard + "\tcontent:" + content;
				news = new NewsInfo(Integer.parseInt(id), url, anthor, title, Integer.parseInt(clicknum), crawldate, publishtime, sourcecard, content);
				outputKey.set(crawldate); // 按日期排序
//				outputValue.set(inStr);
				context.write(outputKey, news);
				InputStream in_withcode = new ByteArrayInputStream((inStr + "\n").getBytes("UTF-8"));
				IOUtils.copyBytes(in_withcode, out, 4096, false);
				oneLawSuitStrBuff = new StringBuffer();
			}
			oneLawSuitStrBuff.append(value.toString() + "\n");

		}
	}

	public static class IntSumReducer extends Reducer<Text, NewsInfo, Text, Text> {
		private Text outputKey = new Text();
		private Text outputValue = new Text();
		private NewsInfo temp;
		public void reduce(Text key, Iterable<NewsInfo> values,
				Context context) throws IOException, InterruptedException {
			StringBuffer inValue = new StringBuffer();
			temp = new NewsInfo();
			temp.setCrawldate("no date");
			temp.setClicknum(-1);
			for(NewsInfo news:values){
				inValue.append(news.toString());
//				System.out.println("reduce输入："+news.toKeyValueString());
				if (temp.getCrawldate().equals(news.getCrawldate())) { // 日期相同，选出最大点击数
					if (temp.getClicknum() < news.getClicknum()) {
//						temp = news;
						temp.setNewsInfo( news);
					}
				}else{//不相同，则表示前一个日期的可以先写出了
					if(temp.getClicknum()!=-1){
					outputKey.set(temp.getCrawldate());
					outputValue.set(temp.getClicknum()+"");
					context.write(outputKey,outputValue);
					}
					temp.setNewsInfo( news);
				}
			}
			System.out.println("reduce输入："+inValue.toString());
			if(!outputKey.toString().equals(temp.getCrawldate())){  //处理最后一个日期（没法跟下一个判断日期不相同）
				outputKey.set(temp.getCrawldate());
				outputValue.set(temp.getClicknum()+"");
				context.write(outputKey,outputValue);
			}
			
		}
	}

}
