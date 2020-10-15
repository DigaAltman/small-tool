package com.diga.db.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Course implements Serializable {
    private Integer courseId;
    private String courseName;
    private List<Student> studentList;
}
