package com.example.sqlite_quanli;

public class ClassItem {
    private String classId;
    private String className;

    public ClassItem(String classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }
}

