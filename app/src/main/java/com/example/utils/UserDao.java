package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UserDao {
    public Boolean Insert(User user){
        user.setHeadimg(user.getHeadimg());
        user.setPhone(user.getPhone());
        user.setPassword(user.getPassword());
        user.setName(user.getName());
        user.setRole(user.getRole());
        if(user.save())
            return true;
        else
            return false;
    }
    public User Query(String phone){
        List<User> users= DataSupport.findAll(User.class);
        User result=new User();
        for(User user:users){
            if(user.getPhone().equals(phone)) {
                result = user;
                break;
            }
        }
        return result;
    }
    public void Update(User user,String pwd){
        user.setPassword(pwd);
        user.updateAll("phone = ?",user.getPhone());
    }
}
