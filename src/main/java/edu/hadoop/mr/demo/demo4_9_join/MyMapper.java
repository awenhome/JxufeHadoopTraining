package edu.hadoop.mr.demo.demo4_9_join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class MyMapper extends Mapper<Object,Text,Text,Text> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);    //这行要注释掉，否则报错Type mismatch in key from map: expected org.apache.hadoop.io.Text, received org.apache.hadoop.io.LongWritable
        //1.以行为单位，对数据进行处理
        String line = value.toString();
        //2.切分一行文本
        StringTokenizer words = new StringTokenizer(line);
        int i = 0;
        String tempKey = new String();
        String tempValue = new String();
        String filetype = new String();
        //3.迭代处理切分出的单词
        while (words.hasMoreElements()){
            String word = words.nextToken();
            //为两个文件name.txt和city.txt设置编号
            if (word.charAt(0)>='0' && word.charAt(0)<='9') {
                tempKey = word;
                if (i > 0) {
                    filetype = "1";
                }else {
                    filetype = "2";
                }
                continue;
            }
            tempValue += word + " ";
            i++;
        }
        //4.将输出的K-V对存入context
        context.write(new Text(tempKey),new Text(filetype + "+" + tempValue));
    }
}
