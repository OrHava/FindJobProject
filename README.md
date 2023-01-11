Overview:
FindJob is an Android application that connects job seekers with job recruiters.
It allows job seekers to upload their resumes, create profiles, search for jobs, and apply for jobs using their resumes. 
Employers can post job listings and view resumes submitted by job seekers.
The application also includes an admin feature that allows for the deletion of reported posts and posts older than 30 days.

Features:
Job seekers can upload their resumes and create profiles
Job seekers can search for jobs and apply for them using their resumes
Job recruiters can post job listings and view resumes submitted by job seekers
Admin feature for deleting reported posts and posts older than 30 days
Users can download a report of their data
Users can delete their account and have their data deleted from the Firebase Realtime Database
Technologies
Java programming language
Android Studio
Firebase (Realtime Database, Cloud, Auth)

Users:
Admin: can delete posts that have been reported and delete posts over 30 days
Job Seeker: can upload their CV, create a profile, search for jobs, and apply for a job with their CV
Job Recruiter: can post jobs and view people who submit their CV to them

Getting Started:
To Connect this project, you need to download the lasted version of Android Studio and Java: https://developer.android.com/studio 
https://www.oracle.com/java/technologies/downloads/
Clone or download the repository
Open the project in Android Studio
Connect the project to your Firebase account- To make sure the code work with cloud, you need to open your own FireBase account and SDK setup and configuration to the code, download google-services.json, and add it to MyApp - > app -> src, and then modify google id String to the new onces you have in your FireBase Ac and then - >
Connect Your perosnal Android studio hash to Firebase: 
    To find hash go to Navigate (its on top bar), Write-> search EveryWhere-> write "gradlew" and enter-> then write click on gradlew ->
     open in-> Terminal -> then write in Terminal- > ./gradlew signingReport, then if everything is fine you should see hash 1 code which is what we need.
Run the project on an emulator or physical device


