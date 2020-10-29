package edu.hadoop.rpc;

public interface MyRpcIface {
    long versionID = 1;//该字段必须要有，不然会报java.lang.NoSuchFieldException: versionID异常
    public String doSomething(String str);
}
