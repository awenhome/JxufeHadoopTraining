package edu.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HdfsTest2019 {
	/*
	 * * FileSystem:这个对象是hdfs抽象目录树的一个实例 获取对象实例的方式：
	 *  1、new Object 2、反射 3、工厂类  4、单例设计模式，静态方法 5、克隆
	 */

	/**
	 * 没有上传文件的权限： 2种设置方法：
	 * 1、代码提交的时候设置 右键>>run as >> run configurations program
	 * arguments:程序运行中需要的参数 VM arguments:JVM运行时需要的参数 -DHADOOP_USER_NAME=jxufe
	 * 2、写在代码中 FileSystem fs = FileSystem.get(new URI("hdfs://master:9000"), conf,
	 * "jxufe"); 或者 System.setProperty("HADOOP_USER_NAME", "jxufe");
	 * 3.修改hdfs文件权限，所有文件对所有用户均具有可读可写可执行权限：   hdfs dfs -chomd -R 777 /
	 */

//	System.setProperty("HADOOP_USER_NAME", "jxufe");

	/**
	 * 从哪里获取的配置文件？
	 * 通过代码上传的文件的默认备份是3，我们设置的副本数是2。conf对象用于读取配置文件：
	 * core-default.xml、hdfs-default.xml、mapred-default.xml、yarn-default.xml
	 * 默认情况下，配置对象加载的配置文件来自于jar包中,加载顺序为：
	 * （1）、jar包 （2）、工程classpath(src)下的配置文件（源码IDEA的src/main/resources或Eclipse的src根目录下、java的classpath下）
	 * 			只识别2种名字：hdfs-default.xml hdfs-site.xml
	 * （3）、可以通过代码单独设置配置项，如： conf.set("dfs.replication","5");
	 * 注意：三种方式生效的优先级： 最后加载的为最终生效的
	 */

	/**
	 * 获取的抽象目录树是分布式文件系统的抽象目录树：
	 * 			根目录 / 它的全路径是：hdfs://hadoop01:9000/
	 * 			当前目录 .  它的全路劲是:	hdfs://hadoop01:9000/user/jxufe(你的用户名)
	 * 本地文件,如   file:\\\D:\1.sh
	 */

	/**
	 * 文件上传/下载的时候会生成一个crc文件 crc文件用于校验下载的文件与上传的文件是否是同一个文件，用于校验文件的完整性,通过起始偏移量和结尾偏移量校验
	 * 真实存储文件的目录： ......./data current:存储数据
	 * 有一个块池的目录，与namenode下VERSION中的blockpoolID对应 每一个文件在上传到hdfs中时会生成2个文件： 原始文件
	 * blk_块id .meta 原始文件的元数据信息 用于记录原始文件的长度、创建时间、偏移量等信息。
	 * in_use.lock:锁文件
	 * 用于标识DataNode进程，一个节点上只允许开启一个DataNode进程
	 */

	public static void main(String[] args) {
		/**
		 * 要操作文件，需要获取到FileSystem对象：
		 * 1、new
		 * 2、反射
		 * 3、工厂类
		 * 4、单例设计模式，静态方法
		 * 5、克隆
		 */

		try {
			System.setProperty("HADOOP_USER_NAME", "jxufe");

			Configuration conf = new Configuration();   //默认会加载配置文件
//			conf.addResource("");
//			conf.set("dfs.replication", "5");
			//org.apache.hadoop.fs.LocalFileSystem@5bd03f44 本地文件系统	windows?linux?
//			FileSystem fs = FileSystem.get(conf);	//获取本地文件系统
			FileSystem fileSystem =  FileSystem.get(conf);
//			FileSystem fileSystem =  FileSystem.get(new URI("hdfs://master:9000"), conf,"jxufe");
			System.out.println("默认会加载配置文件，fileSystem是否为DistributedFileSystem实例？"+ (fileSystem instanceof DistributedFileSystem));
			System.out.println("默认会加载配置文件，fileSystem对象信息："+fileSystem);
			FileSystem localfs = FileSystem.get(new Configuration(false));
			FileSystem localfs2 = FileSystem.getLocal(conf);
			System.out.println("默认不会加载配置文件，localfs2对象信息："+localfs2);


			//Path hdfs内置对象 文件路径对象
//			Path src = new Path("D:\\wordcount.txt");
			Path src = new Path("./localdata/txt/wordcount/wordcount.txt");
			Path dst = new Path("/wordcount3.txt");	//设想这就是hdfs的根目录	事实上：D:\eclipse-workspace\master		单机模式
			//假设path为/test：上传时如果test是一个已存在的目录，则上传文件会传到该目录下，如果不存在则会以test做为文件名称
			fileSystem.copyFromLocalFile(false,true,src, dst);
//			Path src2 = new Path("/wordcount.txt");
//			Path dst2 = new Path("D:\\wordcountdownload.txt");
//			fileSystem.copyToLocalFile(src2, dst2);


			// 创建文件夹
			Path path = new Path("/apitest");
			// 文件是否存在
			boolean b = fileSystem.exists(path);
			if (!b) {
				fileSystem.mkdirs(path);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
