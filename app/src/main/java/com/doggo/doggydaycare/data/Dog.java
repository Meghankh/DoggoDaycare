package com.doggo.doggydaycare.data;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Meghan on 4/22/2017.
 */
public class Dog {

    private String name;
    private int age;
    private double weight;
    private String gender;
    private Time feedTime;
    private Date lastGroom;
    private String[] medications;

    public Dog(String name, int age, double weight, String gender, Time feedTime, Date lastGroom, String[] medications)
    {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.feedTime = feedTime;
        this.lastGroom = lastGroom;
        this.medications = medications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Time getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(Time feedTime) {
        this.feedTime = feedTime;
    }

    public Date getLastGroom() {
        return lastGroom;
    }

    public void setLastGroom(Date lastGroom) {
        this.lastGroom = lastGroom;
    }

    public String[] getMedications() {
        return medications;
    }

    public void setMedications(String[] medications) {
        this.medications = medications;
    }
}
