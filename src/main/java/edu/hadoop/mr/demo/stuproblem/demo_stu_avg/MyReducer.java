package edu.hadoop.mr.demo.stuproblem.demo_stu_avg;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReducer extends Reducer<Text,Text,Text,Text> {
    //在reduce方法执行前执行一次（仅一次）

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        context.write(new Text("分数段"),new Text("人数"+"\t"+"百分比"));
    }
    int totalPerson=0;
    List<String> li=new ArrayList<String>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        /**
         * <60 list(1,1)
         */
        int i=0;
//        Iterable<? extends Text> value = null;
        for(Text t : values){
            if(key.toString().equals("<60")){
                //16 ++;
                i++;
            }
            else if(key.toString().equals("60-70")){
                //g617 ++;
                i++;
            }else if(key.toString().equals("70-80")){
                //g718 ++;
                i++;
            }else if(key.toString().equals("80-90")){
                //g819 ++;
                i++;
            }else if(key.toString().equals("90-100")){
                //g9110 ++;
                i++;
            }
            totalPerson ++;
        }
        li.add(key.toString()+"_"+i);//输出效果
        //context.getCOnfiguration().get("counter");
    }
    //在reduce()方法执行之后再执行一次（仅一次）

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (String s :li){
            String l []=s.split("_");
            context.write(new Text(l[0]),new Text(l[1]+"\t"+Double.parseDouble(l[1])/totalPerson*100+"%"));
        }
    }
}