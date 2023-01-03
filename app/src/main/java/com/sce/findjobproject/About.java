package com.sce.findjobproject;

import static com.google.firebase.auth.FirebaseAuth.getInstance;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
    private final ArrayList<String> Jobs=new ArrayList<>();
    private ArrayList<String> userApplied;
    private int CountJobSeekers;
    private int CountJobRequiter;
    private GoogleSignInClient mGoogleSignInClient;
    private int count_northern_district;
    private int count_haifa_district;
    private int count_tel_aviv_district;
    private int count_central_district;
    private int count_jerusalem_district;
    private int count_southern_district;
    private int count_judea_and_samaria_district;
    private ImageButton btnPlus1,btnPlus2,btnPlus3;
    private TextView txtA1,txtA2,txtA3;


    List<pdfClass> uploads;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize variables for UI elements
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
        FAQ();
        EnterButtons();
        CheckWhichUser();
        DeleteAccount();
    }

    private void FAQ() {
        btnPlus1.setOnClickListener(view -> {
            txtA1.setVisibility(View.VISIBLE);
            txtA2.setVisibility(View.GONE);
            txtA3.setVisibility(View.GONE);


        });


        btnPlus2.setOnClickListener(view -> {
            txtA2.setVisibility(View.VISIBLE);
            txtA1.setVisibility(View.GONE);
            txtA3.setVisibility(View.GONE);


        });

        btnPlus3.setOnClickListener(view -> {
            txtA3.setVisibility(View.VISIBLE);
            txtA1.setVisibility(View.GONE);
            txtA2.setVisibility(View.GONE);
        });




    }

    private void CheckWhichUser() {
        // Check the value of 'WhichUser'
        if(WhichUser==1){
            // Call the 'AllJobsSendCvs' function
            AllJobsSendCvs();
            // Write the contents of the 'Jobs' list to a file called 'AllJobsOffersFile.txt'
            Report("AllJobsOffersFile.txt");
        }
        else if(WhichUser==2){
            // Call the 'AllJobSeekersCvs' function
            AllJobSeekersCvs();
            // Write the contents of the 'Jobs' list to a file called 'AllJobsSeekersCvs.txt'
            Report("AllJobsSeekersCvs.txt");
        }
        else if(WhichUser==3){
            // Call the 'AllJobAdminStats' function
            AllJobAdminStats();
            // Write the contents of the 'Jobs' list to a file called 'AllJobsStatsAdmin.txt'
            Report("AllJobsStatsAdmin.txt");
        }
    }


    private void AmountOfUsers(){
        // Initialize counters for job requiters and job seekers
        CountJobRequiter=0;
        CountJobSeekers=0;
        // Check if the 'user' object is not null
        if (user != null) {
            // Get a reference to the 'AdminStatistics' node of the Firebase database
            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics");
            // Add a single event listener to the 'databaseReference' object
            databaseReference.addListenerForSingleValueEvent((new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Iterate over the children of the 'snapshot' object
                    for (DataSnapshot postsnapshot: snapshot.getChildren()){
                        // Get the boolean value of the current child node
                        Boolean bool =  postsnapshot.getValue(Boolean.class);
                        // Increment the appropriate counter depending on the value of 'bool'
                        if(Boolean.FALSE.equals(bool)){ // User that looking for workers
                            // User is a job requiter
                            CountJobRequiter++;
                        }
                        else{ // User that looking for a job
                            CountJobSeekers++;
                            CountJobSeekers++;
                        }
                    }
                    // Add the counts to the 'Jobs' list as strings
                    Jobs.add("Amount of job seekers: "+ CountJobSeekers+ "\n");
                    Jobs.add("Amount of job Requiters: "+ CountJobRequiter+ "\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // This method is called if the listener is cancelled for any reason
                    // (e.g. if the listener is removed or the database connection is lost)

                }
            }));

        }
    }

    private void AmountOfReports(){
        // Check if the 'user' object is not null
        if (user != null) {
            // Get a reference to the 'reports' node of the Firebase database
            databaseReference = FirebaseDatabase.getInstance().getReference("reports");

            // Add a single event listener to the 'databaseReference' object
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Add the string "Reports: " to the 'Jobs' list
                    Jobs.add("Reports: \n\n");
                    // Add the number of children (i.e. reports) in the 'reports' node to the 'Jobs' list
                    Jobs.add("Amount of reports: " +  snapshot.getChildrenCount() + "\n");
                    // Add a newline character to the 'Jobs' list
                    Jobs.add("\n\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // This method is called if the listener is cancelled for any reason
                    // (e.g. if the listener is removed or the database connection is lost)
                    // It doesn't appear to do anything in this implementation
                }
            });
        }
    }

