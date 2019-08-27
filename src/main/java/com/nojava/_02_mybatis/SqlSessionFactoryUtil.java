package com.nojava._02_mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 获取SqlSession对象
 */
public class SqlSessionFactoryUtil {

    public static SqlSessionFactory getSqlSessionFactory() throws IOException {
        //1.读取配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        //2.创建sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }

    public static SqlSession openSession() throws IOException {
        return openSession(false);
    }

    public static SqlSession openSession(Boolean autoCommit) throws IOException {
        return getSqlSessionFactory().openSession(autoCommit);
    }
}
