package edu.hadoop.mr.demo.temperature.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @简介 用Java代码来验证结果是否正确
 * 思路：
 * 		1.用TreeMap来存储结果，格式为：<key=时间年月，value=最高温度>
 *      2.当key相同时比较Value，讲Value更大的放入Map中
 *      3.最后输出Map即可
 * @結果：
 * 201801=37.5, 201802=34.1, 201803=36.6, 201804=40.2, 201805=39.3, 201806=42.6, 201807=45.5, 201808=44.2, 201809=38.4, 201810=33.0, 201811=34.0, 201812=33.0
 */
public class FileTest {
	public static void main(String[] args) {
		Map<String, Double> result = new TreeMap<String, Double>();
		String fileName = "localdata\\txt\\temperature\\cndcdata10000.txt";
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader bf = new BufferedReader(fr);
			String str;
			int line=0;
			// 按行读取字符串
			while ((str = bf.readLine()) != null) {
				line++;
				// arrayList.add(str);
				// 每行的内容
				// 2 取出日期
				String dateNum = str.substring(15, 21);
				// 3 取出温度、湿度、纬度、经度、压力
				double tempDouble = 0;
				if (str.charAt(87) == '+') {//  parseInt doesn't like leading plus  
					tempDouble = Double.parseDouble(str.substring(88, 92)) / 10;
				} else {
					tempDouble = Double.parseDouble(str.substring(87, 92)) / 10;
				}
				if(dateNum.equals("201801")&&tempDouble>30) {
					System.out.println("line:"+line+";"+dateNum+":"+tempDouble);
				}

				// String temp = str.substring(87, 92).substring(2, 5);
				// double tempDouble = Double.parseDouble(temp)/10;
				// System.out.println(dateNum+"-"+tempDouble);
				if (result.get(dateNum) != null) {
					if (result.get(dateNum) < tempDouble && tempDouble != 999.9) {
						result.put(dateNum, tempDouble);
					}
				} else {
					result.put(dateNum, tempDouble);
				}
			}
			bf.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// // 对ArrayList中存储的字符串进行处理
		// System.out.println("共行数："+arrayList.size());
		// for (int i = 0; i < arrayList.size(); i++) {
		// System.out.println(arrayList.get(i));
		// }
		System.out.println("result:" + result.toString());
		System.out.println(Double.parseDouble("-0347.0") / 10);
	}

}
