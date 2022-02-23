package com.example.socialx.Model;

public class newsModel {

    private String title;
    private String description;
    private String name;
    private String urlToImage;
    private String time;


    public newsModel(String title, String description, String name, String urlToImage, String  time) {
        this.title = title;
        this.description = description;
        this.name = name;
        this.urlToImage = urlToImage;
        this.time= time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
