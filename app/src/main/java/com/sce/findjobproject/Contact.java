package com.sce.findjobproject;

import androidx.annotation.NonNull;


// Class that represents a job posting
public class Contact {
    // The name of the company posting the job
    private final String companyName;
    // The type of job being posted
    private final String jobType;
    // A description of the job
    private final String JobDescription;
    // The location of the job
    private final String JobLocation;
    // Whether the job details are currently expanded in the UI
    private boolean expanded;
    // The date the job was posted
    private final String Date;

    // Constructs a new Contact with the given information
    public Contact(String name, String email, String JobDescription, String JobLocation, String date) {
        this.companyName = name;
        this.jobType = email;
        this.JobDescription = JobDescription;
        this.JobLocation = JobLocation;
        this.Date = date;
        this.expanded = false;
    }
    // Getter for the company name
    public String getCompanyName() {
        return companyName;
    }
    // Getter for the job type
    public String getJobType() {
        return jobType;
    }
    // Getter for the job location
    public String getJobLocation() {
        return JobLocation;
    }
    // Getter for the job description
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
    // Getter for the expanded state
    public boolean isExpanded() {
        return expanded;
    }
    // Setter for the expanded state
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    // Getter for the date
    public String getDate() {
        return Date;
    }

}
