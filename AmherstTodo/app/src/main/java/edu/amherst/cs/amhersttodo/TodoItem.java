package edu.amherst.cs.amhersttodo;

import java.util.Date;

/**
 * Created by aprasad on 3/5/17.
 */

public class TodoItem {

    private String category;
    private String priority;
    private String title;
    private String desc;
    private Date duedate;

    public TodoItem (String title, String desc, String priority)
    {
        this.title = title;
        this.desc = desc;
        this.priority = priority;
    }

    public TodoItem(String title, String desc,
                    String category, String priority,
                    Date duedate)
    {
        this.category = category;
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.duedate = duedate;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getPriority()
    {
        return priority;
    }

}
