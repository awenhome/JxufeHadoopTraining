package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * @author hadoop
 * @簡介 读取HDFS某个目录下的所有文件
 * @命令行例子 upload 或者 hdfs://hadoop01:9000/user/jxufe/upload
 */
public class ListAllFile7 {
	public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "jxufe");
		String uri = args[0];
		Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}
		FileStatus[] status = fs.listStatus(paths);
		for (FileStatus p : status) {
			System.out.println(p);
		}
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}
}
