package edu.hadoop.mr.demo.sortsecond;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 彭文忠
 * @简介  学生类（属性包括学生ID，姓名，年龄，性别，部门ID）
 * 数据例子：1,Tom,25,F,85
 */
public class Student implements WritableComparable<Student> {
    long stuId;
    String stuName;
    int age;
    char gender = 'F';
    int departmentId;

    public Student(){};

    public Student(long stuId, String stuName, int age, char gender, int departmentId) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.age = age;
        this.gender = gender;
        this.departmentId = departmentId;
    }

    public long getStuId() {
        return stuId;
    }

    public void setStuId(long stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", stuName='" + stuName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", departmentId=" + departmentId +
                '}';
    }



    //实现WritableComparable中必须实现的方法：1系列化和反系列化、2类要可比较
    //1.1 序列化本类：将类中的属性序列化成字节流DataOutput
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(stuId);
        out.writeUTF(stuName);
        out.writeInt(age);
        out.writeChar(gender);
        out.writeInt(departmentId);
    }
    //1.2 反序列化本类：将字节输入流DataInput转换为本类的属性
    @Override
    public void readFields(DataInput in) throws IOException {
        this.stuId=in.readLong();
        setStuName(in.readUTF());
        setAge(in.readInt());  //注意：对应的一定要用writeInt(int)，不能用write(int)
        setGender(in.readChar());
        setDepartmentId(in.readInt());
    }

    /**2.本类的比较规则:>1大；=0相等；<0小
     * 比较规则：二级排序
     * 对数据按照年龄和姓名进行降序排序（即先按年龄降序，年龄相同时按姓名字符串降序）
    */
    @Override
    public int compareTo(Student other) {
        if(this.age>other.getAge()){
            return -1;
        }else if(this.age<other.getAge()){
            return 1;
        }else{//age相等
            return -this.getStuName().compareTo(other.getStuName());
//            if(this.getStuName().compareTo(other.getStuName())>0){
//                return 1;
//            }else if(this.getStuName().compareTo(other.getStuName())<0){
//                return -1;
//            }else{//age相等
//                return 0;
//            }
        }
    }
}
