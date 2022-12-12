package com.sce.findjobproject;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profile extends AppCompatActivity {
    private ImageButton btnHome,btnAbout;
    private TextView txtWhichUser,txtJobDescription;
    private EditText edtName,edtLastName,edtEmail,edtPhone,edtCity,edtCompany,edtJobDescription;
    private Button btnSubmitInfo,BtnUpJobDesc,BtnUpJobApplied;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        exitUser();
        EnterInfoJob();
        EnterButtons();

    }


    @SuppressLint("ClickableViewAccessibility")
    private void allowScrollForEditText(){
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

    void EnterButtons(){
        BtnUpJobApplied.setOnClickListener(view -> startActivity(new Intent(Profile.this, ViewCvsApplys.class)));
        btnAbout.setOnClickListener(view -> startActivity(new Intent(Profile.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(Profile.this, Home.class)));

    }


    private void SpinnerHelperJobTypeIndex(Integer JobType1){
        SpinJobsType1.setText(listItemJobTypes[JobType1]);


    }



    private void SpinnerHelperJobTypLocationIndex(Integer JobLocation1){
        SpinJobsLocation1.setText(listItemJobLocations[JobLocation1]);
    }

    public void writeNewJob(String userId,String Company, String JobDescription,String JobType,String JobLocation, Integer SpinJobsLocation11Index, Integer SpinJobsType11Index, String Date) {
        JobSubmit jobSubmit = new JobSubmit(Company, JobDescription, JobType, JobLocation,SpinJobsLocation11Index, SpinJobsType11Index, Date);

        mDatabase.child("usersJobs").child(userId).setValue(jobSubmit);
    }
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



    private void SpinnerFuncAdvance() {



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



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), getString(R.string.Selected) + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    void EnterInfoJob(){

        if(user!=null){
            database = FirebaseDatabase.getInstance();
            String userId = user.getUid();
            DatabaseReference myRef3 = database.getReference("usersJobs").child(userId);

            myRef3.addValueEventListener(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


            BtnUpJobDesc.setOnClickListener(view -> {

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
                    Toast.makeText(Profile.this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
                }


            });

        }
        else{
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show();
        }

    }

    void exitUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || user != null){


            btnExit.setOnClickListener(view -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.Google_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(Profile.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(Profile.this,
                        task -> {
                            // firebase sign out
                            getInstance().signOut();

                            Intent intent = new Intent(Profile.this, SignIn.class);
                            startActivity(intent);
                            finish();

                        });

            });

        }


    }
}