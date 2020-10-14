package com.diga.generic.test.entity;

import com.diga.generic.utils.PropUtils;
import lombok.Data;

import java.util.List;


@Data
public class Course {
    private int id;
    private String name;
    private List<Student> studentList;

    public static void main(String[] args) throws Exception {
        PropUtils.KV kv = PropUtils.load("classpath:app.properties");
        Course course = kv.build(Course.class);
        System.out.println(course);
    }
}
