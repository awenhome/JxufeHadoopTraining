package edu.hadoop.hdfs.simpledemo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author hadoop
 * @简介 查找某个文件在HDFS集群的位置
 * @测试  小文件在hadoop01上执行则返回host[0]=hadoop01，在奴隶机器上运行则返回host[0]=hadoop02或hadoop03
 * @指令验证 bin/hadoop fsck /user/hadoop/law_in/law.txt -files
 * @发现  文件块大小默认不是64M，而是128M
 */
public class BlockLocationTest4 {
    public static void main(String[] args) throws Exception {
        Configuration conf=new Configuration();
        conf.set("fs.defaultFS","hdfs://hadoop01:9000");
        FileSystem hdfs=FileSystem.get(conf);
        Path fpath=new Path(/*args[0]*/"/testdata/hello.txt");
        FileStatus filestatus = hdfs.getFileStatus(fpath);
        BlockLocation[] blkLocations = hdfs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
        int blockLen = blkLocations.length;
        for(int i=0;i<blockLen;i++){
            String[] hosts = blkLocations[i].getHosts();
            //其中host[0]离自己最近
            System.out.println("block_"+i+"_location hosts[0]:"+hosts[0]);
            System.out.println("block_"+i+"_location hosts[1]:"+hosts[1]);
        }
    }
}
