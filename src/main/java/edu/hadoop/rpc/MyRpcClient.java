package edu.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyRpcClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        //"10.255.171.238"  172.29.161.119
        MyRpcIface proxy = RPC.getProxy(MyRpcIface.class, 1, new InetSocketAddress("172.29.161.119", 8077), new Configuration());
        while(true) {
            String result = proxy.doSomething("hadoop01:File01(blk01),File03(blk02)");
            System.out.println(result);
            Thread.sleep(1*1000);
        }
//        RPC.stopProxy(proxy);
    }
}
