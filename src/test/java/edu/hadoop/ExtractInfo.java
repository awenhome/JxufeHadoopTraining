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
     * @说明 本方法是万能方法，适用于所有网站形式。只需要改好前面的参数
     * @关键字 动态设置：设置有效行前缀  设置URL前面字符串   设置名称前面字符串
     * @格式参考 任意格式均可以
     * */
    public void formatCore() {
        String province = "江苏省";   //省名词
        String city = "南京市";  //市名词
        String area = "秦淮区";  //区名词
        String address = "'"+province+"-"+city+"-"+area;   //省市区组合：非公式需要输入一个'前缀
        String subWebsiteStr = area+"从属网站";   //url直接属于这个区（前缀一致)
        //1.用来拼接URL的官网字符串，如果获取到的URL不是http或www开头，则自动拼接此前缀 2.用来判断是否属于从属网址
        String urlOfficialPrefix = "http://www.njqh.gov.cn/";
        String availStartLine = "<li>";   //有效行开头（过滤无效行），如果每行都是有效行，可以为空字符串
        String urlPrefixStr = "href=\"";    //url前面的字符特征提取：可能要改
        String urlSuffixStr="\"";      //url后紧跟的字符特征：可能要改
        String namePrefixStr = "\">";   //名字前面特征提取：一般都是这个格式，一般不用改
        String nameSuffixStr = "<";     //名字后面紧跟字符特征：一般都是这个格式，一般不用改


        String fileName = "localdata\\txt\\other\\1.txt";
//        String fileName = "localdata\\txt\\other\\郁南县人民政府.txt";
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;   //每行的内容
            int line=0;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if(str.startsWith("<!--")){
                    continue;//被注释的则跳过直接处理下一行
                }
                if(availStartLine!=null&&!str.trim().startsWith(availStartLine)){  //数据格式比较乱，只取其中某种格式开头的行进行解析
                    continue;   //不是有效行，则跳过直接处理下一行
                }
                line++;
                int startPoint = str.indexOf(urlPrefixStr)+urlPrefixStr.length();//第一个"位置
                int endPoint = str.indexOf(urlSuffixStr,startPoint+1);  //第一个"位置后出现的“==即第二个"符号位置
                String url = str.substring(startPoint,endPoint);
                int nameStartPoint = str.indexOf(namePrefixStr)+namePrefixStr.length();   //第一个>括号之后
                int nameEndPoint = str.indexOf(nameSuffixStr,nameStartPoint+1);   //nameStartPoint后面的第一个<括号
                String name = str.substring(nameStartPoint,nameEndPoint);
                if(!urlOfficialPrefix.equals("")){ //如果URL有前缀需要拼接
                    if(!url.trim().startsWith("www")&&!url.trim().startsWith("http")){ //如果是www或http开头则不用拼接
                        url = urlOfficialPrefix + url;
                    }
                }
                //判断属否属于官网的从属网站：前缀一样且长度至少多两位
                if(url.trim().startsWith(urlOfficialPrefix)&&url.trim().length()-urlOfficialPrefix.length()>=2){
                    System.out.println(province+"\t"+city+"\t"+area+"\t"+address+"\t"+name.trim() + "\t" + url.trim()+"\t"+subWebsiteStr);
                }else {
                    System.out.println(province+"\t"+city+"\t"+area+"\t"+address+"\t"+name.trim() + "\t" + url.trim());
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    /**
     * @关键字 第一个出现”后是url   >后是名称
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
    @Test
    /**
     * @关键字 herf=   title=  自动过滤掉<!--开头的
     * @格式参考 ***"http://www.xxrmfy.gov.cn/"***>新兴县人民法院<***
     * @单行格式案例  <li><a target="_blank" href="http://fgw.sh.gov.cn/" title="市发展改革委" tabtarget="33" toretabtarget="361" retargeta="362">市发展改革委</a></li>
     */
    public void format2() {
        String fileName = "localdata\\txt\\other\\1.txt";
//        String fileName = "localdata\\txt\\other\\郁南县人民政府.txt";
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;   //每行的内容
            int line=0;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if(str.trim().startsWith("<!--")){
                    continue;
                }
                line++;
                int startPoint = str.indexOf("href=\"")+6;//第一个"位置
                int endPoint = str.indexOf("\"",startPoint+1);  //第一个"位置后出现的“==即第二个"符号位置
                String url = str.substring(startPoint,endPoint);
                int nameStartPoint = str.indexOf("title=\"")+7;   //第一个>括号之后
                int nameEndPoint = str.indexOf("\"",nameStartPoint+1);   //nameStartPoint后面的第一个<括号
                String name = str.substring(nameStartPoint,nameEndPoint);
                System.out.println(name+"\t"+url);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * @关键字：herf=   >***<
     * @格式参考 ***"http://www.xxrmfy.gov.cn/"***>新兴县人民法院<***
     * @单行格式案例  <li><a href="DeptList.aspx?DeptId=003001" target="_blank">区政府办公室</a></li>
     */
    public void format3() {
        String fileName = "localdata\\txt\\other\\1.txt";
//        String fileName = "localdata\\txt\\other\\郁南县人民政府.txt";
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;   //每行的内容
            int line=0;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if(str.startsWith("<!--")){
                    continue;
                }
                line++;
                int startPoint = str.indexOf("href=\"")+6;//第一个"位置
                int endPoint = str.indexOf("\"",startPoint+1);  //第一个"位置后出现的“==即第二个"符号位置
                String url = str.substring(startPoint,endPoint);
                int nameStartPoint = str.indexOf("_blank\">")+8;   //第一个>括号之后
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
