package edu.hadoop.hdfs.temporary;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**
 * @author 彭文忠
 * @describe Hdfs Java Api测试
 * @情景  上课带学生写代码，分析思路
 *
 */
public class HDFSApiTest {
    public static void main(String[] args) {

        try {
            //第一步：获取文件系统FileSystem
            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS","hdfs://hadoop01:9000");
            FileSystem fs = FileSystem.get(configuration);
            LocalFileSystem localFs = FileSystem.getLocal(new Configuration(false));
            System.out.println("fs:"+fs);

            //第二步骤：对文件进行操作：上传、下载、删除、新建、append、打印到控制台显示、列出状态、文件块信息等等
            //1.用FileSystem自带的方法实现上传下载
//            fs.copyToLocalFile(new Path("/hello.txt"),new Path("./outputdata/testdata/hello_download.txt");
//            fs.copyFromLocalFile(new Path("./localdata/txt/hello.txt"),new Path("/hello_create.txt"));
//           fs.delete(new Path("/hello_create.txt"),true);
//           fs.deleteOnExit(new Path("/hello_create.txt"));
            //其它参考：simpledemo中的案例

            //2.通过IOUtils来实现IO传输
            FSDataInputStream in_hdfs = fs.open(new Path("/hello.txt"));  //hdfs://hadoop01:9000/hello.txt
            FSDataInputStream in_local = localFs.open(new Path("./localdata/txt/hello.txt"));
            FSDataOutputStream out_hdfs = fs.create(new Path("/hello_create.txt"));
            FSDataOutputStream out_local = localFs.create(new Path("./outputdata/testdata/hello_download.txt"), true);
            //测试：输出文件内容
//            IOUtils.copyBytes(in_local,System.out,4096,false);
//            IOUtils.copyBytes(in_hdfs,System.out,4096,false);
            //测试：文件传输IO
            //2.1 上传：本地传hdfs
//            IOUtils.copyBytes(in_local,out_hdfs,4096,false);
//            //2.2 下载：HDFS传本地
//            IOUtils.copyBytes(in_hdfs,out_local,4096,false);
//            //2.3 本地传本地
//            IOUtils.copyBytes(in_local,out_local,4096,false);
//            //2.4 hdfs传hdfs
//            IOUtils.copyBytes(in_hdfs,out_hdfs,4096,false);

            //关闭IO 流
            in_hdfs.close();
            in_local.close();
            out_hdfs.close();
            out_local.close();

            //关闭FileSystem
            localFs.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
