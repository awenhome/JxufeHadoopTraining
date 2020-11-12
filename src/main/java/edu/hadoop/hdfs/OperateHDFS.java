package edu.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.junit.Test;

import java.io.IOException;

public class OperateHDFS {
    @Test //单元测试
    /**
     * 1.将数据写入HDFS文件
     */
    public void writeToHDFS() throws IOException{
        //新建配置文件对象
        Configuration conf = new Configuration();
        //给配置文件设置HDFS文件的默认入口
//        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //通过传入的配置参数得到FileSystem
        FileSystem fs = FileSystem.get(conf);
        System.out.println("object fs is DistributedFileSystem? The answer is:"+(fs instanceof DistributedFileSystem?true:false));
        //获取HDFS上/1.txt的绝对路径，注意：一定要确保HDFS上/1.txt是存在的，否则程序会报错
        Path p = new Path("/1.txt");  //如果不设置fs.defaultFS（拷贝配置文件会自动设置），则默认会传到本地文件夹所在盘根目录下
        //FileSystem通过create()方法获得输出流（FSDataOutputStream）
        FSDataOutputStream fos = fs.create(p,true,1024);
        //通过输出流将内容写入1.txt文件
        fos.write("Hello World".getBytes());
        //关闭输出流
        fos.close();
        fs.close();
    }

    @Test
    /**
     * 2.读取HDFS文件
     */
    public void readHDFSFile() throws IOException{
        //新建配置文件对象
        Configuration conf = new Configuration();
        //给配置文件设置HDFS文件的默认入口
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //通过传入的配置参数得到FileSystem
        FileSystem fs = FileSystem.get(conf);
        //获取HDFS上/1.txt的绝对路径，注意：一定要确保HDFS上/1.txt是存在的，否则程序会报错
        Path p = new Path("/1.txt");
        //FileSystem通过open()方法获得输入流（FSDataInputStream）
        FSDataInputStream fis = fs.open(p);
        //分配1024个字节的内存给buf（分配1024个字节的缓冲区）
        byte[] buf = new byte[1024];
        int len = 0;
        //循环读取文件内容到缓冲区中，读到文件末尾结束（结束标识为-1）
        while((len=fis.read(buf))!=-1){
            //输出读取的文件内容到控制台
            System.out.print(new String(buf,0,len));
        }

        //关闭输出流
        fis.close();
        fs.close();
    }

    public void putFile() throws IOException{
        //新建配置文件对象
        Configuration conf = new Configuration();
        //给配置文件设置HDFS文件的默认入口
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        //通过传入的配置参数得到FileSystem
        FileSystem fs = FileSystem.get(conf);
        //文件上传到HDFS上的位置
        Path p = new Path("/");
        //待上传的文件1.sh在Windows系统中的绝对路劲，此处需要提前在Windows系统中D盘下新建1.sh文件，并写入"Hello Boys And Girls"
        Path p2 = new Path("file:///D:/1.sh");
        //从本地（Window系统）上传文件到HDFS
        fs.copyFromLocalFile(p2,p);
        fs.close();
    }

    public static void main(String[] args) {
       try{
           System.setProperty("HADOOP_USER_NAME", "jxufe");
           OperateHDFS operateHDFS = new OperateHDFS();
           operateHDFS.writeToHDFS();
//           operateHDFS.readHDFSFile();
//           operateHDFS.putFile();
       }catch(Exception e){
           e.printStackTrace();
       }

    }
}
