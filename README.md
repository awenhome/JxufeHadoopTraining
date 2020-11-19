# Hadoop大数据处理与应用—彭文忠（alvin、pwz）

## Hadoop training for Students
  Examples for Hadoop Training 
  
## 老师项目上传的Github地址
  https://github.com/awenhome/JxufeHadoopTraining

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
   ```