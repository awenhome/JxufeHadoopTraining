package edu.hadoop.mr.demo.stuproblem.demo_stu_avg;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MyMapper extends Mapper<LongWritable, Text, Text,Text> {
    Text k=new Text();
    Text v =new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //一个学生的成绩
        String line=value.toString();
        String scores []=line.split("\\s+");
        String chinese=scores[1];
        String math=scores[2];
        String english=scores[3];
        double avg=(Double.parseDouble(chinese)+Double.parseDouble(math)+Double.parseDouble(english))/(scores.length-1);
        //判断
        if(avg<60){
            k.set("<60");
            v.set("1");
        }else if(avg>=60 && avg<70){
            k.set("60-70");
            v.set("1");
        }else if(avg>=70 && avg<80) {
            k.set("70-80");
            v.set("1");
        }else if(avg>=80 && avg<90) {
            k.set("80-90");
            v.set("1");
        }else if(avg>=90 && avg<=100) {
            k.set("90-100");
            v.set("1");
        }
        //context.getConfiguration().setInt("counter",counter);
        context.write(k,v);
    }
}