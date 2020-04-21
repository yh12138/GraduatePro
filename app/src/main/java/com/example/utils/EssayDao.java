package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class EssayDao {
    public Boolean Insert(Essay essay){
        essay.setTitle(essay.getTitle());
        essay.setContent(essay.getContent());
        essay.setAuthor(essay.getAuthor());
        essay.setCommentCount(essay.getCommentCount());
        essay.setCollectCount(essay.getCollectCount());
        essay.setDate(essay.getDate());
        if(essay.save())
            return true;
        else
            return false;
    }
    public List<Essay> QueryAll(){
        List<Essay> essays= DataSupport.findAll(Essay.class);

        return essays;
    }
    public Essay QueryById(int id){
        List<Essay> essays= DataSupport.findAll(Essay.class);
        Essay result=new Essay();
        for(Essay essay:essays){
            if(essay.getId()==id) {
                result = essay;
                break;
            }
        }
        return result;
    }
    public List<Essay> QueryByAuthor(String phone){
        List<Essay> essays= DataSupport.findAll(Essay.class);
        List<Essay> results=new ArrayList<Essay>();
        for(Essay essay:essays){
            if(essay.getAuthor().equals(phone)) {
                results.add(essay);
            }
        }
        return results;
    }
    public void DeleteAll(){
        DataSupport.deleteAll(Essay.class);
    }
}
