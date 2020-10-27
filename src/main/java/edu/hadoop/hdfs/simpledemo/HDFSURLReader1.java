package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

public class HDFSURLReader1 {
	static {
		//让java能识别hadoop文件系统的URL 
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	public static void main(String[] args) {
		InputStream inputStream = null;
		try {
//			inputStream = new URL(args[0]).openStream();
			inputStream = new URL("hdfs://hadoop01:9000/b.txt").openStream();
			IOUtils.copyBytes(inputStream, System.out, 1024, true);
		    System.out.println("执行完成！");
		} catch (Exception e) {
			IOUtils.closeStream(inputStream);
		    e.printStackTrace();
		}
	}
}
