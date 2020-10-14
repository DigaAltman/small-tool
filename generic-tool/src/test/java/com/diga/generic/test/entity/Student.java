package com.diga.generic.test.entity;

import lombok.Data;

@Data
public class Student {
    private int id;
    private String name;
    private boolean sex;
    private Course course;
}
