package com.dimfcompany.signpdfapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dimfcompany.signpdfapp.local_db.room.DateConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "documents")
public class Model_Document implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    long id;
    long user_id;
    Integer city;
    String fio;
    String adress;
    String phone;
    String signature_file_name;
    String pdf_file_name;
    String check_file_name;
    String code;

    @TypeConverters({DateConverter.class})
    Date date;

    double sum;
    double montage;
    double delivery;
    double sale;
    double itogo_sum;
    double prepay;

    @TypeConverters({DateConverter.class})
    Date deleted_at;

    String order_form;
    String dop_info;

    int sync_status;

    @Ignore
    @SerializedName("products")
    List<Model_Product> listOfProducts = new ArrayList<>();

    @Ignore
    Model_User user;

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

    public long getUser_id()
    {
        return user_id;
    }

    public void setUser_id(long user_id)
    {
        this.user_id = user_id;
    }

    public String getCheck_file_name()
    {
        return check_file_name;
    }

    public void setCheck_file_name(String check_file_name)
    {
        this.check_file_name = check_file_name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getPdf_file_name()
    {
        return pdf_file_name;
    }

    public void setPdf_file_name(String pdf_file_name)
    {
        this.pdf_file_name = pdf_file_name;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
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

    public int getSync_status()
    {
        return sync_status;
    }

    public void setSync_status(int sync_status)
    {
        this.sync_status = sync_status;
    }

    public Date getDeleted_at()
    {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at)
    {
        this.deleted_at = deleted_at;
    }

    public Model_User getUser()
    {
        return user;
    }

    public void setUser(Model_User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "Model_Document{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", city=" + city +
                ", fio='" + fio + '\'' +
                ", adress='" + adress + '\'' +
                ", phone='" + phone + '\'' +
                ", signature_file_name='" + signature_file_name + '\'' +
                ", pdf_file_name='" + pdf_file_name + '\'' +
                ", check_file_name='" + check_file_name + '\'' +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", sum=" + sum +
                ", montage=" + montage +
                ", delivery=" + delivery +
                ", sale=" + sale +
                ", itogo_sum=" + itogo_sum +
                ", prepay=" + prepay +
                ", deleted_at=" + deleted_at +
                ", order_form='" + order_form + '\'' +
                ", dop_info='" + dop_info + '\'' +
                ", sync_status=" + sync_status +
                '}';
    }
}
