package com.dimfcompany.signpdfapp.models;

import java.io.Serializable;

public class Model_Product implements Serializable
{
    Model_Material material;
    float width;
    float height;
    Model_Color color;
    Model_Krep krep;
    Model_Control control;
    int count;
    double price;
    double sum;

    public Model_Product()
    {

    }

    public Model_Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Model_Material material)
    {
        this.material = material;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
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
                "material=" + material.getName() +
                ", width=" + width +
                ", height=" + height +
                ", color=" + color.getName() +
                ", krep=" + krep.getName() +
                ", control=" + control.getName() +
                ", count=" + count +
                ", price=" + price +
                ", sum=" + sum +
                '}';
    }
}
