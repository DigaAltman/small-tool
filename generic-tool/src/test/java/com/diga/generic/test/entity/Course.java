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
        PropUtils.KV kv = PropUtils.load("classpath:app.properties");
        Collection<Student> studentList = kv.buildCollection(List.class, Student.class);
        System.out.println(studentList);
    }
}
