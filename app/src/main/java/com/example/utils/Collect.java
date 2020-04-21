package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Collect extends DataSupport {

    private int id;
    private String author;//用户手机号
    private Date date;
    private int essayid;

    public Collect() {
        super();
    }
    public Collect( String anthor,Date date,int essayid) {
        super();
        this.author = anthor;
        this.date = date;
        this.essayid=essayid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEssayid() {
        return essayid;
    }

    public void setEssayid(int essayid) {
        this.essayid = essayid;
    }


}
