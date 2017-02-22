package edu.amherst.cs.jsonxmldemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprasad on 2/22/17.
 */

public class Student {

    String name;
    int gradYear;
    List<Course> courses = new ArrayList<Course>();

    Student(String name, int gradYear, List<Course> courses)
    {
        this.name= name;
        this.gradYear = gradYear;
        this.courses = courses;
    }

}
