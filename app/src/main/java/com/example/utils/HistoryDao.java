package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao {
    public Boolean Insert(History history){
        history.setFlower(history.getFlower());
        history.setName(history.getName());
        history.setScore(history.getScore());
        history.setPhone(history.getPhone());
        history.setDate(history.getDate());
        if(history.save())
            return true;
        else
            return false;
    }
    public List<History> QueryByAuthor(String phone){
        List<History> historys= DataSupport.findAll(History.class);
        List<History> results=new ArrayList<History>();
        for(History history:historys){
            if(history.getPhone().equals(phone)) {
                results.add(history);
            }
        }
        return results;
    }
}
