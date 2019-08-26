package com.nojava._02_mybatis;

import com.nojava.eo.Student;

import java.util.List;

/**
 * 接口中的方法的名字和XML文件定义的SQL映射语句的名称要相同
 */
public interface StudentMapper {

    List<Student> findAllStudents();

    Student findStudentById(Integer id);

    void insertStudent(Student student);

}
