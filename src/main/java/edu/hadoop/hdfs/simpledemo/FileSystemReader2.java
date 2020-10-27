package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

public class FileSystemReader2 {
	public static void main(String[] args) throws Exception {
		// 会默认读取core-site.xml中的fs.default.name来判断文件系统。如果未设置则默认是本地文件系统
		Configuration configuration = new Configuration();
		String uri = /* args[0] */"hdfs://hadoop01:9000/b.txt";
//		String uri="intputtemperature.txt";  //自动补全hdfs://hadoop01:9000/user/jxufe/路劲
//		String uri="/home/hadoop/workspace/HdfsTest/inputtemperature.txt";//需要注释调fs.default.name配置，则默认为读取本地文件
		FileSystem fileSystem = FileSystem.get(URI.create(uri), configuration);
		//instanceof二元运算符：左边是对象，右边是类；当对象是右边类或子类所创建对象时，返回true；否则，返回false。
		System.out.println("DistributedFileSystem?"+(fileSystem instanceof DistributedFileSystem ));
		InputStream inputStream = null;
		try {
			inputStream = fileSystem.open(new Path(uri));
			IOUtils.copyBytes(inputStream, System.out, 4096, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(inputStream);
		}
	}
}
