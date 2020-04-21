package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    public Boolean Insert(Comment comment){
        comment.setContent(comment.getContent());
        comment.setAuthor(comment.getAuthor());
        comment.setEssayid(comment.getEssayid());
        comment.setDate(comment.getDate());
        if(comment.save())
            return true;
        else
            return false;
    }
    public List<Comment> QueryAll(int id){
        List<Comment> comments= DataSupport.findAll(Comment.class);
        List<Comment> results= new ArrayList<>();
        Comment result=new Comment();
        for(Comment comment:comments){
            if(comment.getEssayid()==id) {
                result = comment;
                results.add(result);
            }
        }
        return results;
    }
}
