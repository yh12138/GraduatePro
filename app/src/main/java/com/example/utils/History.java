package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class History extends DataSupport {
    private int id;
    private byte[] flower;//图片
    private String name;
    private String score;
    private Date date;
    private String phone;
    public History(){
        super();
    }

    public History(byte[] flower,String name,String score,String phone,Date date){
        super();
        this.flower=flower;
        this.name=name;
        this.score=score;
        this.phone=phone;
        this.date=date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFlower() {
        return flower;
    }

    public void setFlower(byte[] flower) {
        this.flower = flower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
