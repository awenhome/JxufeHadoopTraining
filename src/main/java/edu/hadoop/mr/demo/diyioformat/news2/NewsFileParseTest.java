package edu.hadoop.mr.demo.diyioformat.news2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
public class NewsFileParseTest {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "news parse");
		job.setJarByClass(NewsFileParseTest.class);
		job.setMapperClass(TokenizerMapper.class);
//		job.setReducerClass(NewsFileParse.IntSumReducer.class);
		job.setNumReduceTasks(1); // Reduce任務個數，如果爲0，則不執行Reduce且map直接輸出結果不會按key進行排序
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NewsInfo.class);
		FileInputFormat.addInputPath(job, new Path("hdfs://hadoop01:9000/news_datainput/news_small_3.txt"));
		FileSystem fileSystem = FileSystem.get(new URI("/news_out_test"), // 如果输出文件路径存在则删除
				new Configuration());
		Path path = new Path("/news_out_test");
		if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, new Path("/news_out_test"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class TokenizerMapper extends Mapper<Object, Text, Text, NewsInfo> {
		int i = 0;
		private Text outputKey = new Text();
		private Text outputValue = new Text();
		StringBuffer oneNewsSuitStrBuff = new StringBuffer();
		Configuration conf = new Configuration();
		FileSystem fileSystem;
		FSDataOutputStream out;
		String hdfsPath = "news_out_test/news_100_out.txt";

		@Override
		public void setup(Context context) {
			//初始化全局变量，如fileSystem；out等
			try {
				fileSystem = FileSystem.get(new URI(hdfsPath), conf);
				out = fileSystem.create(new Path(hdfsPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			/**
			 * @简介  核心代码
			 * @param key默认是文件指针偏移量
			 * @param value默认是文本一行
			 * @param content.write（outputkey,outputvalue)用以写到文件
			 * @提示   每条记录确定后也可以直接写入out流中
			 */
			if(value.toString().matches("[0-9]{1,}\\|\\|[\\s\\S]{0,}")&&oneNewsSuitStrBuff.length()!=0){
				//此时oneNewsSuitStrBuff就是上一条news信息
				String[] newInfos = oneNewsSuitStrBuff.toString().split("\\|\\|");
				String id = newInfos[0];
				String url = newInfos[1];
				String anthor = newInfos[2];
				String title = newInfos[3];
				String clicknum = newInfos[4];
				String crawldate = newInfos[5];
				String publishtime = newInfos[6];
				String sourcecard = newInfos[7];
				String content = newInfos[8];
				//1.新建NewsInfo newsinfo对象，get   set。
				NewsInfo newsInfo = new NewsInfo(Integer.parseInt(id), url, anthor, title, Integer.parseInt(clicknum), crawldate, publishtime, sourcecard, content);
				context.write(new Text(newsInfo.getCrawldate()),newsInfo);
//				InputStream in_withcode = new ByteArrayInputStream((newsInfo.toKeyValueString() + "\n").getBytes("UTF-8"));
//				IOUtils.copyBytes(in_withcode, out, (newsInfo.toKeyValueString() + "\n").getBytes().length, false);
				// 把value放到oneNewsSuitStrBuff，value是下一条的第一行
				oneNewsSuitStrBuff = new StringBuffer();  //从缓冲中清空上一条记录
			}
			oneNewsSuitStrBuff.append(value.toString());
			
		}
		
		@Override
		protected void cleanup(Mapper<Object, Text, Text, NewsInfo>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.cleanup(context);
		}
	}

	public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
		}
		}

}
