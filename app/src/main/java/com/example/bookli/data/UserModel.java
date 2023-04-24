package com.example.bookli.data;

public class UserModel {
    String name;
    int studentId;
    String phoneNumber;
    String email;

    public UserModel(String name, int studentId, String phoneNumber, String email) {
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public UserModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
