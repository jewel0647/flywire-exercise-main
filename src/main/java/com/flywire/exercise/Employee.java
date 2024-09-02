package com.flywire.exercise;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String position;
    private String hireDate;
    private boolean active;
    private List<Integer> directReports;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Integer> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Integer> directReports) {
        this.directReports = directReports;
    }

}
