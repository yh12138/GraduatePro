package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RememberUserDao {
    public Boolean Insert(RememberUser user){
        user.setPhone(user.getPhone());
        user.setPassword(user.getPassword());
        if(user.save())
            return true;
        else
            return false;
    }
    public RememberUser Query(String phone){
        List<RememberUser> users= DataSupport.findAll(RememberUser.class);
        RememberUser result=new RememberUser();
        for(RememberUser user:users){
            if(user.getPhone().equals(phone)) {
                result = user;
                break;
            }
        }
        return result;
    }
}
