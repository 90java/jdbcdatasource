package com.nojava._01_jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class Jdbc {

     static String driver;

    static String url;

    static String name;

    static String password;

    Connection connection = null;

    Statement statement = null;

    ResultSet resultSet = null;

    PreparedStatement preparedStatement = null;

    CallableStatement callableStatement = null;

    @Before
    public void init() throws Exception{
        //从配置文件中读取配置
        Properties properties = new Properties();
        properties.load(new FileInputStream("D:\\IdeaProjects\\jdbcdatasource\\src\\main\\resources\\db.properties"));
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        name = properties.getProperty("name");
        password = properties.getProperty("password");
        //注册驱动
        Class.forName(driver);
        //连接数据库
        connection = DriverManager.getConnection(url, name, password);
        //创建Statement对象
        statement = connection.createStatement();
    }

    /**
     * 查询
     * @throws Exception
     */
    @Test
    public void test01() throws Exception{
        String sql="select * from student";
        //执行sql
        resultSet = statement.executeQuery(sql);
        //遍历结果
        while (resultSet.next()){
            //根据行列获取
            System.out.println("根据行列获取");
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            Timestamp age = resultSet.getTimestamp(3);
            System.out.println("id:"+id);
            System.out.println("name:"+name);
            System.out.println("age:"+age);
            //根据表字段查询
            System.out.println("根据表字段查询");
            int id1 = resultSet.getInt("id");
            String name1 = resultSet.getString("name");
            Timestamp age1 = resultSet.getTimestamp("age");
            System.out.println("id1:"+id1);
            System.out.println("name1:"+name1);
            System.out.println("age1:"+age1);
            System.out.println("-------------------------------");
        }

    }

    /**
     * 执行插入操作（Statement）
     * @throws Exception
     */
    @Test
    public void test02() throws Exception{
        String sql = "INSERT INTO student(name,age) VALUES('百度',now())";
        statement.executeUpdate(sql);
    }

    /**
     * 更新（Statement）
     * @throws Exception
     */
    @Test
    public void test03() throws Exception{
        String sql = "update student set name ='afeng' where id = 3";
        statement.executeUpdate(sql);
    }

    /**
     * 删除（Statement）
     * @throws Exception
     */
    @Test
    public void test04() throws Exception{
        String sql = "delete from student where id = 3";
        statement.executeUpdate(sql);
    }



    /**
     * 插入(PreparedStatement)
     * @throws Exception
     */
    @Test
    public void test05() throws Exception{
        String sql = "INSERT INTO student(name,age) VALUES(?,?)";
        //预编译
        preparedStatement = connection.prepareStatement(sql);
        //传参
        preparedStatement.setString(1,"xiaohong");
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        preparedStatement.setTimestamp(2,timestamp);
        //执行
        preparedStatement.execute();
        System.out.println(preparedStatement.toString());
    }

    /**
     * 更新操作(PreparedStatement)
     * @throws Exception
     */
    @Test
    public void test06() throws  Exception{
        String sql = "update student set name =? where id = ?";
        //预编译
        preparedStatement = connection.prepareStatement(sql);
        //传参
        preparedStatement.setString(1,"xiaoli");
        preparedStatement.setInt(2,8);
        //执行更新操作
        int i = preparedStatement.executeUpdate();
        System.out.println("执行了几条："+i);
    }

    /**
     * 删除操作(PreparedStatement)
     * @throws Exception
     */
    @Test
    public void test07() throws Exception{
        String sql = "delete from student where id = ?";
        //获得PreparedStatement对象 预编译
        preparedStatement = connection.prepareStatement(sql);
        //传参
        preparedStatement.setInt(1,7);
        //执行
        int i = preparedStatement.executeUpdate();
        System.out.println("执行了几条："+i);
    }

    @Test
    public void test08() throws Exception{
        String sql = "select * from student where name = ? and password = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,"xiaoming");
        String a = "aaa 'or '8=8'";
        preparedStatement.setString(2,a);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("name");
            System.out.println(name);
        }
        System.out.println(preparedStatement.toString());
    }

    /**
     * 事务测试
     *  首先需要将事务改为手动提交
     *  正常情况下会进行插入操作
     *  异常：分提交前和提交后
     *  提交前异常 数据库未存入。
     *  提交后异常 数据库存入
     * @throws Exception
     */
    @Test
    public void test09() {

        try {
            //false 关闭默认提交
            //com.mysql.cj.protocol.a.NativeServerSession.autoCommit     private boolean autoCommit = true; 源码看出默认为true
            connection.setAutoCommit(false);
            String sql = "INSERT INTO student(name,age) VALUES(?,?)";
            //预编译
            preparedStatement = connection.prepareStatement(sql);
            //传参
            preparedStatement.setString(1,"天天向上2");
            Date date = new Date();
            Timestamp timestamp = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(2,timestamp);
            //执行
            preparedStatement.execute();
//            int i = 1/0;
            connection.commit();
            int i =1/0;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存点测试
     * 在保存前设置一个保存点 过程中出现异常时，可以回滚到保存点。然后commit 提交保存点前执行的sql。
     */
    @Test
    public void test10 (){

        Savepoint savepoint01 =null;
        Savepoint savepoint02 =null;

        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO student(name,age) VALUES(?,?)";
            //预编译
            preparedStatement = connection.prepareStatement(sql);
            savepoint01 = connection.setSavepoint("savepoint01");
            //传参
            preparedStatement.setString(1,"天天向上5");
            Date date = new Date();
            Timestamp timestamp = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(2,timestamp);
            //执行
            preparedStatement.execute();

            //异常
            int i = 1/0;

            savepoint02 = connection.setSavepoint("savepoint02");

            //传参
            preparedStatement.setString(1,"天天向上6");
            Date date1 = new Date();
            Timestamp timestamp1 = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(2,timestamp1);
            //执行
            preparedStatement.execute();
//            //异常
//            int i = 1/0;
            connection.commit();
        } catch (Exception e) {
            if(savepoint02!=null){
                try {
                    connection.rollback(savepoint02);
                    connection.commit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if(savepoint01!=null){
                try {
                    connection.rollback(savepoint01);
                    connection.commit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Statement 进行批处理操作
     * 步骤：
     */
    @Test
    public void test11() throws Exception{

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql1 = "INSERT INTO student(name,age) VALUES('百度1',now())";
            statement.addBatch(sql1);
            String sql2 = "INSERT INTO student(name,age) VALUES('百度2',now())";
            statement.addBatch(sql2);
            String sql3 = "INSERT INTO student(name,age) VALUES('百度3',now())";
            statement.addBatch(sql3);
            String sql4 = "INSERT INTO student(name,age) VALUES('百度4',now())";
            statement.addBatch(sql4);
            String sql5 = "INSERT INTO student(name,age) VALUES('百度5',now())";
            statement.addBatch(sql5);
            String sql6 = "INSERT INTO student(name,age) VALUES('百度6',now())";
            statement.addBatch(sql6);
            String sql7 = "INSERT INTO student(name,age) VALUES('百度7',now())";
            statement.addBatch(sql7);
            String sql8 = "INSERT INTO student(name,age) VALUES('百度8',now())";
            statement.addBatch(sql8);
            String sql9 = "INSERT INTO student(name,age) VALUES('百度9',now())";
            statement.addBatch(sql9);
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }

    }

    /**
     * PreparedStatement 进行批处理操作
     */
    @Test
    public void test12(){
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO student(name,age) VALUES(?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"美团1");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团2");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团3");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团4");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团5");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团6");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团7");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团8");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.setString(1,"美团9");
            preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 调用存储过程
     *  sql 语法为{call PROCEDURE(?,?)}
     *  1.获得CallableStatement对象
     *  2.设置参数
     *  3.执行
     *  ok
     */
    @Test
    public void test13(){
        try {
            String sql = "{call test(?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1,4);
            callableStatement.execute();
        } catch (SQLException e) {

        }

    }


    @After
    public void end() throws Exception{
        //关闭resultSet
        if(resultSet!=null){
            resultSet.close();
        }
        //关闭statement
        if(statement!=null){
            statement.close();
        }
        //关闭preparedStatement
        if(preparedStatement!=null){
            preparedStatement.close();
        }
        //关闭连接
        if(connection!=null){
            connection.close();
        }
    }

}
