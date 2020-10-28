package edu.hadoop.hdfs.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * @author hadoop
 * @簡介 把本地文件上傳到HDFS上
 * @指令  bin/hadoop -put localpath hdfspath
 */
public class CopyFileFromHdfsToLocal {
public static void main(String[] args) {
	/**
	 * 1.Configuration
	 * 2.FileSystem
	 * 3.輸入流：本地文件   3.1 Java io 3.2 FileSystem.getLocal
	 * 4.輸出流：HDFS.  FileSytem.open
	 */
	Configuration conf;
	FileSystem fs;
	FSDataInputStream in;
	OutputStream out;
	String localFile="/home/hadoop/inputdata/file01.txt";
	String hdfsFile = "hdfs_api_out/file01.txt";
	try{
		conf = new Configuration();
		fs = FileSystem.get(conf);
		in = fs.open(new Path(hdfsFile));
		//輸入流獲取:1.用本地文件系統獲取
		FileSystem localfs = FileSystem.getLocal(new Configuration(false));
		out = localfs.create(new Path(localFile));
		//獲取輸入流方法2  用Java自帶文件流
//		FileInputStream fis = new FileInputStream(localFile);
//		in = new BufferedInputStream(fis);
		
		IOUtils.copyBytes(in, out, 4096, true);//false表示不關閉輸入輸出流
		in.seek(0);//輸入流指針回到文件開始位置
		IOUtils.copyBytes(in, System.out, 4096, true);//把輸入文件內容寫道控制臺輸出
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
