package com.sce.findjobproject;

import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.sce.findjobproject.SignIn.WhichUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ImageButton btnHome,btnAbout;
    private TextView txtWhichUser,txtJobDescription;
    private EditText edtName,edtLastName,edtEmail,edtPhone,edtCity,edtCompany,edtJobDescription;
    private Button btnSubmitInfo,BtnUpJobDesc,BtnUpJobApplied,AdminButton,btnDeleteJob,AdminButtonComments;
    private String Name="",LastName="",Email="",Phone="",City="";
    private String Company="",JobDescription="",JobType="",JobLocation="";
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ImageButton btnUpCv;
    private Button btnExit;
    private Dialog dialog,dialog2;
    private Integer SpinJobsLocation1Index,SpinJobsType1Index;
    private TextView SpinJobsType1,SpinJobsLocation1;
    private GoogleSignInClient mGoogleSignInClient;
    private RelativeLayout LayoutPostJob,LayoutProfile,LayoutCv,AdminLayout;
    private RadioButton radio_Male;
    private RadioButton radio_NoBinary;
    private  String[] listItemJobLocations;
    private  String[] listItemJobTypes;
    private RadioButton radio_Female;
    private Integer whichGender=0;
    private TextView txtAmountUsers1,txtAmountUsers2;
    private int countAmountOfJobSeekers=0;
    private int countAmountOfJobRecruiters=0;
    private int count_northern_district=0,count_haifa_district=0,count_tel_aviv_district=0, count_central_district=0,count_jerusalem_district=0,count_southern_district=0,count_judea_and_samaria_district=0;
    private TextView tv_northern_district, tv_haifa_district, tv_tel_aviv_district, tv_central_district,tv_jerusalem_district,tv_southern_district,tv_judea_and_samaria_district;
    private PieChart pieChart;
    private Button AgreeBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Initialize variables for UI elements
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        listItemJobLocations=getResources().getStringArray(R.array.jobs_location);
        listItemJobTypes=getResources().getStringArray(R.array.jobs_types);
        radio_Male=findViewById(R.id.radio_Male);
        radio_NoBinary=findViewById(R.id.radio_NoBinary);
        radio_Female=findViewById(R.id.radio_Female);
        LayoutPostJob=findViewById(R.id.LayoutPostJob);
        btnAbout=findViewById(R.id.btnAbout);
        btnHome=findViewById(R.id.btnHome);
        edtName=findViewById(R.id.edtName);
        AdminButton=findViewById(R.id.AdminButton);
        edtLastName=findViewById(R.id.edtLastName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPhone=findViewById(R.id.edtPhone);
        edtCity=findViewById(R.id.edtCity);
        txtAmountUsers1=findViewById(R.id.txtAmountUsers1);
        txtAmountUsers2=findViewById(R.id.txtAmountUsers2);
        btnSubmitInfo=findViewById(R.id.btnSubmitInfo);
        btnUpCv=findViewById(R.id.btnUpCv);
        edtCompany=findViewById(R.id.edtCompany);
        BtnUpJobDesc=findViewById(R.id.BtnUpJobDesc);
        edtJobDescription=findViewById(R.id.edtJobDescription);
        btnExit=findViewById(R.id.btnExit);
        BtnUpJobApplied=findViewById(R.id.BtnUpJobApplied);
        txtWhichUser=findViewById(R.id.txtWhichUser);
        LayoutProfile=findViewById(R.id.LayoutProfile);
        txtJobDescription=findViewById(R.id.JobDescription);
        SpinJobsType1=findViewById(R.id.SpinJobsType1);
        LayoutCv=findViewById(R.id.LayoutCv);
        AdminLayout=findViewById(R.id.AdminLayout);
        SpinJobsLocation1=findViewById(R.id.SpinJobsLocation1);
        tv_northern_district = findViewById(R.id.tv_northern_district);
        tv_haifa_district = findViewById(R.id.tv_haifa_district);
        tv_tel_aviv_district = findViewById(R.id.tv_tel_aviv_district);
        tv_central_district = findViewById(R.id.tv_central_district);
        tv_jerusalem_district = findViewById(R.id.tv_jerusalem_district);
        tv_southern_district = findViewById(R.id.tv_southern_district);
        tv_judea_and_samaria_district = findViewById(R.id.tv_judea_and_samaria_district);
        pieChart = findViewById(R.id.piechart);
        btnDeleteJob=findViewById(R.id.btnDeleteJob);
        AdminButtonComments=findViewById(R.id.AdminButtonComments);
        AgreeBtn=findViewById(R.id.AgreeBtn);

        allowScrollForEditText();
        CheckWhichUser();
        EnterButtons();
        exitUser();
        SpinnerFuncAdvance();



    }

    private void Agreement(){

        AgreeBtn.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
            alert.setTitle(R.string.Agreement);
            alert.setMessage(R.string.Aggrement_text);
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                RadioButton RadioBtnAgree= findViewById(R.id.RadioBtnAgree);
                RadioBtnAgree.setChecked(true);
                // Dismiss dialog
                dialog.dismiss();

            });
            alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            alert.show();

        });
    }


    private void DeleteJobPost() {
        // Set up click listener for delete button
        btnDeleteJob.setOnClickListener(view -> {
            if(user!=null ) {
                // Get user's unique identifier (userId) from user object
                String userId = user.getUid();
                // Create alert dialog to confirm deletion
                AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
                alert.setTitle(R.string.delete_post);
                alert.setMessage(R.string.are_you_sure);
                alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Set up reference to user's job post in "usersJobs" node
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef3 = database.getReference("usersJobs").child(userId);
                    // Remove value at reference
                    myRef3.getRef().removeValue();
                    // Show snackbar indicating success
                    Snackbar snackbar = Snackbar.make(view, R.string.Files_Delete_successfully, Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                    snackbar.show();
                    // Dismiss dialog
                    dialog.dismiss();

                });
                // Set negative button (cancel) for alert dialog
                alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                // Show alert dialog
                alert.show();
            }

        });


    }

    private void PieChartPie() { //function to build pie of jobs by district



        if(user!=null ) {
            // Get a reference to the Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
            // Add a value event listener to query the database and get the number of job posts for each district
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate over the children of the 'snapshot' object of 'usersJobs'
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
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
                    tv_northern_district.setText(Integer.toString(count_northern_district));
                    tv_haifa_district.setText(Integer.toString(count_haifa_district));
                    tv_tel_aviv_district.setText(Integer.toString(count_tel_aviv_district));
                    tv_central_district.setText(Integer.toString(count_central_district));
                    tv_jerusalem_district.setText(Integer.toString(count_jerusalem_district));
                    tv_southern_district.setText(Integer.toString(count_southern_district));
                    tv_judea_and_samaria_district.setText(Integer.toString(count_judea_and_samaria_district));
                    // Set the data and color to the pie chart
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.northern_district,
                                    Integer.parseInt(tv_northern_district.getText().toString()),
                                    Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.haifa_district,
                                    Integer.parseInt(tv_haifa_district.getText().toString()),
                                    Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.tel_aviv_district,
                                    Integer.parseInt(tv_tel_aviv_district.getText().toString()),
                                    Color.parseColor("#E91E63")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.central_district,
                                    Integer.parseInt(tv_central_district.getText().toString()),
                                    Color.parseColor("#29B6F6")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.jerusalem_district,
                                    Integer.parseInt(tv_jerusalem_district.getText().toString()),
                                    Color.parseColor("#673AB7")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.southern_district,
                                    Integer.parseInt(tv_southern_district.getText().toString()),
                                    Color.parseColor("#FF5722")));
                    pieChart.addPieSlice(
                            new PieModel(
                                    ""+R.string.judea_and_samaria_district,
                                    Integer.parseInt(tv_judea_and_samaria_district.getText().toString()),
                                    Color.parseColor("#009688")));

                    // To animate the pie chart
                    pieChart.startAnimation();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });



        }



    }


    private void AdminProfile() { //function to know amount of users and their type.



        if(user!=null ) {
            // Get a reference to the Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics");
            // Add a value event listener to query the database and get amount of users and their type.
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate over the children of the 'postsnapshot' object of 'AdminStatistics'
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        // Check which user type he is and increment the appropriate counter
                        if (Objects.equals(postsnapshot.getValue(boolean.class), false)) {
                            countAmountOfJobRecruiters++;

                        }
                        else {
                            countAmountOfJobSeekers++;
                        }


                    }
                    //display in set text the info.
                    txtAmountUsers1.setText(getString(R.string.Amount_of_Job_Recruiters)+" " + countAmountOfJobRecruiters);
                    txtAmountUsers2.setText(getString(R.string.Amount_Of_Job_Seekers)+ " " +countAmountOfJobSeekers );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });



        }


    }


    @SuppressLint("ClickableViewAccessibility")
    private void allowScrollForEditText(){ //function that allow scroll in edit text.
        txtJobDescription.setOnTouchListener((v, event) -> {
            if (txtJobDescription.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });
    }





    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) { //function that display which button in the radiobutton to be clicked of user gender.
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_Male:
                if (checked)
                    whichGender=0;
                radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLUE));
                radio_Female.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                break;
            case R.id.radio_Female:
                if (checked)
                    whichGender=1;
                radio_Female.setButtonTintList(ColorStateList.valueOf(Color.RED));
                radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                break;
            case R.id.radio_NoBinary:
                if (checked)
                    whichGender=2;
                radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
                radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                radio_Female.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                break;

        }

    }
    private void CheckWhichUser() {//check which user and start each function that user need.
        // Check value of WhichUser variable
        if(WhichUser==1){
            // If WhichUser is 1, set text for txtWhichUser and hide layouts of admin and post job.
            txtWhichUser.setText(R.string.Your_Looking_For_Jobs);
            AdminLayout.setVisibility(View.GONE);
            LayoutPostJob.setVisibility(View.GONE);
            uploadCv();
            EnterInfoProfile();

        }
        else if(WhichUser==2){
            // If WhichUser is 2, set text for txtWhichUser hide layouts of admin and cv upload.
            txtWhichUser.setText(R.string.Your_Looking_For_Employees);
            AdminLayout.setVisibility(View.GONE);
            LayoutCv.setVisibility(View.GONE);
            EnterInfoJob();
            EnterInfoProfile();
            DeleteJobPost();
        }
        else if(WhichUser==3){
            // If WhichUser is 3, set text for txtWhichUser  and hide LayoutProfile,LayoutPostJob,LayoutCv.
            txtWhichUser.setText(R.string.Your_Manager_of_The_App);
            LayoutProfile.setVisibility(View.GONE);
            LayoutPostJob.setVisibility(View.GONE);
            LayoutCv.setVisibility(View.GONE);
            ClearJobs();
            AdminProfile();
            PieChartPie();
            EnterComments();

        }
    }

    private void EnterComments() { //function that take you to different layout to see comments of admins.
        AdminButtonComments.setOnClickListener(view -> startActivity(new Intent(Profile.this, AdminComments.class)));
    }

    private void ClearJobs() { //function that allow admin delete comments that are over 30 days.
        AdminButton.setOnClickListener(view -> {
            // Check if user object is not null
            if(user!=null ) {
                //start alert dialog that ask user admin if he sure he want to delete jobs over 30 days.

                AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
                alert.setTitle(R.string.delete_post);
                alert.setMessage(R.string.Are_you_sure_2);
                alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                    database = FirebaseDatabase.getInstance();
                    // Set up reference to "usersJobs" node in the database and add single value event listener
                    DatabaseReference myRef3 = database.getReference("usersJobs");
                    myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Iterate through each child (user job) in the "usersJobs" node
                            for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                String dateString = postsnapshot.child("Date").getValue(String.class); //get the date of job post.
                                if (dateString == null) {
                                    continue;
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); //get job of today.
                                Date inputDate = null;
                                try {
                                    inputDate = sdf.parse(dateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String currentDate2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                Date inputDate2 = null;
                                try {
                                    inputDate2 = sdf.parse(currentDate2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long difference = 0;
                                if (inputDate2 != null) {
                                    if (inputDate != null) {
                                        difference = (inputDate2.getTime()-inputDate.getTime() ) / (1000 * 60 * 60 * 24); // calc if the different between two dates is 30 days.
                                    }
                                }
                                // Check if job date is over 30 days.
                                if(difference>30){

                                    postsnapshot.getRef().removeValue();

                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                        }
                    });

                    dialog.dismiss();

                });

                alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

                alert.show();
            }

        });
    }public void exitUser(){ //function that allow user leave the main layouts and go back to 'signin' layout.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //get the user from firebase.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this); //get the user from firebase of google ac.
        if(acct!=null || user != null){ //check if google account or firebase account are not NUll.


            btnExit.setOnClickListener(view -> {
                WhichUser=1;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.Google_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(Profile.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(Profile.this,
                        task -> {
                            // firebase sign out

                            Log.d("User Exit: ","successfully!");
                            getInstance().signOut();
                            //go to SignIn Layout.

                            Intent intent = new Intent(Profile.this, SignIn.class);
                            startActivity(intent);
                            finish();

                        });

            });

        }





    }




    private void SpinnerFuncAdvance() { // function explained already in Home Class. that help user job recruiter to choose job type and location.



        SpinJobsLocation1.setOnClickListener(view -> {
            dialog2= new Dialog((Profile.this));
            dialog2.setContentView(R.layout.dialog_searchable_spinner);

            dialog2.getWindow().setLayout(650,800);

            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog2.show();

            EditText editText= dialog2.findViewById(R.id.edit_text);
            ListView listView=dialog2.findViewById(R.id.list_view);


            ArrayAdapter<String> adapter= new ArrayAdapter<String>(Profile.this,
                    android.R.layout.simple_list_item_1,
                    listItemJobLocations){


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    TextView text = view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);


                    return view;
                }
            };

            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                SpinJobsLocation1.setText(adapter.getItem(i));

                for(int j = 0; i<listItemJobLocations.length-1;j++){
                    if(listItemJobLocations[j].equals(adapter.getItem(i))){
                        SpinJobsLocation1Index=j;
                        break;
                    }
                }
                dialog2.dismiss();
            });

        });

        SpinJobsType1.setOnClickListener(view -> {
            dialog= new Dialog((Profile.this));
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            dialog.getWindow().setLayout(650,800);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();

            EditText editText= dialog.findViewById(R.id.edit_text);
            ListView listView=dialog.findViewById(R.id.list_view);


            ArrayAdapter<String> adapter= new ArrayAdapter<String>(Profile.this,
                    android.R.layout.simple_list_item_1,
                    listItemJobTypes){


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    TextView text = view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);


                    return view;
                }
            };

            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            listView.setOnItemClickListener((adapterView, view12, i, l) -> {
                SpinJobsType1.setText(adapter.getItem(i));
                for(int j = 0; i<listItemJobTypes.length-1;j++){
                    if(listItemJobTypes[j].equals(adapter.getItem(i))){
                        SpinJobsType1Index=j;
                        break;
                    }
                }


                dialog.dismiss();
            });

        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    void uploadCv(){ //function that allow user job seeker to go to layout 'UpLoadPDF' to upload his CV.



        btnUpCv.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),UpLoadPDF.class);
            startActivity(intent);



        });


    }

    void EnterInfoProfile(){ //function that allow job seeker and job recruiter enter personal info.
        // Check if user object is not null
        if(user!=null){
            database = FirebaseDatabase.getInstance();
            String userId = user.getUid();
            // Set up reference to "users" node in the database and add single value event listener
            DatabaseReference myRef3 = database.getReference("users").child(userId);

            myRef3.addValueEventListener(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check child of user exist and if its exist that all info and set the text to it.
                    if(dataSnapshot.exists()){

                        if(dataSnapshot.child("Name").getValue()!=null){
                            String TheName=  dataSnapshot.child("Name").getValue(String.class);
                            edtName.setText(TheName);
                        }
                        if(dataSnapshot.child("LastName").getValue()!=null){
                            String TheLastName=  dataSnapshot.child("LastName").getValue(String.class);
                            edtLastName.setText(TheLastName);

                        }
                        if(dataSnapshot.child("Email").getValue()!=null){
                            String TheEmail=  dataSnapshot.child("Email").getValue(String.class);
                            edtEmail.setText(TheEmail);
                        }
                        if(dataSnapshot.child("Phone").getValue()!=null){
                            String ThePhone= dataSnapshot.child("Phone").getValue(String.class);
                            edtPhone.setText(ThePhone);

                        }
                        if(dataSnapshot.child("City").getValue()!=null){
                            String TheCity=  dataSnapshot.child("City").getValue(String.class);
                            edtCity.setText(TheCity);

                        }
                        if(dataSnapshot.child("GenderIndex").getValue(Integer.class)!=null){
                            Integer TheGender=  dataSnapshot.child("GenderIndex").getValue(Integer.class);
                            setGender(TheGender);

                        }









                    }


                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Failed to read value

                }
            });


            btnSubmitInfo.setOnClickListener(view -> {
                // save info of user he put for the first time or edit to firebase database.
                Name=edtName.getText().toString();
                LastName=edtLastName.getText().toString();
                Email=edtEmail.getText().toString();
                Phone=edtPhone.getText().toString();
                City=edtCity.getText().toString();
                edtName.clearFocus();
                edtLastName.clearFocus();
                edtEmail.clearFocus();
                edtPhone.clearFocus();
                edtCity.clearFocus();

                if (user != null) {
                    String userId1 = user.getUid();
                    writeNewUser(userId1,Name,LastName,Email,Phone,City,whichGender);
                    Snackbar snackbar = Snackbar.make(view, R.string.Success_uploading_profile, Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                    snackbar.show();
                }


            });


        }
        else{
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show();

        }

    }


    void setGender(Integer TheGender){ //function that help to set gender of user.
        if(TheGender==0){
            radio_Male.setChecked(true);
            radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLUE));
            radio_Female.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        }
        else if(TheGender==1){
            radio_Female.setChecked(true);
            radio_Female.setButtonTintList(ColorStateList.valueOf(Color.RED));
            radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        }
        else if(TheGender==2){
            radio_NoBinary.setChecked(true);
            radio_NoBinary.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
            radio_Male.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            radio_Female.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        }
    }


    void EnterInfoJob(){ //function that enter info of job.

        Agreement();//A Way for user to agree to the agreements.
        // Check if user object is not null
        if(user!=null){
            database = FirebaseDatabase.getInstance();
            String userId = user.getUid();
            // Set up reference to "usersJobs" node in the database and add single value event listener
            DatabaseReference myRef3 = database.getReference("usersJobs").child(userId);

            myRef3.addValueEventListener(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check child of user exist and if its exist that all info and set the text to it.
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("Company").getValue()!=null){
                            String TheCompany=  dataSnapshot.child("Company").getValue(String.class);
                            edtCompany.setText(TheCompany);

                        }

                        if(dataSnapshot.child("JobDescription").getValue()!=null){
                            String TheJobDescription=  dataSnapshot.child("JobDescription").getValue(String.class);
                            edtJobDescription.setText(TheJobDescription);
                        }


                        if(dataSnapshot.child("SpinJobsLocation11Index").getValue(Integer.class)!=null){
                            Integer TheJobLocationIndex=  dataSnapshot.child("SpinJobsLocation11Index").getValue(Integer.class);
                            if (TheJobLocationIndex != null) {
                                SpinnerHelperJobTypLocationIndex(TheJobLocationIndex);
                            }
                            SpinJobsLocation1Index=TheJobLocationIndex;

                        }
                        if(dataSnapshot.child("SpinJobsType11Index").getValue(Integer.class)!=null){
                            Integer TheJobTypeIndex=  dataSnapshot.child("SpinJobsType11Index").getValue(Integer.class);
                            if (TheJobTypeIndex != null) {
                                SpinnerHelperJobTypeIndex(TheJobTypeIndex);
                            }
                            SpinJobsType1Index=TheJobTypeIndex;

                        }






                    }


                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

            RadioButton RadioBtnAgree= findViewById(R.id.RadioBtnAgree);

                BtnUpJobDesc.setOnClickListener(view -> {
                    if(RadioBtnAgree.isChecked()){

                        // save info of user he put for the first time or edit to firebase database.
                        Company=edtCompany.getText().toString();
                        JobDescription=edtJobDescription.getText().toString();
                        JobType=SpinJobsType1.getText().toString();
                        JobLocation=SpinJobsLocation1.getText().toString();
                        edtCompany.clearFocus();
                        edtJobDescription.clearFocus();
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                        if (user != null) {
                            String userId1 = user.getUid();
                            writeNewJob(userId1,Company,JobDescription,JobType,JobLocation,SpinJobsLocation1Index,SpinJobsType1Index,currentDate);
                            Snackbar snackbar = Snackbar.make(view, R.string.Job_Posted_Successfully, Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                            snackbar.show();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(view, R.string.user_is_null, Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                            snackbar.show();
                        }


                    }
                    else{
                        Snackbar snackbar = Snackbar.make(view, R.string.Agree, Snackbar.LENGTH_SHORT);
                        snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                        snackbar.show();
                    }



                });






        }


    }





    private void SpinnerHelperJobTypeIndex(Integer JobType1){ //function that help spinner of job type to set the text by index
        SpinJobsType1.setText(listItemJobTypes[JobType1]);


    }



    private void SpinnerHelperJobTypLocationIndex(Integer JobLocation1){  //function that help spinner of job Location to set the text by index
        SpinJobsLocation1.setText(listItemJobLocations[JobLocation1]);
    }




    public void writeNewUser(String userId, String Name, String LastName,String Email,String Phone,String City, Integer Gender) { //function that create new node child of user to database.
        User user = new User(Name, LastName, Email, Phone, City, Gender);

        mDatabase.child("users").child(userId).setValue(user);
    }
//function that create new node child of userJob to database.
    public void writeNewJob(String userId,String Company, String JobDescription,String JobType,String JobLocation, Integer SpinJobsLocation11Index, Integer SpinJobsType11Index, String Date) {
        JobSubmit jobSubmit = new JobSubmit(Company, JobDescription, JobType, JobLocation,SpinJobsLocation11Index, SpinJobsType11Index, Date);

        mDatabase.child("usersJobs").child(userId).setValue(jobSubmit);
    }


    @IgnoreExtraProperties //class for user type.
    public static class User {

        public String Name;
        public String LastName;
        public String Email;
        public String Phone;
        public String City;
        public Integer GenderIndex;

        public User(String Name, String LastName,String Email,String Phone,String City, Integer Gender) {
            this.Name = Name;
            this.LastName = LastName;
            this.Email = Email;
            this.Phone = Phone;
            this.City = City;
            this.GenderIndex = Gender;
        }

    }
    //class for job post type.
    @IgnoreExtraProperties
    public static class JobSubmit {

        public String Company;
        public String JobDescription;
        public String JobType;
        public String JobLocation;
        public Integer SpinJobsLocation11Index;
        public Integer SpinJobsType11Index;
        public String Date;

        public JobSubmit(String Company, String JobDescription,String JobType,String JobLocation,Integer SpinJobsLocation11Index, Integer SpinJobsType11Index, String Date) {
            this.Company = Company;
            this.SpinJobsLocation11Index=SpinJobsLocation11Index;
            this.SpinJobsType11Index=SpinJobsType11Index;
            this.JobDescription = JobDescription;
            this.JobType = JobType;
            this.JobLocation = JobLocation;
            this.Date = Date;

        }

    }




    void EnterButtons(){ // Sets up the onClickListeners for the navigation buttons
        BtnUpJobApplied.setOnClickListener(view -> startActivity(new Intent(Profile.this, ViewCvsApplys.class)));
        btnAbout.setOnClickListener(view -> startActivity(new Intent(Profile.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(Profile.this, Home.class)));

    }

}