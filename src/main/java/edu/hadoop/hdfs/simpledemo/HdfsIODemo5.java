package edu.hadoop.hdfs.simpledemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.URL;

@SuppressWarnings("deprecation")
public class HdfsIODemo5 {
    static final Logger logger = Logger.getLogger(HdfsIODemo5.class);
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }
    /**
     * @命令行参数示例：
     *   从本地拷贝到服务器：
     *      Linux下测试：create /home/jxufe/hadooplocaldata/hello.txt /hello.txt
     *      Window下测试:create E:/sftp/hadooplocaldata/hello.txt /hello.txt
     *       绝对路劲文件:create ./localdata/txt/inputtemperature.txt /inputtemperature.txt
     *       相对路劲文件：
     *   从服务器写到本地：
     *      Linux下测试：get /hello.txt /home/jxufe/hello_download.txt   true
     *      Window下测试：get /hello.txt E:/sftp/hadooplocaldata/hello_download.txt  true
     *      get /inputtemperature.txt ./outputdata/txt/inputtemperature.txt   true
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

    	HdfsIODemo5 sample = new HdfsIODemo5();
    	//参数设置默认值
        String operateType = "create";
        String localPath = "./localdata/txt/hello.txt";
        String hdfsPath = "/hello1029test.txt";   //hdfs://hadoop01:9000/hello.txt
        boolean print = true;
        if(args.length >=3) {
    	    operateType = args[0];
    	    localPath = args[1];
    	    hdfsPath = args[2];
        }
        if (operateType.equals("create")) {
            sample.createFile(localPath, hdfsPath);
        } else if (operateType.equals("get")) {
            if(args.length >=4) {
                print = Boolean.parseBoolean(args[3]);
            }
            sample.getFile(localPath, hdfsPath, print);
        }
    }
    /**
     * 创建文件 :1.在hdfs上创建该文件（内容为空）；2.将本地文件内容用流形式写入到hdfs该文件中
     * @param localPath   //本地文件路劲
     * @param hdfsPath    //hdfs目标文件路劲
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public void createFile(String localPath, String hdfsPath) throws IOException {
        InputStream in = null;
        try {
            System.setProperty("HADOOP_USER_NAME", "jxufe");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS","hdfs://hadoop01:9000");
            //如果Hadoop用户标识不同于客户机上的用户账号，可以通过hadoop.job.ugi属性来显式设定Hadoop用户名和组名
//            conf.set("hadoop.job.ugi", "jxufe,jxufe");
            FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
            FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));

//            Configuration confLocal = new Configuration(false);
//            confLocal.addResource("core-site2_local.xml");
//            LocalFileSystem localFileSystem= FileSystem.getLocal(confLocal);
//            in =localFileSystem.open(new Path(localPath));
            in = new BufferedInputStream(new FileInputStream(new File(localPath)));
            IOUtils.copyBytes(in, out, 4096, false);
            out.hsync();
            out.close();
            logger.info("create file in hdfs:" + hdfsPath);
        } finally {
            IOUtils.closeStream(in);
            System.out.println("上传文件到HDFS成功,HDFS保存位置为："+hdfsPath);
        }
    }
    /**
     * 从HDFS获取文件，并拷贝到本地localPath
     * @param localPath
     * @param hdfsPath
     * @param print 是否将文件内容同时打印到控制台
     * @throws IOException
     */
    public void getFile( String hdfsPath,String localPath, boolean print) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "jxufe");
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");   //有配置文件时可以不用设置
        //如果Hadoop用户标识不同于客户机上的用户账号，可以通过hadoop.job.ugi属性来显式设定Hadoop用户名和组名
//        conf.set("hadoop.job.ugi", "jxufe,jxufe");
        FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
//        fileSystem.setPermission(null, null);
        FSDataInputStream in = null;
        OutputStream out = null;
        try {
            in = fileSystem.open(new Path(hdfsPath));
            File file = new File(localPath);
            out = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copyBytes(in, out, 4096, !print);  //如果需要打印即print=ture，那么close=false。最后不关闭文件流
            logger.info("get file form hdfs to local: " + hdfsPath + ", " + localPath);
            System.out.println("从HDFS下载文件成功,本地保存位置为："+localPath);
            if (print) {
                in.seek(0);  //输入流从新回到文件开始的位置
                IOUtils.copyBytes(in, System.out, 4096, true);
            }
        } finally {
            IOUtils.closeStream(out);

        }
    }
}
