package com.dimfcompany.signpdfapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class Model_Addable_Element implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    long id;
    long product_id;
    String name;

    public Model_Addable_Element()
    {

    }

    public long getProduct_id()
    {
        return product_id;
    }

    public void setProduct_id(long product_id)
    {
        this.product_id = product_id;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
