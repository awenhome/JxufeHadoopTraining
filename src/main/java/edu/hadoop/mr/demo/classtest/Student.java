package edu.hadoop.mr.demo.classtest;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 彭文忠
 * @describe 学生类
 */
public class Student implements WritableComparable<Student> {
    private long id;
    private int age;
    private String stuName;
    private String province;

    //无参数构造函数
    public Student() {
    }
    //构造函数：全参数
    public Student(long id, int age, String stuName, String province) {
        this.id = id;
        this.age = age;
        this.stuName = stuName;
        this.province = province;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", age=" + age +
                ", stuName='" + stuName + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

    //让Student类可比较:线按照年龄进行降序，再按照名字字符串进行降序
    @Override
    public int compareTo(Student other) {
        if(this.getAge()>other.getAge()){
            return -1;
        }else if(this.getAge() < other.getAge()){
            return 1;
        }else{
            return -this.getStuName().compareTo(other.getStuName());
        }
//        return this.getAge()>other.getAge()?1:(this.getAge()<other.getAge()?-1:0);
    }

    //系列化：让类转化为DataOutput输出流，可以写入磁盘文件或在网络上进行传输
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeInt(this.age);
        out.writeUTF(this.stuName);
        out.writeUTF(this.province);
    }
    //反系列化：让DataInput输入流，通过readFields转变成Student对象
    @Override
    public void readFields(DataInput in) throws IOException {
//        this.id = in.readLong();
        setId(in.readLong());
        setAge(in.readInt());
        setStuName(in.readUTF());
        setProvince(in.readUTF());
    }
}
