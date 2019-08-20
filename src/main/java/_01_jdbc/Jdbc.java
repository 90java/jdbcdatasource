package _01_jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;

public class Jdbc {

     static final String DRIVER="com.mysql.cj.jdbc.Driver";

     static final String URL="jdbc:mysql://192.168.40.128:3306/demo01?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";

     static final String NAME="nojava";

     static final String PASSWORD="111111";


    @Before
    public void init(){
        System.out.println("建表");
        System.out.println("/*\n" +
                "SQLyog 企业版 - MySQL GUI v8.14 \n" +
                "MySQL - 8.0.17 \n" +
                "*********************************************************************\n" +
                "*/\n" +
                "/*!40101 SET NAMES utf8 */;\n" +
                "\n" +
                "create table `student` (\n" +
                "\t`id` double ,\n" +
                "\t`name` varchar (120),\n" +
                "\t`age` timestamp \n" +
                "); \n" +
                "insert into `student` (`id`, `name`, `age`) values('2','90java','2019-08-20 15:11:22');");

    }


    @Test
    public void test01() throws Exception{
        //注册驱动
        Class.forName(DRIVER);
        //连接数据库
        Connection connection = DriverManager.getConnection(URL, NAME, PASSWORD);
        //创建Statement对象
        Statement statement = connection.createStatement();
        String sql="select * from student";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            Timestamp age = resultSet.getTimestamp(3);
            System.out.println("id:"+id);
            System.out.println("name:"+name);
            System.out.println("age:"+age);
            System.out.println("------------");
            int id1 = resultSet.getInt("id");
            String name1 = resultSet.getString("name");
            Timestamp age1 = resultSet.getTimestamp("age");
            System.out.println("id1:"+id1);
            System.out.println("name1:"+name1);
            System.out.println("age1:"+age1);
            System.out.println("-------------------------------");
        }
        //关闭resultSet
        resultSet.close();
        //关闭statement
        statement.close();
        //关闭连接
        connection.close();
    }


}
