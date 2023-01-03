package com.sce.findjobproject;

import static com.sce.findjobproject.SignIn.WhichUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
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

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView txtWhichUser, SpinJobsType, SpinJobsLocation, txtJobType, txtLocation;
    private ImageButton btnProfile, btnAbout, BtnSearch;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    //private  StorageReference storageReference;
    private RecyclerView contactsRecView;
    private String[] CompanyId;
    private LottieAnimationView searchAnim;
    private Button btnAdmin;
    private int amountofJobs = 0;
    private int i = 0;
    private int count = 0;
    public int count2 = 0;
    private String[] listItemJobLocations;
    private String[] listItemJobTypes;
    boolean haveCvOrNo;
    private Dialog dialog, dialog2;
    private long mLastClickTime = 0;
    private final List<String> JobInfo = new ArrayList<>();
    private List<String> CompanyIdWithReports = new ArrayList<>();
    private ArrayList<String> CompanyReasons = new ArrayList<>();
    private Integer SpinJobsLocation1Index, SpinJobsType1Index;

    private String JobTypeSearch = "", LocationTypeSearch = "";

    //to scan websites you need to Parsing competitive websites for more jobs. using google api
    //Data of citys from: https://www.science.co.il/municipal/Cities.php
    //To find hash go to Navigate (its on top bar), Write-> search EveryWhere-> write "gradlew" and enter-> then write click on gradlew ->
    // open in-> Terminal -> then write in Terminal- > ./gradlew signingReport, then if everything is fine you should see hash 1 code which is what we need.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        contactsRecView = findViewById(R.id.contactsRecView);
        btnProfile = findViewById(R.id.btnProfile);
        btnAbout = findViewById(R.id.btnAbout);
        txtWhichUser = findViewById(R.id.txtWhichUser);
        BtnSearch = findViewById(R.id.BtnSearch);
        searchAnim = findViewById(R.id.searchAnim);
        SpinJobsType = findViewById(R.id.SpinJobsType);
        txtLocation = findViewById(R.id.txtLocation);
        txtJobType = findViewById(R.id.txtJobType);
        SpinJobsLocation = findViewById(R.id.SpinJobsLocation);
        btnAdmin = findViewById(R.id.btnAdmin);
        listItemJobLocations = getResources().getStringArray(R.array.jobs_location);
        listItemJobTypes = getResources().getStringArray(R.array.jobs_types);
        user = FirebaseAuth.getInstance().getCurrentUser();
        SearchHelperManager();
        EnterButtons();
        CheckWhichUser();
        SpinnerFuncAdvance();


    }

    private void AdminSearchHelper() {


        if (user != null) {
            CompanyIdWithReports = new ArrayList<>();
            CompanyReasons = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("reports");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                        CompanyIdWithReports.add(postsnapshot.getKey());
                        if (Objects.equals(postsnapshot.getValue(String.class), "1")) {
                            CompanyReasons.add(getString(R.string.discrimination));
                        } else if (Objects.equals(postsnapshot.getValue(String.class), "2")) {
                            CompanyReasons.add(getString(R.string.violence_content));
                        } else if (Objects.equals(postsnapshot.getValue(String.class), "3")) {
                            CompanyReasons.add(getString(R.string.wrong_information));
                        } else if (Objects.equals(postsnapshot.getValue(String.class), "4")) {
                            CompanyReasons.add(getString(R.string.disturbing_or_offensive));
                        } else if (Objects.equals(postsnapshot.getValue(String.class), "5")) {
                            CompanyReasons.add(getString(R.string.violation_of_intellectual_property_or_other_law));
                        } else if (Objects.equals(postsnapshot.getValue(String.class), "6")) {
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
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            AdminSearchHelper();
            mLastClickTime = SystemClock.elapsedRealtime();
            amountofJobs = 0;
            ArrayList<Contact> contacts = new ArrayList<>();
            searchAnim.playAnimation();
            if (user != null) {

                databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (int j = 0; j < CompanyIdWithReports.size(); j++) {
                            for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                                if (Objects.equals(postsnapshot.getKey(), CompanyIdWithReports.get(j))) {
                                    String CompanyName = postsnapshot.child("Company").getValue(String.class);
                                    String Date = postsnapshot.child("Date").getValue(String.class);
                                    String JobDescription = postsnapshot.child("JobDescription").getValue(String.class);
                                    String JobTypeSearch = postsnapshot.child("JobType").getValue(String.class);
                                    String LocationTypeSearch = postsnapshot.child("JobLocation").getValue(String.class);
                                    contacts.add(new Contact(CompanyName, JobTypeSearch, JobDescription, LocationTypeSearch, Date));
                                    JobInfo.add(contacts.get(amountofJobs).toString());
                                    amountofJobs++;
                                    ContactsRecViewAdapter adapter = new ContactsRecViewAdapter(Home.this);
                                    adapter.setContacts(contacts);
                                    adapter.setCompanyReasons(CompanyReasons);
                                    contactsRecView.setAdapter(adapter);
                                    contactsRecView.setLayoutManager(new LinearLayoutManager(Home.this));

                                }
                            }

                        }


                        searchAnim.cancelAnimation();
                        if (amountofJobs == 0) {
                            Snackbar snackbar = Snackbar.make(view, "" + getString(R.string.Job_Not_Found), Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                            snackbar.show();

                        } else {
                            txtWhichUser.setText(getString(R.string.We_Found) + amountofJobs + getString(R.string.with_reports_on_them));

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

    public void ShareButton(int index) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Look at the job opportunities at FindJob, " + JobInfo.get(index));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void ReportButton(int index) {


        final String[] Reasons = {
                getString(R.string.discrimination), getString(R.string.violence_content), getString(R.string.wrong_information), getString(R.string.disturbing_or_offensive),
                getString(R.string.violation_of_intellectual_property_or_other_law), getString(R.string.other), getString(R.string.exit)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(R.string.choose_reason_of_report_post);
        builder.setItems(Reasons, (dialog, which) -> {
            if (getString(R.string.discrimination).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("1");
                dialog.dismiss();
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
            } else if (getString(R.string.violence_content).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("2");
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
            } else if (getString(R.string.wrong_information).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("3");
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (getString(R.string.disturbing_or_offensive).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("4");
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (getString(R.string.violation_of_intellectual_property_or_other_law).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("5");
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (getString(R.string.other).equals(Reasons[which])) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("6");
                Toast.makeText(Home.this, R.string.post_report_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (getString(R.string.exit).equals(Reasons[which])) {
                dialog.dismiss();
            }

        });
        builder.show();


    }

    public void DeletePostAdmin(int index) {
        AtomicBoolean DeletePostOrNot = new AtomicBoolean(false);
        if (WhichUser == 3) {
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


    private void checkIfUserHaveCV() {

        if (user != null) {
            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads").child(userId).child("Uploads");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    haveCvOrNo = dataSnapshot.exists();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }

    }


    private void SearchHelperManager() {
        if (user != null) {
            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Managers");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        if (Objects.requireNonNull(postsnapshot.getValue()).toString().equals(userId)) {
                            count2++;
                        }


                    }
                    if (count2 > 0) {
                        WhichUser = 3;
                        CheckWhichUser();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });


        }

    }

    private void SpinnerFuncAdvance() {


        SpinJobsLocation.setOnClickListener(view -> {
            dialog2 = new Dialog((Home.this));
            dialog2.setContentView(R.layout.dialog_searchable_spinner);

            dialog2.getWindow().setLayout(650, 800);

            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog2.show();

            EditText editText = dialog2.findViewById(R.id.edit_text);
            ListView listView = dialog2.findViewById(R.id.list_view);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,
                    android.R.layout.simple_list_item_1,
                    listItemJobLocations) {


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
                SpinJobsLocation.setText(adapter.getItem(i));
                for (int j = 0; i < listItemJobLocations.length - 1; j++) {
                    if (listItemJobLocations[j].equals(adapter.getItem(i))) {
                        SpinJobsLocation1Index = j;
                        break;
                    }
                }
                dialog2.dismiss();
            });

        });

        SpinJobsType.setOnClickListener(view -> {
            dialog = new Dialog((Home.this));
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            dialog.getWindow().setLayout(650, 800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edit_text);
            ListView listView = dialog.findViewById(R.id.list_view);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,
                    android.R.layout.simple_list_item_1,
                    listItemJobTypes) {


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
                SpinJobsType.setText(adapter.getItem(i));

                for (int j = 0; i < listItemJobTypes.length - 1; j++) {
                    if (listItemJobTypes[j].equals(adapter.getItem(i))) {
                        SpinJobsType1Index = j;
                        break;
                    }
                }
                dialog.dismiss();
            });

        });
    }

    private void SearchHelper() {

        databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    if (Objects.equals(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class), SpinJobsLocation1Index) && Objects.requireNonNull(postsnapshot.child("SpinJobsType11Index").getValue(Integer.class)).equals(SpinJobsType1Index)) {
                        CompanyId[i] = postsnapshot.getKey();
                        i++;

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });


    }


    private void Search() {


        BtnSearch.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            amountofJobs = 0;
            i = 0;
            ArrayList<Contact> contacts = new ArrayList<>();
            JobTypeSearch = SpinJobsType.getText().toString();
            LocationTypeSearch = SpinJobsLocation.getText().toString();
            searchAnim.playAnimation();
            if (user != null) {

                databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            if (Objects.equals(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class), SpinJobsLocation1Index) && Objects.requireNonNull(postsnapshot.child("SpinJobsType11Index").getValue(Integer.class)).equals(SpinJobsType1Index)) {
                                String CompanyName = postsnapshot.child("Company").getValue(String.class);
                                String Date = postsnapshot.child("Date").getValue(String.class);
                                String JobDescription = postsnapshot.child("JobDescription").getValue(String.class);
                                contacts.add(new Contact(CompanyName, JobTypeSearch, JobDescription, LocationTypeSearch, Date));
                                JobInfo.add(contacts.get(amountofJobs).toString());
                                amountofJobs++;


                                ContactsRecViewAdapter adapter = new ContactsRecViewAdapter(Home.this);
                                adapter.setContacts(contacts);
                                contactsRecView.setAdapter(adapter);

                                contactsRecView.setLayoutManager(new LinearLayoutManager(Home.this));

                            }


                        }
                        CompanyId = new String[amountofJobs];

                        searchAnim.cancelAnimation();
                        if (amountofJobs == 0) {
                            Snackbar snackbar = Snackbar.make(view, "" + getString(R.string.Job_Not_Found), Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                            snackbar.show();

                        } else {
                            txtWhichUser.setText(getString(R.string.We_Found) + amountofJobs + getString(R.string.JobsThatSuitYou));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value

                    }
                });


                SearchHelper();

            }


        });

    }


    void SendCvFunc(int position) {
        count = 0;
        if (user != null) {
            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(CompanyId[position]).child("cvIds");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                        if (Objects.requireNonNull(postsnapshot.getValue()).toString().equals(userId)) {
                            count++;
                        }


                    }
                    if (count == 0 && !haveCvOrNo) {
                        Toast.makeText(Home.this, R.string.You_dont_have_a_cv_to_send, Toast.LENGTH_SHORT).show();
                    } else if (count == 0) {
                        String userId = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(CompanyId[position]).child("cvIds").push();
                        databaseReference.setValue(userId);
                        Toast.makeText(Home.this, R.string.Cv_sent_successfully2, Toast.LENGTH_SHORT).show();

                    } else if (count > 0) {
                        Toast.makeText(Home.this, R.string.Cv_already_sent, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });
        } else {
            Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), getString(R.string.Selected) + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public static int CheckWhichUserForRecView() {
        if (WhichUser == 1) {
            return 1;
        } else if (WhichUser == 2) {
            return 2;
        } else if (WhichUser == 3) {

            return 3;
        }
        return 0;
    }


    void CheckWhichUser() {
        boolean whichUserBoolean = false;
        if (WhichUser == 1) {
            txtWhichUser.setText(R.string.Your_Looking_For_Jobs);
            whichUserBoolean = true;
            checkIfUserHaveCV();
            Search();
        } else if (WhichUser == 2) {
            txtWhichUser.setText(R.string.Your_Looking_For_Employees);
            checkIfUserHaveCV();
            Search();
        } else if (WhichUser == 3) {
            txtWhichUser.setText(R.string.Your_Manager_of_The_App);
            btnAdmin.setVisibility(View.VISIBLE);
            txtJobType.setVisibility(View.GONE);
            txtLocation.setVisibility(View.GONE);
            SpinJobsLocation.setVisibility(View.GONE);
            SpinJobsType.setVisibility(View.GONE);

            AdminSearch();
        }

        if (user != null) {

            String userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics").child(userId);
            databaseReference.setValue(whichUserBoolean);
        }
    }


    void EnterButtons() {

        btnProfile.setOnClickListener(view -> startActivity(new Intent(Home.this, Profile.class)));

        btnAbout.setOnClickListener(view -> startActivity(new Intent(Home.this, About.class)));
    }
    public String[] getCompanyId() {
        return CompanyId;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public AlertDialog.Builder getAlertDialogBuilder() {
        return new AlertDialog.Builder(this);
    }

    public void showToast(String messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }

}