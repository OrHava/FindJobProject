package com.sce.findjobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class About extends AppCompatActivity {
    private ImageButton btnProfile,btnHome;
    private Button btnReport;
    private int count;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ArrayList<String> Jobs=new ArrayList<>();
    private ArrayList<String> userApplied;
    private int CountJobSeekers;
    private int CountJobRequiter;
    private int CountReports;
    private String JobType;
    private int count_northern_district;
    private int count_haifa_district;
    private int count_tel_aviv_district;
    private int count_central_district;
    private int count_jerusalem_district;
    private int  count_southern_district;
    private int count_judea_and_samaria_district;


    List<pdfClass> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    private void AmountOfJobsLocation(){
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        if(Arrays.asList(0, 1, 9,24,32,36,37,39,40,45,59,60,62,64.67,72).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){

                            count_northern_district++;

                        }
                        else if(Arrays.asList(6, 17, 18,19,26,27,30,33,42,47,66,68).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_haifa_district++;
                        }
                        else if(Arrays.asList(7, 12, 16,20,22,31,48,53,54,63).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_tel_aviv_district++;
                        }
                        else if(Arrays.asList(15,21,25,34,38,41,43,49,50,51,55,56,57,58,61,65,69,70,71).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_central_district++;
                        }
                        else if(Arrays.asList(10,23).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_jerusalem_district++;
                        }
                        else if(Arrays.asList(2,4,5,8,13,14,28,29,44,46,52,60).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_southern_district++;
                        }
                        else if(Arrays.asList(3,11,35).contains(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class))){
                            count_judea_and_samaria_district++;
                        }
                    }
                    Jobs.add("Jobs location:\n");
                    Jobs.add("Northern District: "+count_northern_district+"\n");
                    Jobs.add("Haifa District: "+count_haifa_district+"\n");
                    Jobs.add("Tel Aviv District: "+ count_tel_aviv_district+"\n");
                    Jobs.add("Central District: "+count_central_district+"\n");
                    Jobs.add("Jerusalem District: "+count_jerusalem_district+"\n");
                    Jobs.add("Southern District: "+count_southern_district+"\n");
                    Jobs.add("Judea and Samaria: "+count_judea_and_samaria_district+"\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void AllJobAdminStats() {
        AmountOfUsers();
        AmountOfReports();
        AmountOfJobsLocation();
    }
}