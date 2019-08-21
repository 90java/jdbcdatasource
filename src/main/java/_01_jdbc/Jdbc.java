package _01_jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Date;

public class Jdbc {

     static final String DRIVER="com.mysql.cj.jdbc.Driver";

     static final String URL="jdbc:mysql://192.168.40.128:3306/demo01?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";

     static final String NAME="nojava";

     static final String PASSWORD="111111";

    Connection connection = null;

    Statement statement = null;

    ResultSet resultSet = null;

    PreparedStatement preparedStatement = null;

    @Before
    public void init() throws Exception{
        //注册驱动
        Class.forName(DRIVER);
        //连接数据库
        connection = DriverManager.getConnection(URL, NAME, PASSWORD);
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
