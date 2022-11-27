package com.sce.findjobproject;

import androidx.annotation.NonNull;

public class Contact {
    private final String companyName;
    private final String jobType;
    private final String JobDescription;
    private final String JobLocation;
    private boolean expanded;
    private final String Date;


    public Contact(String name, String email, String JobDescription, String JobLocation, String date) {
        this.companyName = name;
        this.jobType = email;
        this.JobDescription = JobDescription;
        this.JobLocation = JobLocation;
        this.Date = date;
        this.expanded = false;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public String getJobLocation() {
        return JobLocation;
    }

    public String getJobDescription() {
        return JobDescription;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "companyName='" + companyName + '\'' +
                ", jobType='" + jobType + '\'' +
                ", JobLocation='" + JobLocation + '\'' +
                ", JobDescription='"+JobDescription
                ;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDate() {
        return Date;
    }

}
