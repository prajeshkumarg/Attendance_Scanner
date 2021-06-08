package com.example.attendancescanner.models;

import java.util.List;

public class Profile {

    private String name;
    private String email;
    private String regno;
    private List<Classes> classesList;

    public Profile() {
    }

    public Profile(String name, String email, String regno, List<Classes> classesList) {
        this.name = name;
        this.email = email;
        this.regno = regno;
        this.classesList = classesList;
    }

    public Profile(String name, String email, String regno) {
        this.name = name;
        this.email = email;
        this.regno = regno;
    }

    public List<Classes> getClassesList() {
        return classesList;
    }

    public void setClassesList(List<Classes> classesList) {
        this.classesList = classesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }
}
