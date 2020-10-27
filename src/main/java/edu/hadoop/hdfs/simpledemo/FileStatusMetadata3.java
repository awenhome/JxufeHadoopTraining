package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.sql.Timestamp;

public class FileStatusMetadata3 {
    public static void main(String[] args) throws Exception {
        //读取hadoop文件系统的配置
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //如果Hadoop用户标识不同于客户机上的用户账号，可以通过hadoop.job.ugi属性来显式设定Hadoop用户名和组名
//        conf.set("hadoop.job.ugi", "jxufe,jxufe");
        //实验1:查看HDFS中某文件的元信息
        System.out.println("实验1:查看HDFS中某文件的元信息");
//        String fileUri = args[0];
        System.setProperty("HADOOP_USER_NAME", "jxufe");
        String fileUri = "/testdata/hello.txt";
        FileSystem fileFS = FileSystem.get(URI.create(fileUri) ,conf);
        FileStatus fileStatus = fileFS.getFileStatus(new Path(fileUri));
        //获取这个文件的基本信息
        if(fileStatus.isDirectory()==false){
            System.out.println("这是个文件");
        }
        System.out.println("文件路径: "+fileStatus.getPath());
        System.out.println("文件长度: "+fileStatus.getLen());
        System.out.println("文件修改日期： "+new Timestamp (fileStatus.getModificationTime()).toString());
        System.out.println("文件上次访问日期： "+new Timestamp(fileStatus.getAccessTime()).toString());
        System.out.println("文件备份数： "+fileStatus.getReplication());//结果是3.但配置文件设置的副本数是2
        System.out.println("文件的块大小： "+fileStatus.getBlockSize());
        System.out.println("文件所有者：  "+fileStatus.getOwner());
        System.out.println("文件所在的分组： "+fileStatus.getGroup());
        System.out.println("文件的 权限： "+fileStatus.getPermission().toString());
        System.out.println();

        //实验2:查看HDFS中某文件的元信息
        System.out.println("实验2:查看HDFS中某目录的元信息");
        String dirUri = /*args[1]*/"/testdata";
        FileSystem dirFS = FileSystem.get(URI.create(dirUri) ,conf);
        FileStatus dirStatus = dirFS.getFileStatus(new Path(dirUri));
        //获取这个目录的基本信息
        if(dirStatus.isDir()==true){
            System.out.println("这是个目录");
        }
        System.out.println("目录路径: "+dirStatus.getPath());
        System.out.println("目录长度: "+dirStatus.getLen());
        System.out.println("目录修改日期： "+new Timestamp (dirStatus.getModificationTime()).toString());
        System.out.println("目录上次访问日期： "+new Timestamp(dirStatus.getAccessTime()).toString());
        System.out.println("目录备份数： "+dirStatus.getReplication());
        System.out.println("目录的块大小： "+dirStatus.getBlockSize());

        System.out.println("目录所有者：  "+dirStatus.getOwner());
        System.out.println("目录所在的分组： "+dirStatus.getGroup());
        System.out.println("目录的 权限： "+dirStatus.getPermission().toString());
        System.out.println("这个目录下包含以下文件或目录：");
        FileStatus[] fsList = dirFS.listStatus(new Path(dirUri));
        for(FileStatus fs : fsList){
            System.out.println(fs.getPath());

        }
    }
}
