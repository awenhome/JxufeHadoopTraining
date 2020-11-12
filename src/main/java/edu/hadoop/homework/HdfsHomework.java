package edu.hadoop.homework;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 彭文忠
 * @descirbe HDFS作业
 * @目地 带学生写程序，熟悉编程思路
 */
public class HdfsHomework {
    public static void main(String[] args) {
        try {
            //1.得到FileSystem:HDFS和Local
            Configuration conf = new Configuration();
//            conf.set("fs.defaultFS","hdfs://hadoop01:9000");
            FileSystem fsHdfs = FileSystem.get(conf);
            System.out.println(fsHdfs);
            LocalFileSystem fsLocal = FileSystem.getLocal(new Configuration(false));

            //2.文件输入输出流：IO
            //(1)用Java文件流得到IO
            Path pathInHdfs = new Path("/hello.txt");
            Path pathOutHdfs = new Path("/hello1105.txt");
            Path pathInLocal = new Path("./localdata/txt/wordcount/wordcount.txt");
            Path pathOutLocal = new Path("./outputdata/txt/wordcount/wc1105.txt");
            FileInputStream inJavaLocal = new FileInputStream(new File("./localdata/txt/wordcount/wordcount.txt"));
            //(2)FileSystem得到文件流
            FSDataInputStream inHdfs = fsHdfs.open(pathInHdfs);
            FSDataOutputStream outHdfs = fsHdfs.create(pathOutHdfs);
            FSDataInputStream inLocal = fsLocal.open(pathInLocal);
            FSDataOutputStream outLocal = fsLocal.create(pathOutLocal);

            //3.IO传输
            /**
             * Copies from one stream to another.
             * @param in InputStrem to read from
             * @param out OutputStream to write to
             * @param buffSize the size of the buffer
             * @param close whether or not close the InputStream and
             * OutputStream at the end. The streams are closed in the finally clause.
             */
            //上传
//            IOUtils.copyBytes(inLocal,outHdfs,1024,false);
            //下载
//            IOUtils.copyBytes(inHdfs,outLocal,1024,false);
            //LocalFile-->LocalFile2    HDFS1--->HDFS2
            //打印InputStream内容到控制
//            IOUtils.copyBytes(inHdfs,System.out,1024,false);
            //4.关闭IO流、关闭FileSystem
            inHdfs.close();
            inLocal.close();
            outHdfs.close();
            outLocal.close();

            //5.使用FileSystem自带的接口实现IO传输

//            fsHdfs.copyFromLocalFile(pathInLocal, pathOutHdfs);
//            fsHdfs.copyToLocalFile(pathInHdfs,pathOutLocal);
//            fsHdfs.

            //homework1
            Path rootPath = new Path("/pengwenzhong");
            boolean fileExists = fsHdfs.exists(rootPath);
            if(!fileExists){
                fsHdfs.mkdirs(rootPath);
            }
            fsHdfs.copyFromLocalFile(new Path("D:\\Awen\\idea_workspaces\\JxufeHadoopTraining\\outputdata\\txt\\inputtemperature.txt"),new Path("/pengwenzhong"));

            //问题2

            //问题3
            FileStatus[] fileStatuses = fsHdfs.listStatus(new Path("/pengwenzhong"));
            //for fileStatuses
            FileStatus oneFileStatus = fileStatuses[0];
            BlockLocation[] fileBlockLocations = fsHdfs.getFileBlockLocations(oneFileStatus, 0, oneFileStatus.getLen());
           //for fileBlockLocations
            BlockLocation blockLocation = fileBlockLocations[0];
            String[] hosts = blockLocation.getHosts();
            //for hosts:第一个就是离你最近的文件块的主机IP

            //6.关闭FileSystem
            fsHdfs.close();
            fsLocal.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
