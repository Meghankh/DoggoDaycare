package com.doggo.doggydaycare.data;

/**
 * Created by Meghankh on 5/5/17.
 */

public class TodoItem
{
    private String subject;
    private String message;

    public TodoItem(String subject, String message)
    {
        this.subject = subject;
        this.message = message;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
