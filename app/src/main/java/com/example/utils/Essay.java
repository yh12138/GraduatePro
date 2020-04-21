package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Essay extends DataSupport {
    private int id;
    private String title;
    private String content;
    private String author;//用户手机号
    private int commentCount ;
    private int collectCount ;
    private Date date;
    private int model;
    private byte[] image;//

    public Essay() {
        super();
    }
    public Essay(String title, String content,String anthor,int commentCount,int collectCount,Date date,int model,byte[] image) {
        super();
        this.title = title;
        this.content = content;
        this.author = anthor;
        this.commentCount = commentCount;
        this.collectCount = collectCount;
        this.date = date;
        this.model=model;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setAuthor(String anthor) {
        this.author = anthor;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
