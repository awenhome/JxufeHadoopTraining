#!/bin/bash
#kafka集群启动命令，调用方式：kk.sh start 或者 kk.sh stop

#$1即取到使用该命令后的第一个参数，即：start 或者 stop
#下面的命令中主要将路径改为本地电脑安装kafka的路劲
case $1 in
"start"){
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    #ssh $i "/home/jxufe/software/kafka/kafka_2.12-2.5.0/bin/kafka-server-start.sh -daemon /home/jxufe/software/kafka/kafka_2.12-2.5.0/config/server.properties"
    #如果在/etc/profile或者~/.bashrc中配置了KAFKA_HOME路径，则可以用$KAFKA_HOME;方便以后版本升级
    ssh $i "$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties"
  done
};;

"stop"){
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    #ssh $i "/home/jxufe/software/kafka/kafka_2.12-2.5.0/bin/kafka-server-stop.sh"
    ssh $i "$KAFKA_HOME/bin/kafka-server-stop.sh"
  done
};;
  esac
