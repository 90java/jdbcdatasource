package com.nojava._02_mybatis;

import com.nojava.eo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest {

    InputStream is = null;

    /**
     * SqlSessionFactory和SqlSession接口
     * SqlSessionFactory接口的实现类为一个工厂对象，专门生成sqlSession对象。
     * 生成方式可以为xml生成或者javaapi生成
     *
     *
     * sqlSession接口的实现类时mybatis最重要的一个类，通过该对象动态生成接口的实现类。
     *
     * SqlSessionFactory-->SqlSession-->映射接口实现类（执行sql）
     *
     */

    SqlSessionFactory sqlSessionFactory = null;

    SqlSession sqlSession = null;

    @Before
    public void before() throws Exception{
//        String path = getClass().getResource("/").getFile().toString();
        //1.读取配置文件
        is = Resources.getResourceAsStream("mybatis-config.xml");
        //2.创建sqlSessionFactory对象
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        //3.创建SqlSession对象
        sqlSession = sqlSessionFactory.openSession();
    }


    @Test
    public void test01(){
        //4.1获得StudentMapper的接口实现类对象（动态生成StudentMapper实现类） 推荐！！
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> allStudents = mapper.findAllStudents();
//        for(Student s : allStudents){
//            System.out.println(s);
//        }
        Student student = mapper.findStudentById(2);
        System.out.println(student);
        System.out.println("-------------------");


        //4.2不通过接口生成，而是通过映射文件写好的sql语句
        Student  o = sqlSession.selectOne("com.nojava._02_mybatis.StudentMapper.findStudentById", 2);
        System.out.println(o);

    }

    /**
     * 插入晁错
     * @throws IOException
     */
    @Test
    public void test02() throws IOException {
        try {
            SqlSession sqlSession = SqlSessionFactoryUtil.openSession();
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Student student = new Student();
            student.setName("我是傻逼");
            student.setPassword("22222222222");
            mapper.insertStudent(student);
            sqlSession.commit();
        } catch (IOException e) {
            sqlSession.rollback();
        } finally {
            if(sqlSession!=null){
                sqlSession.close();
            }
        }
    }

    /**
     * 多环境测试
     * 生成肯定不会有多种数据库的 测试使用
     * 注意：一个InputStream不能多个使用，否则报错。
     */
    @Test
    public void test03() throws Exception{
        InputStream is1 = Resources.getResourceAsStream("mybatis-config.xml");
        System.out.println("默认-------------");
        //默认环境
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(is1);
        System.out.println("mysql数据源-------------");
        //mysql数据库环境
        InputStream is2 = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory development = new SqlSessionFactoryBuilder().build(is2, "development");
        SqlSession sqlSessionDev = development.openSession();
        StudentMapper mapperDev = sqlSessionDev.getMapper(StudentMapper.class);
        List<Student> allStudentsDev = mapperDev.findAllStudents();
        for(Student s:allStudentsDev){
            System.out.println(s);
        }

        System.out.println("oracle数据源-----------------");
        //oracle数据环境
        InputStream is3 = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory production = new SqlSessionFactoryBuilder().build(is3, "test");
        SqlSession sqlSessionProd = production.openSession();
        StudentMapper mapperProd = sqlSessionProd.getMapper(StudentMapper.class);
        List<Student> allStudentsProd = mapperProd.findAllStudents();
        for(Student s:allStudentsProd){
            System.out.println(s);
        }
    }


    @After
    public void after(){

    }

}
