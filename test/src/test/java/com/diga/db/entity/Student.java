package com.diga.db.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(exclude = "course")
public class Student implements Serializable {
    private Integer studentId;
    private String studentName;
    private Course course;
}
