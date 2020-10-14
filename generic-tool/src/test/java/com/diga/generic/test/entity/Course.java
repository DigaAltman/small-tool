package com.diga.generic.test.entity;

import com.diga.generic.utils.PropUtils;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;
import java.util.List;


@Data
public class Course {
    private int id;
    private String name;

    @ToString.Exclude
    private List<Student> studentList;

    public static void main(String[] args) throws Exception {

        /**
         *
         * course.id=1
         * course.name=3-2
         *
         * student.id.1=1
         * student.name.1=Min.Lee
         * student.sex.1=true
         *
         * student.id.2=2
         * student.name.2=GGBoy
         * student.sex.2=true
         *
         * student.id.xee=3
         * student.name.xee=ZuLi
         * student.sex.xee=false
         *
         */

        PropUtils.KV kv = PropUtils.load("classpath:app.properties");
        Collection<Student> studentList = kv.buildCollection(List.class, Student.class);
        System.out.println(studentList);
    }
}
