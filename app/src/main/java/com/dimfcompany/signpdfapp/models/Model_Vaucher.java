package com.dimfcompany.signpdfapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "vauchers")
public class Model_Vaucher implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    long id;
    long document_id;
    String header;

    @Ignore
    List<Model_Price_Element> price_elements = new ArrayList<>();

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getDocument_id()
    {
        return document_id;
    }

    public void setDocument_id(long document_id)
    {
        this.document_id = document_id;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public List<Model_Price_Element> getPrice_elements()
    {
        return price_elements;
    }

    public void setPrice_elements(List<Model_Price_Element> price_elements)
    {
        this.price_elements = price_elements;
    }
}
