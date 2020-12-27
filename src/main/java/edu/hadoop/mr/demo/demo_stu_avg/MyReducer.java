package edu.hadoop.mr.demo.demo_stu_avg;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReducer extends Reducer<Text,Text,Text,Text> {
    private Iterable<? extends Text> value;
    //在reduce()方法执行前执行一次（仅一次）

    @Override
    protected void setup(Context context) throws IOException, InterruptedException{
           context.write(new Text("分数段"),new Text("人数"+"\t"+"百分比"));
           }
        int totalPerson = 0;
        List<String> li = new ArrayList<String>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        /**
         * <60 list(1,1)
         */
        int i = 0;
        for (Text t: values){   //这里之前多了一个分号导致这个循环没执行
//            if(key.toString().equals("<60")){
//                //16 ++;
//                i ++;
//            } else if (key.toString().equals("60-70")){
//                //g617 ++;
//                i ++;
//            } else if (key.toString().equals("70-80")){
//                //g718 ++;
//                i ++;
//            } else if (key.toString().equals("80-90")){
//                //g819 ++;
//                i ++;
//            } else if (key.toString().equals("90-100")){
//                //g9110 ++;
//                i ++;
//            }
            i ++;
            totalPerson ++;
        }
        System.out.println(key.toString()+"_"+i);
        li.add(key.toString()+"_"+i);//输出效果 <"<60  3">
        //context.getConfiguration().get("counter");
    }
    //在reduce()方法执行之后执行一次（仅一次）

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (String s : li) {
            String l [] = s.split("_");
        context.write(new Text(l [0]), new Text(l [1]+"\t"+Double.parseDouble(l[1])/totalPerson*100+"%"));
        }
    }
}
