package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author hadoop
 * @簡介 本地文件上傳到服務器，每傳4096字節則輸出一個英文句號以做提示
 * @命令行例子：
 * Linux下测试：/home/hadoop/hadoopdata/law_100.txt updatetest/law_test.txt
 * Window下测试：E:/sftp/hadooplocaldata/hello.txt /upload/hello.txt
 */

public class FileCopyWithProgress6 {
	public static void main(String[] args) throws Exception {
        String localSrc = "";
        String dst = "";
	    if(args.length>=2){
	        localSrc = args[0];
	        dst = args[1];
	    }

		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		System.setProperty("HADOOP_USER_NAME", "jxufe");
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS","hdfs://hadoop01:9000");
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress() {
				System.out.print(".");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
