package edu.hadoop.mr.demo.sortsecond;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @author 彭文忠
 * @date 2020/11/26
 * @说明 手把手教学敲代码：二级排序案例
 * @班级  002班，软件工程专业
 *二级排序要求：
    编写MapReduce程序对students.txt（文件格式为编号,姓名,年龄,性别,部门ID）文件进行处理，对数据按照年龄和姓名进行降序排序（即先按年龄降序，年龄相同时按姓名字符串降序）。
    students.txt数据：链接: http://pan-yz.chaoxing.com/share/info/75cc4b688048d80e
    （1）自定义分区分为两个区；
    （2）输出格式参考：
    33 Lill
    33 Bill
    ......
 */
public class StudentSortSecondDemo {
    public static class StuSortSecMapper extends Mapper<LongWritable, Text,Student, NullWritable>{
        //Ctrl+O快捷键，显示父类中有哪些方法
        int lineNumber;
        /**
         * @说明 在map方法执行前执行且仅执行一次
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            lineNumber = 0;
        }

        @Override
        protected void map(LongWritable keyIn, Text valueIn, Context context) throws IOException, InterruptedException {
            //TODO 处理一行内容，从<K1,V1>到List<K2,V2>
            lineNumber++;
            //valueIn例子：1,Tom,25,F,85  ==>每行转成一个Student类的对象
            String[] propertys = valueIn.toString().split(",");  //空白字符切分
            Student stu;
            if(propertys.length==5){
                stu = new Student(Long.parseLong(propertys[0]),propertys[1],Integer.parseInt(propertys[2]),propertys[3].trim().charAt(0),Integer.parseInt(propertys[4]));
                context.write(stu,NullWritable.get());
                System.out.println("Map out:"+stu);
            }else{
                //数据不规范
            }

        }

        /**
         * @说明 在map方法执行后执行且仅执行一次
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            System.out.println("文件一共有行数："+lineNumber);
        }


    }

    /**
     * @说明  实现Reduce类
     */
    public static class StuSortSecReduce extends Reducer<Student,NullWritable,Student,NullWritable>{
        //Ctrl+O

        @Override
        protected void reduce(Student key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            System.out.println("Reduce key:"+key);
            context.write(key,NullWritable.get());
        }
    }


    /**
     * @简介 自定义分区
     * @分区规则  按学生年龄来判断分区，规则：
     *      分区0：30岁以下（含30岁）
     *      分区1：30岁以上
     */
    public static class StuSortSecPartitioner extends Partitioner<Student,NullWritable>{
        @Override
        public int getPartition(Student key, NullWritable value, int numPartitions) {
            try {
                if(key.getAge()<=30){
                    System.out.println("Partitioner:"+key.getStuName()+";"+key.getAge()+"==>0");
                    return 0%numPartitions;
                }else{
                    System.out.println("Partitioner:"+key.getStuName()+";"+key.getAge()+"==>1");
                    return 1%numPartitions;
                }
            }catch(Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String inputPath = "/inputdata/students.txt",outputPath="/output/StuSortSecDemo";
        if(args.length>=2) {
            inputPath =  args[0];
            outputPath=args[1];
        }
        //1.Client提交Job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //2.设置任务的入口：驱动类
        job.setJarByClass(StudentSortSecondDemo.class);

        //3.设置Mapper相关
        //设置输入文件路劲：add可以添加多个不同路劲的文件或文件夹
        TextInputFormat.addInputPath(job,new Path(inputPath));
        job.setMapperClass(StuSortSecMapper.class);
        job.setMapOutputKeyClass(Student.class);
        job.setMapOutputValueClass(NullWritable.class);

        //4.设置Reduce类
        job.setReducerClass(StuSortSecReduce.class);
        job.setNumReduceTasks(2);  //多少个Reduce任务
        job.setOutputKeyClass(Student.class);
        job.setOutputValueClass(NullWritable.class);
        //5.设置job任务输出
        //输出路劲如果存在则删除
        FileSystem.get(conf).delete(new Path(outputPath),true);
        TextOutputFormat.setOutputPath(job,new Path(outputPath));

        //6.Job其它设置：Partitioner
        job.setPartitionerClass(StuSortSecPartitioner.class);


        System.out.println(job.waitForCompletion(true)?"Job execute SUCCESS":"Job execute FAILED");  //sout

    }

}
