package edu.hadoop.hive.udaf.backup;
import org.apache.hadoop.hive.ql.exec.UDF;
public final class AddUDF extends UDF {
   public Integer evaluate(Integer a, Integer b) {
     if (null == a || null == b) {
        return null;
     }
     return a + b;
   }
   public Double evaluate(Double a, Double b) {
     if (a == null || b == null)
        return null;
     return a + b;
   }
   public Integer evaluate(Integer... a) {
     int total = 0;
     for (int i = 0; i < a.length; i++)
        if (a[i] != null)
          total += a[i];
     return total;
   }
}
