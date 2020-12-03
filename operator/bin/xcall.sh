#!/bin/bash
#在集群上统一调用某个指令，使用方式如：xcall.sh jps
  for i in master slave1 slave2
  do
    echo "*************$i*************"
    ssh $i $1
  done
