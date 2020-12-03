# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

# User specific aliases and functions
export JAVA_HOME=/home/jxufe/hadoop/jdk1.8.0_121
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export HADOOP_HOME=/home/jxufe/hadoop/hadoop-2.7.3
export PATH=$HADOOP_HOME/sbin:$HADOOP_HOME/bin:$PATH
export HADOOP_MAPRED_HOME=${HADOOP_HOME}
export HADOOP_COMMON_HOME=${HADOOP_HOME}
export HADOOP_HDFS_HOME=${HADOOP_HOME}
export YARN_HOME=${HADOOP_HOME}
export HADOOP_COMMON_LIB_NATIVE_DIR=${HADOOP_HOME}/lib/natvie
export PATH=$HADOOP_COMMON_LIB_NATIVE_DIR:$PATH
# 运行Spark-shell，解决Unable to load native-hadoop library for your platform
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native/:$LD_LIBRARY_PATH
export HADOOP_OPTS="-Djava.library.path=${HADOOP_HOME}/lib:${HADOOP_HOME}/lib/native"

export HIVE_HOME=/home/jxufe/software/hive/hive-2.1.1
export PATH=$PATH:$HIVE_HOME/bin
export SQOOP_HOME=/home/jxufe/software/sqoop/sqoop-1.4.6.bin__hadoop-2.0.4-alpha
export PATH=$PATH:$SQOOP_HOME/bin
export SCALA_HOME=/home/jxufe/software/scala/scala-2.12.10
export PATH=$PATH:$SCALA_HOME/bin
export ZOOKEEPER_HOME=/home/jxufe/software/zookeeper/zookeeper-3.4.6
export PATH=$PATH:$ZOOKEEPER_HOME/bin
export HBASE_HOME=/home/jxufe/software/hbase/hbase-1.3.0
export PATH=$PATH:$HBASE_HOME/bin
#spark
export SPARK_HOME=/home/jxufe/spark/spark-2.4.5-bin-without-hadoop
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
export SBT_HOME=/home/jxufe/software/sbt
export PATH=$PATH:$SBT_HOME
export KAFKA_HOME=/home/jxufe/software/kafka/kafka_2.11-2.2.0
export PATH=$PATH:$KAFKA_HOME/bin
export E2_HOME=/home/jxufe/software/maven/apache-maven-3.6.3
export PATH=$PATH:$E2_HOME/bin


#在idea中输入中文问题
export XIM="ibus"
export XIM_PROGRAM="ibus"
export XMODIFIERS="@im=ibus"
export GTK_IM_MODULE="ibus"
export QT_IM_MODULE="ibus"
