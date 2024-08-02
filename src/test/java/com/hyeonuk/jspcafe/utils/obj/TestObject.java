package com.hyeonuk.jspcafe.utils.obj;

public class TestObject {
    private String name;
    private int age;
    private boolean isStudent;

    public TestObject(){

    }

    public TestObject(String name, int age, boolean isStudent) {
        this.name = name;
        this.age = age;
        this.isStudent = isStudent;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isStudent() {
        return isStudent;
    }
}
