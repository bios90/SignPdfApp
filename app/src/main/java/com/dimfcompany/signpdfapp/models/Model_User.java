package com.dimfcompany.signpdfapp.models;

import java.io.Serializable;

public class Model_User implements Serializable
{
    int id;
    String first_name;
    String last_name;
    String email;
    String password;
    String fb_token;
    int role_id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFb_token()
    {
        return fb_token;
    }

    public void setFb_token(String fb_token)
    {
        this.fb_token = fb_token;
    }

    public int getRole_id()
    {
        return role_id;
    }

    public void setRole_id(int role_id)
    {
        this.role_id = role_id;
    }
}
