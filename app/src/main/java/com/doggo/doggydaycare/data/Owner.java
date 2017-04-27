package com.doggo.doggydaycare.data;

import java.util.ArrayList;

/**
 * Created by Meghan on 4/22/2017.
 */

public class Owner
{
    private String firstName;
    private String lastName;
    private ArrayList<Dog> dogs;

    public Owner(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        dogs = new ArrayList<Dog>();
    }

    public void addDog(Dog dog)
    {
        dogs.add(dog);
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

    public ArrayList<Dog> getDogs() {
        return dogs;
    }
}
