package com.example.voting_project;

public class Votes {
    String name;
    String check;
    String id;

    public Votes(String id, String name, String check)
    {
        this.check= check;
        this.name = name;
        this.id = id;
    }

    public Votes(String name, String check)
    {
        this.check= check;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getCheck()
    {
        return check;
    }
    public void setCheck(String check)
    {
        this.name = check;
    }

    public String getId()
    {
        return name;
    }
    public void setId(String id)
    {
        this.name = id;
    }

    public String toString()
    {
        return this.getId() + " " + this.getName() + " " + this.getCheck();

    }
}
