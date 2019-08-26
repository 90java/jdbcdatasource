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
//        4.获得StudentMapper的接口实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> allStudents = mapper.findAllStudents();
        for(Student s : allStudents){
            System.out.println(s);
        }
    }

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
            e.printStackTrace();
        } finally {
        }

    }














    @After
    public void after(){

    }

}
