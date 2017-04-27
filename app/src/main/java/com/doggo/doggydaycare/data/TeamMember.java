package com.doggo.doggydaycare.data;

/**
 * Created by Meghan on 2/17/2017.
 */
public class TeamMember
{
    private String name;
    private int steps;
    private int goal;

    public TeamMember(String name,
                      int steps,
                      int goal)
    {
        this.name = name;
        this.steps = steps;
        this.goal = goal;
    }

    public String getName()
    {
        return name;
    }

    public int getGoal()
    {
        return goal;
    }

    public int getSteps()
    {
        return steps;
    }
}