//    private void AmountOfTypejobs(){ // Not ready
//        if (user != null) {
//            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//    }
    private void AmountOfJobsLocation(){
        // Check if the 'user' object is not null
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            // Get a reference to the 'usersJobs' node of the Firebase database
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                // Add a single event listener to the 'databaseReference' object
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Iterate over the children of the 'snapshot' object of 'usersJobs'
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        // Check which district the job belongs to and increment the appropriate counter
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
                    // Add the counts for each district to the 'Jobs' list as strings
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
        // Check if the 'user' object is not null
        if(user!=null) {
            // Get the user's unique ID
            String userId = user.getUid();
            // Get a reference to the 'cvIds' child node of the 'usersJobs' node for this user
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(userId).child("cvIds");
            // Add a single event listener to the 'databaseReference' object
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate over the children of the 'dataSnapshot' object
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                        // Add the value of the current child node to the 'userApplied' list
                        userApplied.add(Objects.requireNonNull(postsnapshot.getValue(String.class)));
                    }
                    // Call the 'viewAllFiles' function
                    viewAllFiles();

                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // This method is called if the listener is cancelled for any reason
                    // (e.g. if the listener is removed or the database connection is lost)
                    // It doesn't appear to do anything in this implementation

                }
            });

        }




    }

    private void viewAllFiles(){
        // Check if the 'user' object is not null
        if(user!=null) {
            // Get the user's unique ID
            String userId = user.getUid();
            // Get a reference to the 'Uploads' node of the Firebase database


            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
            // Add a value event listener to the 'databaseReference' object
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate over the elements in the 'userApplied' list
                    for(int i=0;i<userApplied.size();i++){
                        // Iterate over the children of the 'dataSnapshot' object
                        for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                            // Check if the key of the current child node matches the current element in the 'userApplied' list
                            // and if the user's ID does not match the key
                            if(userApplied.get(i).equals(postsnapshot.getKey()) && !(userId.equals(postsnapshot.getKey()))){
                                // Add the value of the 'url' child node of the 'Uploads' child node to the 'Jobs' list
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
        // Initialize count to 0
        count=0;
        // Check if the 'user' object is not null
        if(user!=null ) {
            // Get the user's unique ID
            String userId = user.getUid();
            // Get a reference to the 'usersJobs' node of the Firebase database
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            // Add a value event listener to the 'databaseReference' object
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate over the children of the 'dataSnapshot' object
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        // Iterate over the children of the 'cvIds' child node of the current 'postsnapshot' object
                        for (DataSnapshot postsnapshot2 : postsnapshot.child("cvIds").getChildren()){
                            // Check if the value of the current 'postsnapshot2' object matches the user's ID

                            if (Objects.requireNonNull(postsnapshot2.getValue(String.class)).equals(userId)) {
                                // Increment count by 1
                                count++;
                                // Add job information to the 'Jobs' list
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
        // Set an onClickListener on the 'btnReport' object
        btnReport.setOnClickListener(view -> {
            // Request write permissions from the user
            ActivityCompat.requestPermissions(About.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            boolean wasSuccessful;
            // Convert the contents of the 'Jobs' list to a string
            String datafilecontect = String.valueOf(Jobs);
            // Get the public documents directory
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            try {
                // Attempt to create the directory
                wasSuccessful = dir.mkdirs();

            }
            catch (Exception e){
                // Print the stack trace if an exception was thrown
                e.printStackTrace();
            }
            // Create a new file in the 'dir' directory with the specified file name
            File contentfilename = new File(dir, FileName);
            try {
                // Create a FileOutputStream for the 'contentfilename' file
                FileOutputStream stream = new FileOutputStream(contentfilename);
                // Write the contents of 'datafilecontect' to the file
                stream.write(datafilecontect.getBytes());
                // Display a toast message indicating that the directory was created
                Toast.makeText(About.this, "Dir created", Toast.LENGTH_SHORT).show();
                // Call the 'openFile' method
                openFile();


            }
            catch (IOException e){
                // Print the stack trace if an exception
                e.printStackTrace();
            }
        });




    }


    private void openFile() {
        // Get the path to the documents directory
        String path = Environment.DIRECTORY_DOCUMENTS ;
        // Convert the path to a Uri object
        Uri uri = Uri.parse(path);
        // Create a new Intent with the ACTION_VIEW action
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Set the data and type for the intent
        intent.setDataAndType(uri,  "*/*");
        // Start the activity using the intent
        startActivity(intent);
    }


    // Sets up the onClickListeners for the navigation buttons
    void EnterButtons(){
        // Set an OnClickListener for the 'btnProfile' button
        // Start the 'Profile' activity when the button is clicked
        btnProfile.setOnClickListener(view -> startActivity(new Intent(About.this, Profile.class)));
        // Set an OnClickListener for the 'btnHome' button
        // Start the 'Home' activity when the button is clicked
        btnHome.setOnClickListener(view -> startActivity(new Intent(About.this, Home.class)));

    }


    // Function to delete user's account
    void DeleteAccount(){
        // Set on click listener for delete button

        btnDelete.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Assign the current user to a final variable to be used in inner class
            user= FirebaseAuth.getInstance().getCurrentUser();
            // Create an alert dialog to confirm account deletion
            AlertDialog.Builder alert = new AlertDialog.Builder(About.this);
            alert.setTitle(R.string.Delete_Account); // Set dialog title
            alert.setMessage(R.string.delete_account); // Set dialog message
            // Assign final user to a new variable to be used in inner class
            FirebaseUser finalUser = user;
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                // If the user is not null, proceed with deleting the account

                if (finalUser != null){
                    // Get the user's unique ID
                    String userId = finalUser.getUid();
                    // Delete the user's account
                    finalUser.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // User account deleted successfully
                                    WhichUser=1;
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef3 = database.getReference("AdminStatistics").child(userId);
                                    myRef3.getRef().removeValue();
                                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestIdToken(getString(R.string.Google_id))
                                            .requestEmail()
                                            .build();
                                    mGoogleSignInClient = GoogleSignIn.getClient(About.this, gso);
                                    mGoogleSignInClient.signOut().addOnCompleteListener(About.this,
                                            task2 -> {
                                                // firebase sign out
                                                getInstance().signOut();

                                                Intent intent = new Intent(About.this, SignIn.class);
                                                startActivity(intent);
                                                finish();

                                            });

                                } else {
                                    // Error occurred during delete operation
                                    Toast.makeText(About.this, "Error deleting user account", Toast.LENGTH_SHORT).show();
                                }
                            });


                }

                dialog.dismiss();

            });

            alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

            alert.show();





        });
    }

}