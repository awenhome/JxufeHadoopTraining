# Hadoop大数据处理与应用—彭文忠（alvin、pwz）

## Hadoop training for Students
  Examples for Hadoop Training 
  
## 老师项目上传的Github地址
  https://github.com/awenhome/JxufeHadoopTraining

## 课堂视频：超星云盘（定期更新）
  链接: http://pan-yz.chaoxing.com/share/info/cfb3ad99181c4de9
  
## IDEA快捷键
  参考网址：https://blog.csdn.net/qq_38963960/article/details/89552704
   ```
   常用快捷键	介绍
    Ctrl + F	在当前文件进行文本查找 （必备）
    Ctrl + R	在当前文件进行文本替换 （必备）
    Ctrl + Z	UNDO:撤销 （必备）
    Ctrl + Y	REDO:取消撤销 （必备）
    Ctrl + X	剪切光标所在行 或 剪切选择内容
    Ctrl + C	复制光标所在行 或 复制选择内容
    Ctrl + W	递进式选择代码块。可选中光标所在的单词或段落，连续按会在原有选中的基础上再扩展选中范围 （必备）
    Ctrl + E	显示最近打开的文件记录列表
    Ctrl + N	根据输入的 类名 查找类文件
    Ctrl + G	在当前文件跳转到指定行处
    Ctrl + J	插入自定义动态代码模板
    Ctrl + P	方法参数提示显示
    Ctrl + Q	光标所在的变量 / 类名 / 方法名等上面（也可以在提示补充的时候按），显示文档内容
    Ctrl + /	注释光标所在行代码，会根据当前不同文件类型使用不同的注释符号 （必备）
    Ctrl + Space	基础代码补全，默认在 Windows 系统上被输入法占用，需要进行修改，建议修改为 Ctrl + 逗号 （必备）
                    修改方法： Setting->Keymap(可以搜索)->copy->Main Menu->Code->Complete Code->在下面的base(默认快捷键Ctrl + Space),上新增一个快捷键Alt+?(应用时会提示已存在，是否删除这个快捷键默认指向的其他功能，确定即可）
                              Keymap种可以直接搜索Complete Code直接定位到位置，然后修改
    

    Alt + Insert	代码自动生成，如生成对象的 set / get 方法，构造函数，toString() 等
    Alt + 1,2,3...9	显示对应数值的选项卡，其中 7 是查看类结构(【必备】查看类中的变量和方法),其中 1 是 Project 用得最多
    
    Tab         缩进
    Shift + Tab	取消缩进
    
    Ctrl + Alt + L	格式化代码，可以对当前文件和整个包目录使用 （必备）
    Ctrl + Alt + O	优化导入的类，可以对当前文件和整个包目录使用 （必备）
    Ctrl + Alt + V	快速引进变量
    Ctrl + Alt + F7	显示使用的地方。寻找被该类或是变量被调用的地方，用弹出框的方式找出来
    Ctrl + Alt + Space	类名自动完成
    Ctrl + Alt + 左方向键	退回到上一个操作的地方 （必备）
    Ctrl + Alt + 右方向键	前进到上一个操作的地方 （必备）

    Ctrl + Shift + F	根据输入内容查找整个项目 或 指定目录内文件 （必备）
    Ctrl + Shift + C	复制当前文件磁盘路径到剪贴板
    Ctrl + Shift + I	快速查看光标所在的方法 或 类的定义
    Ctrl + Shift + A	查找动作 / 设置
    Ctrl + Shift + /	代码块注释 （必备）

    连按两次Shift||CTRL + N	弹出 Search Everywhere 弹出层
   ```

## 课程相关资料
   ```
    官网首页：http://hadoop.apache.org
   -以Hadoop2.7.7为例：
        官网文档：http://hadoop.apache.org/docs/r2.7.7/
        官网API：http://hadoop.apache.org/docs/r2.7.7/api/index.html
   -IDEA中hadoop插件:https://github.com/fangyuzhong2016/HadoopIntellijPlugin
   ```

