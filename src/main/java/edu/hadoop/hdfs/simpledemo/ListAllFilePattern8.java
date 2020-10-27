package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * @author hadoop
 * @簡介  读取HDFS某个目录下的所有文件
 * @命令行例子 grep_in/f*
 *  */
public class ListAllFilePattern8 {
    public static void main(String[] args) throws Exception {
    	String uri = "upload/h*";
        System.setProperty("HADOOP_USER_NAME", "jxufe");
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        FileSystem fs = FileSystem.get(URI.create(uri), conf);

        Path path = new Path(uri);
        FileStatus[] status = fs.globStatus(path);
        Path[] listedPaths = FileUtil.stat2Paths(status);
        for (Path p : listedPaths) {
          System.out.println(p);
        }
    }
}
