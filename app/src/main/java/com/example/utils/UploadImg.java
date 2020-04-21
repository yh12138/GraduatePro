package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class UploadImg extends DataSupport {

    private int id;
    private byte[] img;
    private String name;
    private Date date;
    private String author;//用户手机号

    public UploadImg(){
        super();
    }
    public UploadImg(byte[] img,String name,Date date,String author){
        super();
        this.img=img;
        this.name=name;
        this.date=date;
        this.author=author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
