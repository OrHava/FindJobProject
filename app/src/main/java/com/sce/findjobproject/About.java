package com.sce.findjobproject;
import static com.sce.findjobproject.SignIn.WhichUser;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class About extends AppCompatActivity {
    private ImageButton btnProfile,btnHome;
    private Button btnReport,btnDelete;
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
    private int count_southern_district;
    private int count_judea_and_samaria_district;
    private ImageButton btnPlus1,btnPlus2,btnPlus3,btnPlus4,btnPlus5,btnPlus6,btnPlus7,btnPlus8,btnPlus9,btnPlus10;
    private TextView txtA1,txtA2,txtA3,txtA4,txtA5,txtA6,txtA7,txtA8,txtA9,txtA10;

    List<pdfClass> uploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnProfile=findViewById(R.id.btnProfile);
        btnHome=findViewById(R.id.btnHome);
        btnReport=findViewById(R.id.btnReport);
        btnDelete=findViewById(R.id.btnDelete);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uploads=new ArrayList<>();
        userApplied= new ArrayList<>();
        txtA1 = findViewById(R.id.txtA1);
        btnPlus1 = findViewById(R.id.btnPlus1);
        txtA2 = findViewById(R.id.txtA2);
        btnPlus2 = findViewById(R.id.btnPlus2);
        txtA3 = findViewById(R.id.txtA3);
        btnPlus3 = findViewById(R.id.btnPlus3);
        txtA4 = findViewById(R.id.txtA4);
        btnPlus4 = findViewById(R.id.btnPlus4);
        txtA5 = findViewById(R.id.txtA5);
        btnPlus5 = findViewById(R.id.btnPlus5);
        txtA6 = findViewById(R.id.txtA6);
        btnPlus6 = findViewById(R.id.btnPlus6);
        txtA7 = findViewById(R.id.txtA7);
        btnPlus7 = findViewById(R.id.btnPlus7);
        txtA7 = findViewById(R.id.txtA7);
        btnPlus8 = findViewById(R.id.btnPlus8);
        txtA8 = findViewById(R.id.txtA8);
        btnPlus9 = findViewById(R.id.btnPlus9);
        txtA10 = findViewById(R.id.txtA10);
        btnPlus10 = findViewById(R.id.btnPlus10);

        FAQ();
        EnterButtons();
        CheckWhichUser();
        DeleteAccount();
    }

    private void FAQ() {
        btnPlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.VISIBLE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });


        btnPlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.VISIBLE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.VISIBLE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.VISIBLE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.VISIBLE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.VISIBLE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.VISIBLE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.VISIBLE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.VISIBLE);
                txtA10.setVisibility(View.GONE);
            }
        });

        btnPlus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtA1.setVisibility(View.GONE);
                txtA2.setVisibility(View.GONE);
                txtA3.setVisibility(View.GONE);
                txtA4.setVisibility(View.GONE);
                txtA5.setVisibility(View.GONE);
                txtA6.setVisibility(View.GONE);
                txtA7.setVisibility(View.GONE);
                txtA8.setVisibility(View.GONE);
                txtA9.setVisibility(View.GONE);
                txtA10.setVisibility(View.VISIBLE);
            }
        });




    }

    private void CheckWhichUser() {
        if(WhichUser==1){
            AllJobsSendCvs(); //Function that put all the jobs user applied info, into the ArrayList "Jobs".
            Report("AllJobsOffersFile.txt");
        }
        else if(WhichUser==2){
            AllJobSeekersCvs();
            Report("AllJobsSeekersCvs.txt");

        }
        else if(WhichUser==3){
            AllJobAdminStats();
            Report("AllJobsStatsAdmin.txt");
        }
    }
    private void AmountOfUsers(){
        CountJobRequiter=0;
        CountJobSeekers=0;
        if (user != null) {

            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics");
            databaseReference.addListenerForSingleValueEvent((new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postsnapshot: snapshot.getChildren()){
                        Boolean bool =  postsnapshot.getValue(Boolean.class);

                        if(Boolean.FALSE.equals(bool)){ // User that looking for workers
                            CountJobRequiter++;
                        }
                        else{ // User that looking for a job
                            CountJobSeekers++;
                        }
                    }
                    Jobs.add("Amount of job seekers: "+ CountJobSeekers+ "\n");
                    Jobs.add("Amount of job Requiters: "+ CountJobRequiter+ "\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));

        }
    }
    private void AmountOfReports(){
        if (user != null) {
            CountReports = 0;
            databaseReference = FirebaseDatabase.getInstance().getReference("reports");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Jobs.add("Reports: \n\n");
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        String ReportName = postsnapshot.getValue(String.class);
                        CountReports++;
                    }
                    Jobs.add("Amount of reports: " + CountReports + "\n");
                    Jobs.add("\n\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void AmountOfTypejobs(){ // Not ready
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        JobType= postsnapshot.child("SpinJobsType11Index").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
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


    private void AllJobSeekersCvs(){
        if(user!=null) {
            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(userId).child("cvIds");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                        userApplied.add(Objects.requireNonNull(postsnapshot.getValue(String.class)));
                    }
                    viewAllFiles();

                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }




    }

    private void viewAllFiles(){
        if(user!=null) {
            String userId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(int i=0;i<userApplied.size();i++){
                        for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                            if(userApplied.get(i).equals(postsnapshot.getKey()) && !(userId.equals(postsnapshot.getKey()))){

                                Jobs.add(postsnapshot.child("Uploads").child("url").getValue(String.class));
                            }


                        }
                    }





                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }


    }

    private void AllJobsSendCvs(){

        count=0;
        if(user!=null ) {
            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot postsnapshot2 : postsnapshot.child("cvIds").getChildren()){

                            if (Objects.requireNonNull(postsnapshot2.getValue(String.class)).equals(userId)) {

                                count++;
                                Jobs.add("Job Index "+ count);
                                Jobs.add("Company: "+ postsnapshot.child("Company").getValue(String.class));
                                Jobs.add("Date: "+ postsnapshot.child("Date").getValue(String.class));
                                Jobs.add("JobLocation: "+ postsnapshot.child("JobLocation").getValue(String.class));
                                Jobs.add("JobType: "+ postsnapshot.child("JobType").getValue(String.class));
                                Jobs.add("JobDescription: "+ postsnapshot.child("JobDescription").getValue(String.class));
                                Jobs.add("----------------------------------------");

                            }

                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });
        }



    }

    private void Report(String FileName) {
        btnReport.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(About.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            boolean wasSuccessful;
            String datafilecontect = String.valueOf(Jobs);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            try {
                wasSuccessful = dir.mkdirs();

            }
            catch (Exception e){
                e.printStackTrace();
            }

            File contentfilename = new File(dir, FileName);
            try {
                FileOutputStream stream = new FileOutputStream(contentfilename);
                stream.write(datafilecontect.getBytes());
                Toast.makeText(About.this, "Dir created", Toast.LENGTH_SHORT).show();
                openFile();


            }
            catch (IOException e){
                e.printStackTrace();
            }
        });




    }


    private void openFile() {

        String path = Environment.DIRECTORY_DOCUMENTS ;
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,  "*/*");
        startActivity(intent);
    }



    void EnterButtons(){

        btnProfile.setOnClickListener(view -> startActivity(new Intent(About.this, Profile.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(About.this, Home.class)));

    }

    void DeleteAccount(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // User account deleted successfully
                                    Toast.makeText(About.this, "User account deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(About.this, SignIn.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // Error occurred during delete operation
                                    Toast.makeText(About.this, "Error deleting user account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

}