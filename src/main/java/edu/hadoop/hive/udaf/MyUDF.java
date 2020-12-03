package edu.hadoop.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyUDF extends UDF {
	public String evaluate(String canshu1,String canshu2) {
		return canshu1+"_"+canshu2;
	}
	
	public String evaluate(String gradeid) {
		String gradeName;
		switch(gradeid){
		case "11":
			gradeName="小学一年级";
			break;
		case "12":
			gradeName="小学二年级";
			break;
		default:
			gradeName="幼儿园";
			break;
		}
		return gradeName;
	}
	

}
