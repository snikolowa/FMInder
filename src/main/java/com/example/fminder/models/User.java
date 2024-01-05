package com.example.fminder.models;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Gender graduationYear;
    private int year;
    private Major major;

    public User(String firstName, String lastName, String email, String password, Gender graduationYear, int year, Major major) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.graduationYear = graduationYear;
        this.year = year;
        this.major = major;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Gender graduationYear) {
        this.graduationYear = graduationYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }
}
