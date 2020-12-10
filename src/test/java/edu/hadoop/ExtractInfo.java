package edu.hadoop;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 从网页源码中提取需要的政府机构名称及对应的URL
 */
public class ExtractInfo {
    @Test
    /**
     * @格式参考 ***"http://www.xxrmfy.gov.cn/"***>新兴县人民法院<***
     * @单行格式案例  <option value="http://www.xxrmfy.gov.cn/">新兴县人民法院</option>
     *              或<a href="http://www.gdyunan.gov.cn/gkmlpt/index" target="_blank">郁南县人民政府办公室</a>
     * @处理文件 localdata\txt\other\新兴县人民政府.txt
     *          localdata\txt\other\郁南县人民政府.txt
     */
    public void format1() {
        String fileName = "localdata\\txt\\other\\新兴县人民政府.txt";
//        String fileName = "localdata\\txt\\other\\郁南县人民政府.txt";
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;   //每行的内容
            int line=0;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                line++;
                int startPoint = str.indexOf("\"");//第一个"位置
                int endPoint = str.indexOf("\"",startPoint+1);  //第一个"位置后出现的“==即第二个"符号位置
                String url = str.substring(startPoint+1,endPoint);
//                System.out.println(/*startPoint+"\t"+endPoint+"\t"+*/url);
                int nameStartPoint = str.indexOf(">")+1;   //第一个>括号之后
                int nameEndPoint = str.indexOf("<",nameStartPoint+1);   //nameStartPoint后面的第一个<括号
                String name = str.substring(nameStartPoint,nameEndPoint);
                System.out.println(name+"\t"+url);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
