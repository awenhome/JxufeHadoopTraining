package edu.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 彭文忠
 * @describe Hdfs Java Api测试
 * @情景  上课带学生写代码，分析思路
 *
 */
public class HdfsJavaApi {
    public void getFile(FileSystem fileSystem){


    }

    /**
     * @function 将本地文件上传|复制到HDFS上
     * @param fileSystem 传入DSF文件系统
     * @param localPathStr  本地文件路劲  file:///D:/1.txt   ./1.txt
     * @param hdfsPathStr    HDFS路劲
     */
    public void putFile(FileSystem fileSystem, String localPathStr, String hdfsPathStr) throws IOException {
    //方法一：调用FileSystem提供的方法copyFromLocalFile
        //        fileSystem.copyFromLocalFile(new Path(localPathStr),new Path(hdfsPathStr));
        //方法二：
        //本地文件流方法一
//        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration(false));
//        FSDataInputStream in = localFileSystem.open(new Path(localPathStr));
        //本地文件流方法二：Java方式
//        FileInputStream in = new FileInputStream(new File(localPathStr));
        FSDataInputStream in = null;
        FSDataOutputStream out = fileSystem.create(new Path(hdfsPathStr), true);
        IOUtils.copyBytes(in,out,4096,false);
//        in.seek(0);
//        IOUtils.copyBytes(in,System.out,4096,false);
        out.close();
        in.close();
    }

    /**
     * @function 删除文件
     * @param fileSystem
     * @param pathStr  hdfs://hadoop01:9000/hello.txt
     *                 file:///D:/1.txt
     */
    public void delFile(FileSystem fileSystem,String pathStr) throws IOException {
        Path path = new Path(pathStr);
        fileSystem.delete(path,true);
    }
    public void appendFile(FileSystem fileSystem){
//        fileSystem.append()
    }

    public void printHdfsFile(FileSystem fileSystem){

    }

    /**
     * @function 查看文件状态
     * @参考 simpledemo 7、8
     */
    public void listFile(){

    }
    public static void main(String[] args) {
        try {
            //1.得到FileSystem对象：注意FileSystem是本地文件系统还是DFS文件系统
            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS","hdfs://hadoop01:9000");
            FileSystem fileSystem = FileSystem.get(configuration);
//            LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration(false));
            System.out.println("fileSystem:"+fileSystem);
            //2.对文件上传、下载、修改、删除、查看等等操作
            HdfsJavaApi hdfsUtil = new HdfsJavaApi();
            hdfsUtil.putFile(fileSystem,"./localdata/txt/hello.txt","/testdata/hello1029.txt");


            fileSystem.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
