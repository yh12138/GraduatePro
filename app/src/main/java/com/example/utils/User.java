package com.example.utils;

import org.litepal.crud.DataSupport;

public class User extends DataSupport {

    private int id;


    private byte[] headimg;//头像
    private String phone;
    private String name;
    private String password;
    private String role;
    public User(){
        super();
    }
    public User(byte[] headimg,String phone,String name,String password,String role){
        super();
        this.headimg=headimg;
        this.phone=phone;
        this.name=name;
        this.password=password;
        this.role=role;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getHeadimg() {
        return headimg;
    }

    public void setHeadimg(byte[] headimg) {
        this.headimg = headimg;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
