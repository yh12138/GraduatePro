package com.example.utils;

import org.litepal.crud.DataSupport;

public class RememberUser extends DataSupport {
    private int id;
    private String phone;
    private String password;

    public RememberUser(){
        super();
    }
    public RememberUser(String phone,String password){
        super();
        this.phone=phone;
        this.password=password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
