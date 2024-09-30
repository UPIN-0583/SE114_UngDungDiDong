package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Class implements Serializable {
    private int ID;
    private String className;
    private List<Student> students;

    public Class(int ID, String className, List<Student> students) {
        this.ID = ID;
        this.className = className;
        this.students = students;
    }

    public String getClassName() {
        return className;
    }

    public int getID() {
        return ID;
    }

    public List<Student> getStudents() {
        return students;
    }
}
