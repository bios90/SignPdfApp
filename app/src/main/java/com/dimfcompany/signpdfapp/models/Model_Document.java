package com.dimfcompany.signpdfapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model_Document implements Serializable
{
    int id_local;
    Integer city;
    String fio;
    String adress;
    String phone;
    String signature_file_name;
    String pdf_file_name;
    long date;

    List<Model_Product> listOfProducts = new ArrayList<>();

    public Model_Document()
    {

    }


    public List<Model_Product> getListOfProducts()
    {
        return listOfProducts;
    }

    public void setListOfProducts(List<Model_Product> listOfProducts)
    {
        this.listOfProducts = listOfProducts;
    }

    public int getId_local()
    {
        return id_local;
    }

    public void setId_local(int id_local)
    {
        this.id_local = id_local;
    }

    public String getPdf_file_name()
    {
        return pdf_file_name;
    }

    public void setPdf_file_name(String pdf_file_name)
    {
        this.pdf_file_name = pdf_file_name;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public Integer getCity()
    {
        return city;
    }

    public void setCity(Integer city)
    {
        this.city = city;
    }

    public String getFio()
    {
        return fio;
    }

    public void setFio(String fio)
    {
        this.fio = fio;
    }

    public String getAdress()
    {
        return adress;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSignature_file_name()
    {
        return signature_file_name;
    }

    public void setSignature_file_name(String signature_file_name)
    {
        this.signature_file_name = signature_file_name;
    }
}
