package edu.hadoop.hive.udaf;
import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
public class AvgUDAF extends UDAF {
   public static class AvgState {
     private long mCount;
     private double mSum;
   }
   public static class AvgEvaluator implements UDAFEvaluator {
     AvgState state;
     public AvgEvaluator() {
        super();
        state = new AvgState();
        init();
     }
     /**
      * init函数类似于构造函数，用于UDAF的初始化
      */
     public void init() {
        state.mSum = 0;
        state.mCount = 0;
     }
     /**
      * iterate接收传入的参数，并进行内部的轮转。其返回类型为boolean
      * 
      * @param o
      * @return
      */
     public boolean iterate(Double o) {
        if (o != null) {
          state.mSum += o;
          state.mCount++;
        }
        return true;
     }
     /**
      * terminatePartial无参数，其为iterate函数轮转结束后，返回轮转数据，
      * terminatePartial类似于hadoop的Combiner
      * 
      * @return
      */
     public AvgState terminatePartial() {// combiner
        return state.mCount == 0 ? null : state;
     }
     /**
      * merge接收terminatePartial的返回结果，进行数据merge操作，其返回类型为boolean
      * 
      * @param o
      * @return
      */
     public boolean merge(AvgState o) {
        if (o != null) {
          state.mCount += o.mCount;
          state.mSum += o.mSum;
        }
        return true;
     }
     /**
      * terminate返回最终的聚集函数结果
      * 
      * @return
      */
     public Double terminate() {
        return state.mCount == 0 ? null : Double.valueOf(state.mSum
             / state.mCount);
     }
   }
}
