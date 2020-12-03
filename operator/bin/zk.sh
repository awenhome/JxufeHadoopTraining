#!/bin/bash
#zookeeper集群启动，调用方式：zk.sh start  || zk.sh status  ||zk.sh stop
#$1即使用该文件带的参数，即：start || status || stop
case $1 in
"start"){
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    ssh $i "$ZOOKEEPER_HOME/bin/zkServer.sh start"
  done
};;

"status"){
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    ssh $i "$ZOOKEEPER_HOME/bin/zkServer.sh status"
  done
};;

"stop"){
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    ssh $i "$ZOOKEEPER_HOME/bin/zkServer.sh stop"
  done
};;
  esac
