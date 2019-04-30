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
    String code;
    long date;

    double sum;
    double montage;
    double delivery;
    double sale;
    double itogo_sum;
    double prepay;

    String order_form;
    String dop_info;

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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
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


    public double getSum()
    {
        return sum;
    }

    public void setSum(double sum)
    {
        this.sum = sum;
    }

    public double getMontage()
    {
        return montage;
    }

    public void setMontage(double montage)
    {
        this.montage = montage;
    }

    public double getDelivery()
    {
        return delivery;
    }

    public void setDelivery(double delivery)
    {
        this.delivery = delivery;
    }

    public double getSale()
    {
        return sale;
    }

    public void setSale(double sale)
    {
        this.sale = sale;
    }

    public double getItogo_sum()
    {
        return itogo_sum;
    }

    public void setItogo_sum(double itogo_sum)
    {
        this.itogo_sum = itogo_sum;
    }

    public double getPrepay()
    {
        return prepay;
    }

    public void setPrepay(double prepay)
    {
        this.prepay = prepay;
    }

    public String getOrder_form()
    {
        return order_form;
    }

    public void setOrder_form(String order_form)
    {
        this.order_form = order_form;
    }

    public String getDop_info()
    {
        return dop_info;
    }

    public void setDop_info(String dop_info)
    {
        this.dop_info = dop_info;
    }

    @Override
    public String toString()
    {
        return "Model_Document{" +
                "id_local=" + id_local +
                ", city=" + city +
                ", fio='" + fio + '\'' +
                ", adress='" + adress + '\'' +
                ", phone='" + phone + '\'' +
                ", signature_file_name='" + signature_file_name + '\'' +
                ", pdf_file_name='" + pdf_file_name + '\'' +
                ", date=" + date +
                ", sum=" + sum +
                ", montage=" + montage +
                ", delivery=" + delivery +
                ", sale=" + sale +
                ", itogo_sum=" + itogo_sum +
                ", prepay=" + prepay +
                ", order_form='" + order_form + '\'' +
                ", dop_info='" + dop_info + '\'' +
                ", listOfProducts=" + listOfProducts +
                '}';
    }
}
