package com.example.myapplication;

import java.io.Serializable;

public class Student implements Serializable {
    private int studentID;
    private String studentName;
    private String yearBirth;

    public Student(int studentID, String studentName, String yearBirth) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.yearBirth = yearBirth;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getYearBirth() {
        return yearBirth;
    }
}
