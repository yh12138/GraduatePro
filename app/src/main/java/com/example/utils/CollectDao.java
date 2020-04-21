package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CollectDao {
    public Boolean Insert(Collect collect){
        collect.setAuthor(collect.getAuthor());
        collect.setEssayid(collect.getEssayid());
        collect.setDate(collect.getDate());
        if(collect.save())
            return true;
        else
            return false;
    }
    public List<Collect> QueryAll(String phone){
        List<Collect> collects= DataSupport.findAll(Collect.class);
        List<Collect> results= new ArrayList<>();
        Collect result=new Collect();
        for(Collect collect:collects){
            if(collect.getAuthor().equals(phone)) {
                result = collect;
                results.add(result);
            }
        }
        return results;
    }
}
