package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Comment extends DataSupport {

    private int id;
    private String content;
    private String author;//用户手机号
    private Date date;
    private int essayid;

    public Comment() {
        super();
    }
    public Comment( String content,String anthor,Date date,int essayid) {
        super();
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
