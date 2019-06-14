package com.dimfcompany.signpdfapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "price_elements")
public class Model_Price_Element implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    long id;
    long vaucher_id;
    String text;
    double price;

    public long getVaucher_id()
    {
        return vaucher_id;
    }

    public void setVaucher_id(long vaucher_id)
    {
        this.vaucher_id = vaucher_id;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
}
