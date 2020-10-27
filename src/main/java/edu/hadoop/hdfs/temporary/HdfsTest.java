package edu.hadoop.hdfs.temporary;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

public class HdfsTest {

    public void putFile(String localFile, String destFile) throws IOException {
//        1、获取FileSystem
//                (1)获取Configuration对象，配置属于HDFS的文件系统：conf.set("fs.defaultFS","hdfs://hadoop01:9000")
        System.setProperty("HADOOP_USER_NAME", "jxufe");
        Configuration conf = new Configuration();
//        conf.addResource();
        conf.set("fs.defaultFS", "hdfs://hadoop01:9000");
//        (2)得到FileSystem对象：
        FileSystem fs = FileSystem.get(conf);
//        2、读写文件
//        (1)文件路径：Path----new Path("文件路径");
        Path localPath = new Path("file:///D:/1.txt");
        Path hdfsPath = new Path("hdfs://hadoop01:9000/1.txt");
//        (2)通过FileSystem得到文件输入输出流：FSDataOutputStream/FSDataInputStream
        FSDataInputStream fis = fs.open(localPath);
        FSDataOutputStream fos = fs.create(hdfsPath,true,1024);
//        fs.createNewFile(new Path(destFile));
//        (3)读写文件:FSDataOutputStream.write||FSDataInputStream.read(buf)||FileSystem.copyFromLocalFile(本地Path,HDFS的Path)
        //1
        fos.write("Hello World".getBytes());
        //2
        byte[] buf = new byte[1024];
        int len = fis.read(new byte[1024]);
        String str= new String(buf,0,len);
        //3
//        FSDataOutputStream output = fs.create(new Path(destFile),true);
        fs.copyFromLocalFile(false, new Path(localFile), new Path(destFile));
//        IOUtils.copyBytes(fis,fos,4096,false);
        //        3、关闭文件输入输出流：FSDataOutputStream.close()/FSDataInputStream.close()
        fis.close();
        fos.close();
//        4、关闭文件系统：
        fs.close();

    }

    public void getFile(String localFile, String destFile) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "jxufe");
        Configuration conf = new Configuration();
//        conf.addResource();
        conf.set("fs.defaultFS", "hdfs://hadoop01:9000");
//        (2)得到FileSystem对象：
        FileSystem fs = FileSystem.get(conf);
        fs.copyToLocalFile(new Path(destFile), new Path(localFile));
        fs.close();
    }

    public static void main(String[] args) {
        HdfsTest dfsTest = new HdfsTest();
        try {
            //wordcount.xlsx  ==> hdfs://hadoop01:9000/user/jxufe/wordcount.xlsx
            dfsTest.putFile("E:\\sftp\\wordcount.txt", "/wordcount.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