## Hadoop推荐自学视频
   ```
    Hadoop大数据开发实战书籍配套视频（人邮网站注册用户观看）：https://www.rymooc.com/course/show/604
    尚硅谷Hadoop教程(hadoop框架精讲)(Eclipse环境)：https://www.bilibili.com/video/BV1cW411r7c5?from=search&seid=18294227332956442829
    大数据自学教程Hadoop从零到精通完整版(IDEA环境)：https://www.bilibili.com/video/BV1ek4y117Yq   
    大数据开发Hadoop全集视频教程（光环大数据）：https://study.163.com/course/courseMain.htm?courseId=1208888825
   ```
## Hadoop/Spark开发环境搭建
```
 -windows下环境搭建视频：
    链接: http://pan-yz.chaoxing.com/share/info/c3b6fe27c5f468da
 -linux下环境搭建视频：
    链接: http://pan-yz.chaoxing.com/share/info/a1251cc3e6d8eed2
 -为防止被利用Hadoop集群进行挖掘，建议修改以下yarn的端口，参考：
    https://blog.csdn.net/yz972641975/article/details/102835910
```
 
   ### 使用Hadoop RPC通信机制步骤
   ```
    1.定义RPC服务接口类（public interface 接口类）：RPC定义了服务器端对外提供的服务接口；
        注意必须要有字段：long versionID
    2.实现RPC服务接口：Hadoop的RPC服务接口通常是Java接口，用户需要实现该接口；
        2.1.服务类实现（implements）1中定义的接口类；
    3.构造和启动RPC Server：使用Builder类构造RPC Server，并调用start()方法启动RPC Server;
        3.1. new RPC().Builder(conf)......build();得到服务类对象
        3.2 服务类对象启动： RPC.Server.start()
    4.RPC Client发送请求给RPC Server：RPC Client调用线程发起RPC连接请求，等待RPC Server响应后，向其传输数据。
        4.1 通过RPC.getProxy得到接口实例，调用接口中定义好的方法，实现远程RPC与服务器交互。
   ```
   ### Hadoop HDFS的编程套路
   ```
       1、获取FileSystem
            (1)获取Configuration对象，配置属于HDFS的文件系统：conf.set("fs.defaultFS","hdfs://hadoop01:9000")
                System.setProperty("HADOOP_USER_NAME", "jxufe");
                Configuration conf = new Configuration();
                conf.set("fs.defaultFS","hdfs://hadoop01:9000");
            配置文件拷贝放在：src/main/resources下自动生效
            (2)得到FileSystem对象：FileSystem.get(conf);
        重难点：1.FileSystem.get(Configuration conf)返回的对象是本地文件系统还是分布式文件系统？==>取决于Configuration对象的配置
               2.FileSystem.getLocal(Configuration conf)  如果conf中配置了fs.defaultFS为hdfs，此时该方法返回的还是LocalFileSystem
       2、读写文件
            (1)文件路径：Path----new Path("文件路径");
            (2)通过FileSystem得到文件输入输出流：FSDataOutputStream/FSDataInputStream
            (3)读写文件:FileSystem.copyFromLocalFile(本地Path,HDFS的Path)||IOUtils.copyBytes||FSDataOutputStream.write||FSDataInputStream.read(buf)
       3、关闭文件输入输出流：FSDataOutputStream.close()/FSDataInputStream.close()
       4、关闭文件系统：FileSystem.close()  
常见异常解决：
    (1)针对提示没有权限的错误：
    hdfs dfs -chmod -R 777 /
   ```
   ### hadoop jar命令提交任务
   ```
    1.生成jar包
        1.1 打开maven界面，点击package后在项目的target下面会生成本项目Jar文件，名称如：JxufeHadoopTraining-1.0-SNAPSHOT.jar
        1.2 jar文件上传到linux机器中
    2.命令提交格式：
        hadoop jar jar文件（绝对或相对路劲） 类名（含有包路劲的完整类名称） program_args（main参数）
        上传：hadoop jar JxufeHadoopTraining-1.0-SNAPSHOT.jar edu.hadoop.hdfs.simpledemo.HdfsIODemo5 create ./hello.txt /hello.txt
        下载：hadoop jar JxufeHadoopTraining-1.0-SNAPSHOT.jar edu.hadoop.hdfs.simpledemo.HdfsIODemo5 get  /hello.txt ./hello_download.txt ture
           注意：./hello.txt这个是一个本地文件路劲，是相对jar文件的相对路劲。这里可以写绝对路劲、如果是window下测试，需要用window下路劲
   ```
   ### MapReduce执行流程简介
   ```
    执行顺序大致如下：
        1.Map Task读文件{TextInputFormat(-->RecordReader-->read())}一次读取一行，返回（Key，Value）
        2.Map执行：将上一步获取的(key，value)键值对经过Mapper的map方法逻辑处理成新的(k,v)键值对，通过context.write输出到OutputCollector收集器
    Shuffle开始:
        3.OutputCollector把收集到的（k,v）键值对写入到环形缓冲区中，环形缓冲区默认大小为100M，只写80%（阈值）。缓冲区达到80%开始溢写文件，触发spill溢写操作；
           3.1.分区Partitioner；
           3.2.排序Sort(先按分区排，再按key排序)；
           3.3.Combiner在Map端进行局部Value合并
        PS：理解顺序为什么是Partitioner-->Sort-->Combiner
        4.Spill溢出多个文件合并Merge（采用归并排序进行合并，合并后还是有序）
        5.到Reduce端再进行Merge（采用归并排序进行合并，合并后还是有序），合并成大文件
    Shuffle结束
        6.Reduce执行
        7.最后通过OutputFormat方法将结果数据写出到输出文件夹中
        注：环形缓冲区的大小可以通过在mapred-site.xml中设置mapreduce.task.io.sort.mb的值来改变，默认是100M。Map端溢出的时候会先调用Combiner组件，逻辑
           和Reduce是一样的，合并，相同的key对应的value值相加，这样传送效率高，不用一下子传好多相同的key，在数据量非常大的时候，这样的优化可以节省很多网络宽带和
           本地磁盘IO流的读写。

    MapReduce作业运行流程主要包含两个阶段：Map阶段、Reduce阶段。
        （1）Map阶段：FileInputFormat => InputSplit => RecordReader => Mapper => Partition => Sort => Combiner。
        （2）Reduce阶段：Reducer => FileOutputFormat => RecordWriter。
   ```

   ### Hadoop错误参考
   ```
    解决Exception: org.apache.hadoop.io.nativeio.NativeIO$Windows.access0(Ljava/lang/String;I)Z 等一系列问题
    https://blog.csdn.net/congcong68/article/details/42043093
   ```
