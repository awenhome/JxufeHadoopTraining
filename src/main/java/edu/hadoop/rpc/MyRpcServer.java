package edu.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class MyRpcServer implements MyRpcIface{
    @Override
    public String doSomething(String str) {
        System.out.println("MyRpcServer doSomething");
        return "服务器已收到信息："+str;
    }

    public static void main(String[] args) throws IOException {
        RPC.Server server = new RPC
                .Builder(new Configuration())
                .setProtocol(MyRpcIface.class)
                .setInstance(new MyRpcServer())
                .setBindAddress("10.255.171.238")
                .setPort(8077)
                .build();
        server.start();
    }
}
