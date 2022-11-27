package com.sce.findjobproject;

public class pdfClass {


    public String name,url;

    public pdfClass() { //must have for be able to use this class
    }

    public pdfClass(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) { //must have for be able to use this class
        this.url = url;
    }
}
