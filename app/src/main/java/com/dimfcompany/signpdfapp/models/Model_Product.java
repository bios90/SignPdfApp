package com.dimfcompany.signpdfapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "products")
public class Model_Product implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    long id;
    long document_id;
    String width;
    String height;
    int count;
    double price;
    double sum;

    @Ignore
    Model_Color color;
    @Ignore
    Model_Krep krep;
    @Ignore
    Model_Control control;
    @Ignore
    Model_Material material;

    public Model_Product()
    {

    }

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

    public Model_Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Model_Material material)
    {
        this.material = material;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public Model_Color getColor()
    {
        return color;
    }

    public void setColor(Model_Color color)
    {
        this.color = color;
    }

    public Model_Krep getKrep()
    {
        return krep;
    }

    public void setKrep(Model_Krep krep)
    {
        this.krep = krep;
    }

    public Model_Control getControl()
    {
        return control;
    }

    public void setControl(Model_Control control)
    {
        this.control = control;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getSum()
    {
        return sum;
    }

    public void setSum(double sum)
    {
        this.sum = sum;
    }

    @Override
    public String toString()
    {
        return "Model_Product{" +
                ", width=" + width +
                ", height=" + height +
                ", count=" + count +
                ", price=" + price +
                ", sum=" + sum +
                '}';
    }
}
