package edu.amherst.cs.studentsqldemo;

/**
 * Created by aarathi on 9/19/15.
 * Student object for the StudentSQLDemo table
 */
public class StudentDemo {
    private String name;
    private String major;
    private int year;
    private long id;

    public long getId() {return id;}

    public void setId(long id) {this.id=id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor(){
        return major;
    }

    public void setMajor(String major)
    {
        this.major = major;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name + ", " + major + " " + year;
    }
}