##Hive部分
   ### MYSQL安装（作为HIVE元数据的数据库）
   ```
    MYSQL相关软件下载：http://pan.jxufe.cn:80/link/5F9950DF4ECEFD5D5E2A692236C8F919 有效期限：2025-04-01
    MYSQL安装（Windows、Linux)+Navicat安装视频：
        链接: http://pan-yz.chaoxing.com/share/info/cc122dca21cef516
    Linux安装文字版本，参考：operator\mysql\install_mysql_in_linux.txt   
   ```
   ### HIVE安装配置
   ```
    I.Hive下载方式：    
        1.官网下载：http://hive.apache.org/downloads.html
            建议下载2.X最后稳定版：https://mirror.bit.edu.cn/apache/hive/hive-2.3.7/apache-hive-2.3.7-bin.tar.gz
        2.老师云盘下载（推荐）
    II.根据文档operator\mysql\1.install_mysql_in_linux.txt完成MYSQL安装；
    III.根据文档：operator\hive_operator\2.Inithive(initSchema).txt完成Hive安装和配置；
    IV.Hive使用及功能测试：
   ```
   ### HIVE HQL相关语法
   ```
    创建数据库表语法规则：
        CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name 
          [(col_name data_type [COMMENT col_comment], ...)] 
          [COMMENT table_comment] 
          [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)] 
          [CLUSTERED BY (col_name, col_name, ...) 
          [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS] 
          [ROW FORMAT row_format] 
          [STORED AS file_format] 
          [LOCATION hdfs_path]
    创建数据库表语法说明：
        CREATE TABLE 创建一个指定名字的表。如果相同名字的表已经存在，则抛出异常；用户可以用 IF NOT EXIST 选项来忽略这个异常。
        EXTERNAL 关键字可以让用户创建一个外部表，在建表的同时指定一个指向实际数据的路径（LOCATION），
        有分区的表可以在创建的时候使用 PARTITIONED BY 语句。一个表可以拥有一个或者多个分区，每一个分区单独存在一个目录下。
        表和分区都可以对某个列进行 CLUSTERED BY 操作，将若干个列放入一个桶（bucket）中。
        可以利用SORT BY 对数据进行排序。这样可以为特定应用提高性能。
        默认的字段分隔符为ascii码的控制符\001(^A)
                tab分隔符为 \t。
        注意：Hive默认只支持单个字符的分隔符。https://blog.csdn.net/fighting_one_piece/article/details/37610085
        如果文件数据是纯文本，可以使用 STORED AS  
                TEXTFILE。如果数据需要压缩，使用 STORED 
                AS SEQUENCE 。
    
    加载数据语法：
        LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]
    加载数据语法说明：
        1.Load 操作只是单纯的复制/移动操作，将数据文件移动到 Hive 表对应的位置。如果表中存在分区，则必须指定分区名。
        2.加载本地数据，指定LOCAL关键字，即本地，可以同时给定分区信息 。
            2.1load 命令会去查找本地文件系统中的 filepath。如果发现是相对路径，则路径会被解释为相对于当前用户的当前路径。用户也可以为本地文件指定一个完整的 URI，比如：file:///user/hive/project/data1.
            2.2例如：加载本地数据，同时给定分区信息：
                hive> LOAD DATA LOCAL INPATH 'file:///examples/files/kv2.txt' OVERWRITE INTO TABLE invites PARTITION (ds='2008-08-15');
        3.加载DFS数据 ，同时给定分区信息：
            3.1 如果 filepath 可以是相对路径 URI路径，对于相对路径，Hive 会使用在 hadoop 配置文件中定义的 fs.defaultFS  指定的Namenode 的 URI来自动拼接完整路径。
            3.2 例如：加载数据到hdfs中，同时给定分区信息
              hive> LOAD DATA INPATH '/user/myname/kv2.txt' OVERWRITE INTO TABLE invites            
               PARTITION (ds='2008-08-15');
        4.OVERWRITE
            4.1指定 OVERWRITE ,目标表（或者分区）中的内容（如果有）会被删除，然后再将 filepath 指向的文件/目录中的内容添加到表/分区中。如果目标表（分区）已经有一个文件，并且文件名和 filepath 中的文件名冲突，那么现有的文件会被新文件所替代。
    分区分桶测试参考文件：operator\hive_operator\hive_table_with_partitioned&bucket.txt
   ```
   ### HIVE 实现wordcount案例
   ```
        创建表：drop table wordcount;
        create table wordcount(line string);   //默认是空格切分列，换行切分行
        导入数据（不加local即默认从hdfs上导入文件,且默认是剪切过去而不是拷贝)：
        load data inpath '/wc_in/word.txt' into table wordcount;
        查询：
        select a.word word,count(*) count from (select explode(split(line,' ')) as word from wordcount) a group by a.word order by count desc limit 6;
   ```
   ### HIVE自定义函数
   ```
    1.自定义函数分类：
        UDF(User-Defined-Function)用户自定义函数，输入一个数据然后产生一个数据（自带的如concat等）；
        UDAF(User-Defined Aggregation Function)用户自定义聚合函数，多个输入数据然后产生一个输出参数(自带的如max、min、avg、count等)；
        UDTF(User-Defined Table-generating Function)用户自定义表生成函数，输入一行数据生成N行数据(自带的如explode等)
    2.自定义UDF函数流程：参考operator\hive_operator\hive_udf_test.txt
        要想在Hive中完成自定义UDF函数的操作，要按照如下的流程进行操作：
        1、自定义Java类并继承org.apache.hadoop.hive.ql.exec.UDF；
        2、写evaluate函数[注意：evaluate()函数在父类UDF中是没有的]，evaluate函数支持重载；
        3、把程序打包放到hive所在服务器；
        4、进入hive客户端，添加jar包；
        5、创建关联到Java类的Hive函数；
        6、Hive命令行中执行查询语句：select 方法名(属性) from 表名——得出自定义函数输出的结果。
   ```
   ### 自然语言处理HANLP
   ```
    GitHub网址：https://github.com/hankcs/HanLP/tree/1.x
    
   ```