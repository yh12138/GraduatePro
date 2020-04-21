package com.example.utils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UploadImgDao {
    public Boolean Insert(UploadImg uploadImg){
        uploadImg.setImg(uploadImg.getImg());
        uploadImg.setName(uploadImg.getName());
        uploadImg.setDate(uploadImg.getDate());
        uploadImg.setName(uploadImg.getName());
        if(uploadImg.save())
            return true;
        else
            return false;
    }
    public UploadImg Query(String phone){
        List<UploadImg> uploadImgs= DataSupport.findAll(UploadImg.class);
        UploadImg result=new UploadImg();
        for(UploadImg uploadImg:uploadImgs){
            if(uploadImg.getAuthor().equals(phone)) {
                result = uploadImg;
                break;
            }
        }
        return result;
    }
    public List<UploadImg> QueryAll(){
        List<UploadImg> uploadImgs= DataSupport.findAll(UploadImg.class);

        return uploadImgs;
    }
}
