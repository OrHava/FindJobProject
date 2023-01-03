package com.sce.findjobproject;
// Class representing a PDF file
public class pdfClass {

    // Properties of a PDF file: name and URL
    public String name,url;
    // Default constructor, required for class to be used with Firebase Realtime Database
    public pdfClass() { //must have for be able to use this class
    }
    // Constructor that takes name and URL as arguments
    public pdfClass(String name, String url) {
        this.name = name;
        this.url = url;
    }
    // Getter for name property
    public String getName() {
        return name;
    }
    // Setter for name property
    public void setName(String name) {
        this.name = name;
    }
    // Getter for url property
    public String getUrl() {
        return url;
    }
    // Setter for url property
    public void setUrl(String url) { //must have for be able to use this class
        this.url = url;
    }
}
