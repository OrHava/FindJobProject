package com.sce.findjobproject;
import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.sce.findjobproject.SignIn.WhichUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Home extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private TextView txtWhichUser,SpinJobsType,SpinJobsLocation;
    private ImageButton btnProfile,btnAbout,BtnSearch;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    //private  StorageReference storageReference;
    private RecyclerView contactsRecView;
    private String[] CompanyId;
    private LottieAnimationView searchAnim;
    private Button btnAdmin;
    private int amountofJobs=0;
    private int i=0;
    private int count=0;
    public  int count2=0;
    private  String[] listItemJobLocations;
    private  String[] listItemJobTypes;
    boolean haveCvOrNo;
    private Dialog dialog,dialog2;
    private long mLastClickTime = 0;
    private final List<String> JobInfo = new ArrayList<>();
    private List<String> CompanyIdWithReports = new ArrayList<>();
    private  ArrayList<String> CompanyReasons = new ArrayList<>();
    private Integer SpinJobsLocation1Index,SpinJobsType1Index;

    private String JobTypeSearch="",LocationTypeSearch="";
    //to scan websites you need to Parsing competitive websites for more jobs. using google api
    //Data of citys from: https://www.science.co.il/municipal/Cities.php
    //To find hash go to Navigate (its on top bar), Write-> search EveryWhere-> write "gradlew" and enter-> then write click on gradlew ->
    // open in-> Terminal -> then write in Terminal- > ./gradlew signingReport, then if everything is fine you should see hash 1 code which is what we need.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        contactsRecView=findViewById(R.id.contactsRecView);
        btnProfile=findViewById(R.id.btnProfile);
        btnAbout=findViewById(R.id.btnAbout);
        txtWhichUser=findViewById(R.id.txtWhichUser);
        BtnSearch=findViewById(R.id.BtnSearch);
        searchAnim=findViewById(R.id.searchAnim);
        SpinJobsType=findViewById(R.id.SpinJobsType);
        SpinJobsLocation=findViewById(R.id.SpinJobsLocation);
        btnAdmin=findViewById(R.id.btnAdmin);
        listItemJobLocations=getResources().getStringArray(R.array.jobs_location);
        listItemJobTypes=getResources().getStringArray(R.array.jobs_types);
        user= FirebaseAuth.getInstance().getCurrentUser();
        EnterButtons();
        CheckWhichUser();


    }

    private void AdminSearchHelper() {



        if(user!=null) {
            CompanyIdWithReports = new ArrayList<>();
            CompanyReasons = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("reports");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){

                        CompanyIdWithReports.add(postsnapshot.getKey());
                        if(Objects.equals(postsnapshot.getValue(String.class), "1")){
                            CompanyReasons.add(getString(R.string.discrimination));
                        }
                        else if(Objects.equals(postsnapshot.getValue(String.class), "2")){
                            CompanyReasons.add(getString(R.string.violence_content));
                        }
                        else if(Objects.equals(postsnapshot.getValue(String.class), "3")){
                            CompanyReasons.add(getString(R.string.wrong_information));
                        }

                        else if(Objects.equals(postsnapshot.getValue(String.class), "4")){
                            CompanyReasons.add(getString(R.string.disturbing_or_offensive));
                        }

                        else if(Objects.equals(postsnapshot.getValue(String.class), "5")){
                            CompanyReasons.add(getString(R.string.violation_of_intellectual_property_or_other_law));
                        }

                        else if(Objects.equals(postsnapshot.getValue(String.class), "6")){
                            CompanyReasons.add(getString(R.string.other));
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



    private void AdminSearch() {

        BtnSearch.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            AdminSearchHelper();
            mLastClickTime = SystemClock.elapsedRealtime();
            amountofJobs=0;
            ArrayList<Contact> contacts=new ArrayList<>();
            searchAnim.playAnimation();
            if(user!=null) {

                databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(int j = 0; j<CompanyIdWithReports.size(); j++){
                            for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){

                                if(Objects.equals(postsnapshot.getKey(), CompanyIdWithReports.get(j)) ){
                                    String CompanyName= postsnapshot.child("Company").getValue(String.class);
                                    String Date= postsnapshot.child("Date").getValue(String.class);
                                    String JobDescription= postsnapshot.child("JobDescription").getValue(String.class);
                                    String JobTypeSearch= postsnapshot.child("JobType").getValue(String.class);
                                    String LocationTypeSearch= postsnapshot.child("JobLocation").getValue(String.class);
                                    contacts.add(new Contact(CompanyName,JobTypeSearch,JobDescription,LocationTypeSearch, Date));
                                    JobInfo.add(contacts.get(amountofJobs).toString());
                                    amountofJobs++;
                                    ContactsRecViewAdapter adapter= new ContactsRecViewAdapter(Home.this);
                                    adapter.setContacts(contacts);
                                    adapter.setCompanyReasons(CompanyReasons);
                                    contactsRecView.setAdapter(adapter);
                                    contactsRecView.setLayoutManager(new LinearLayoutManager(Home.this));

                                }
                            }

                        }


                        searchAnim.cancelAnimation();
                        if( amountofJobs==0){
                            Toast.makeText(Home.this,""+ getString(R.string.Job_Not_Found), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            txtWhichUser.setText(getString(R.string.We_Found)+amountofJobs+ getString(R.string.with_reports_on_them));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value

                    }
                });









            }




        });

    }
    public void DeletePostAdmin(int index){
        AtomicBoolean DeletePostOrNot = new AtomicBoolean(false);
        if(WhichUser ==3){
            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            alert.setTitle(R.string.delete_post);
            alert.setMessage(R.string.are_you_sure);
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("usersJobs").child(CompanyIdWithReports.get(index));
                myRef3.getRef().removeValue();
                DatabaseReference myRef4 = database.getReference("reports").child(CompanyIdWithReports.get(index));
                myRef4.getRef().removeValue();
                DeletePostOrNot.set(true);
                Toast.makeText(Home.this, R.string.Files_Delete_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            });

            alert.setNegativeButton(R.string.no, (dialog, which) -> {
                DeletePostOrNot.set(false);

                dialog.dismiss();
            });

            alert.show();



        }

    }





    public static int CheckWhichUserForRecView() {
        if(WhichUser==1){
            return 1;
        }
        else if(WhichUser==2){
            return 2;
        }
        else if(WhichUser==3){

            return 3;
        }
        return 0;
    }
    void CheckWhichUser() {
        boolean whichUserBoolean = false;
        if(WhichUser==1){
            txtWhichUser.setText(R.string.Your_Looking_For_Jobs);
            whichUserBoolean=true;
            checkIfUserHaveCV();
            Search();
        }
        else if(WhichUser==2){
            txtWhichUser.setText(R.string.Your_Looking_For_Employees);
            checkIfUserHaveCV();
            Search();
        }
        else if(WhichUser==3){
            txtWhichUser.setText(R.string.Your_Manager_of_The_App);
            btnAdmin.setVisibility(View.VISIBLE);
            AdminSearch();

        }

        if(user!=null ) {

            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics").child(userId);
            databaseReference.setValue(whichUserBoolean);

        }
    }


    void EnterButtons(){

        btnProfile.setOnClickListener(view -> startActivity(new Intent(Home.this, Profile.class)));

        btnAbout.setOnClickListener(view -> startActivity(new Intent(Home.this, About.class)));

    }






}