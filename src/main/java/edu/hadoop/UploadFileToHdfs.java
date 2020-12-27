package edu.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;

/**
 * @author 彭文忠
 * @简介 写一个方法，方便把本地文件上傳到HDFS上，默认回上传到HDFS的 /inputdata 目录下
 *      操作步骤：localSrc传入你要上传的文件路径即可（可以是单个文件、也可以是文件夹（支持文件夹嵌套））
 *              dst目标父文件夹：将localSrc文件或者文件夹会自动上传到dst目录下
 */
public class UploadFileToHdfs {
    public static void main(String[] args) {
        try {
            String localSrc = "localdata\\txt\\ml-1m";
            String dst = "/inputdata/";
            if (args.length >= 2) {
                localSrc = args[0];
                dst = args[1];
            }
            System.setProperty("HADOOP_USER_NAME", "jxufe");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://hadoop01:9000");
            FileSystem hdfsFs = FileSystem.get(conf);
            hdfsFs.mkdirs(new Path(dst));  //目标目录如果不存在，则自动创建
            hdfsFs.copyFromLocalFile(new Path(localSrc),new Path(dst));
            System.out.println("文件上传完成，HDFS上路径为："+dst+localSrc.substring(localSrc.lastIndexOf("\\")+1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
