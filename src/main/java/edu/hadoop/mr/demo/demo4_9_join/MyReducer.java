package edu.hadoop.mr.demo.demo4_9_join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class MyReducer extends Reducer<Text,Text,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int cityIDFromName = 0;
        String[] name = new String[10];
        int cityID = 0;
        String[] city = new String[10];
        Iterator value = values.iterator();
        while (value.hasNext()){
            String record = value.next().toString();
            int len = record.length();
            int i =2;
            if (0 == len){
                continue;
            }
            //获取两个文件的编号
            char filetype = record.charAt(0);
            if ('1' == filetype) {
                name[cityIDFromName] = record.substring(i);
                cityIDFromName++;
            }
            if('2' == filetype) {
                city[cityID] = record.substring(i);
                cityID++;
            }
        }
        //求笛卡尔积
        if (0 !=cityIDFromName && 0 !=cityID) {
            for (int j = 0;j < cityIDFromName;j++){
                for (int k = 0; k < cityID; k++) {
                    context.write(new Text(name[j]),
                            new Text(city[k]));
                }
            }
        }
    }
}
