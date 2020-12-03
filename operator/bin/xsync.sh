#!/bin/sh
# 获取输入参数个数，如果没有参数，直接退出
pcount=$#
if((pcount==0)); then
        echo no args...;
        exit;
fi

# 获取文件名称
p1=$1
fname=`basename $p1`
echo fname=$fname
# 获取上级目录到绝对路径
pdir=`cd -P $(dirname $p1); pwd`
echo pdir=$pdir
# 获取当前用户名称
user=`whoami`
# 循环:发送文件到slave1,slave2
for((host=1; host<=2; host++));
do
   echo $pdir/$fname $user@slave$host:$pdir
   echo ==================slave$host==================
   rsync -rvl $pdir/$fname $user@slave$host:$pdir
done
#Note:这里的slave对应自己主机名，需要做相应修改。另外，for循环中的host的边界值由自己的主机编号决定。
