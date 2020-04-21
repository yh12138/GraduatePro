package com.example.utils;

public class Flower {
    private String name;
    private String alias;
    private String branch;
    private String fanmily;
    private String scientific_name;
    private String english_name;
    private String provenance;
    private String reproduction;
    private String stage;
    private String sunshine;
    private String temperature;
    private String soil;
    private String moisture;
    private String character;
    private String ornamental;
    private String img;
    public Flower(){
        super();
    }
    public Flower(String name,String alias,String branch,String fanmily,String scientific_name,String english_name,
                  String provenance,String reproduction,String stage,String sunshine,String temperature,String soil,
                  String moisture,String character,String ornamental,String img){
        super();
        this.name=name;
        this.alias=alias;
        this.branch=branch;
        this.fanmily=fanmily;
        this.scientific_name=scientific_name;
        this.english_name=english_name;
        this.provenance=provenance;
        this.reproduction=reproduction;
        this.stage=stage;
        this.sunshine=sunshine;
        this.temperature=temperature;
        this.soil=soil;
        this.moisture=moisture;
        this.character=character;
        this.ornamental=ornamental;
        this.img=img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFanmily() {
        return fanmily;
    }

    public void setFanmily(String fanmily) {
        this.fanmily = fanmily;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getReproduction() {
        return reproduction;
    }

    public void setReproduction(String reproduction) {
        this.reproduction = reproduction;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getSunshine() {
        return sunshine;
    }

    public void setSunshine(String sunshine) {
        this.sunshine = sunshine;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getOrnamental() {
        return ornamental;
    }

    public void setOrnamental(String ornamental) {
        this.ornamental = ornamental;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
