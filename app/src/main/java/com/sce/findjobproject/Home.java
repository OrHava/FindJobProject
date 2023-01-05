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
import android.util.Log;
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
        // Initialize variables for UI elements
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

        // Check if the 'user' variable is not null
        if (user != null) {
            // Initialize the 'CompanyIdWithReports' and 'CompanyReasons' lists
            CompanyIdWithReports = new ArrayList<>();
            CompanyReasons = new ArrayList<>();
            // Get a reference to the 'reports' node in the Firebase database
            databaseReference = FirebaseDatabase.getInstance().getReference("reports");
            // Add a listener to the 'reports' node to get a single update of the data
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through all the children of the 'reports'
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        // Add the key (company ID) of the current child to the 'CompanyIdWithReports' list
                        CompanyIdWithReports.add(postsnapshot.getKey());
                        // Check the value of the current child and add the corresponding string to the 'CompanyReasons' list
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
        // Set up a click listener for BtnSearch button
        BtnSearch.setOnClickListener(view -> {
            // If button was clicked within the last second, return and do nothing
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            // Call AdminSearchHelper method
            AdminSearchHelper();
            // Reset mLastClickTime and amountofJobs
            mLastClickTime = SystemClock.elapsedRealtime();
            amountofJobs = 0;
            // Initialize contacts list
            ArrayList<Contact> contacts = new ArrayList<>();
            // Play searchAnim animation
            searchAnim.playAnimation();
            if (user != null) {
                // Get reference to Firebase database
                databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
                // Add listener to database reference
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Iterate over CompanyIdWithReports list and children of database reference
                        for (int j = 0; j < CompanyIdWithReports.size(); j++) {
                            for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                // If key of child matches element in CompanyIdWithReports list
                                if (Objects.equals(postsnapshot.getKey(), CompanyIdWithReports.get(j))) {
                                    // Retrieve values of certain fields from child
                                    String CompanyName = postsnapshot.child("Company").getValue(String.class);
                                    String Date = postsnapshot.child("Date").getValue(String.class);
                                    String JobDescription = postsnapshot.child("JobDescription").getValue(String.class);
                                    String JobTypeSearch = postsnapshot.child("JobType").getValue(String.class);
                                    String LocationTypeSearch = postsnapshot.child("JobLocation").getValue(String.class);
                                    // Create new Contact object with retrieved values
                                    contacts.add(new Contact(CompanyName, JobTypeSearch, JobDescription, LocationTypeSearch, Date));
                                    // Add Contact object to JobInfo list and increment amountofJobs
                                    JobInfo.add(contacts.get(amountofJobs).toString());
                                    // Set up ContactsRecViewAdapter with contacts list and set it to contactsRecView
                                    amountofJobs++;
                                    ContactsRecViewAdapter adapter = new ContactsRecViewAdapter(Home.this);
                                    adapter.setContacts(contacts);
                                    adapter.setCompanyReasons(CompanyReasons);
                                    contactsRecView.setAdapter(adapter);
                                    // Set layout manager for contactsRecView
                                    contactsRecView.setLayoutManager(new LinearLayoutManager(Home.this));

                                }
                            }

                        }

                        // Cancel searchAnim animation
                        searchAnim.cancelAnimation();
                        // If amountofJobs is 0, show snackbar that jobs not found.
                        if (amountofJobs == 0) {
                            Snackbar snackbar = Snackbar.make(view, "" + getString(R.string.Job_Not_Found), Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                            snackbar.show();

                        } else { //else show the amount of jobs found in textview 'txtWhichUser'
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
    // Method to share job information via another app
    public void ShareButton(int index) {
        // Create new intent for sending action
        Intent sendIntent = new Intent();
        // Set action to send
        sendIntent.setAction(Intent.ACTION_SEND);
        // Put job information from JobInfo list at specified index in intent as extra text
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Look at the job opportunities at FindJob, " + JobInfo.get(index));
        // Set type of content being sent as plain text
        sendIntent.setType("text/plain");
        // Start activity using intent
        startActivity(sendIntent);

    }

    public void ReportButton(int index) {

        // Define an array of report reasons
        final String[] Reasons = {
                getString(R.string.discrimination), getString(R.string.violence_content), getString(R.string.wrong_information), getString(R.string.disturbing_or_offensive),
                getString(R.string.violation_of_intellectual_property_or_other_law), getString(R.string.other), getString(R.string.exit)
        };
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        // Set the title of the dialog to a string defined in the app's resource files
        builder.setTitle(R.string.choose_reason_of_report_post);
        // Set the items of the dialog to the Reasons array and set an OnClickListener for the dialog
        builder.setItems(Reasons, (dialog, which) -> {
            // Check which item was clicked
            if (getString(R.string.discrimination).equals(Reasons[which])) {
                // Write a value to the Firebase Database at "reports/CompanyId[index]"
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("reports").child(CompanyId[index]);
                myRef3.getRef().setValue("1");
                myRef3.getRef().setValue("1");
                // Dismiss the dialog and display a toast message to confirm the post was successfully reported
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
        // show the AlertDialog
        builder.show();


    }

    public void DeletePostAdmin(int index) {
        // Define a boolean variable to store whether the post should be deleted or not
        AtomicBoolean DeletePostOrNot = new AtomicBoolean(false);
        // Check if the user is an administrator (WhichUser == 3)
        if (WhichUser == 3) {
            // Create an AlertDialog.Builder
            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            // Set the title and message of the dialog
            alert.setTitle(R.string.delete_post);
            alert.setMessage(R.string.are_you_sure);
            // Set a positive button for the dialog with a lambda expression as the OnClickListener
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                // Delete the post from the Firebase Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database.getReference("usersJobs").child(CompanyIdWithReports.get(index));
                myRef3.getRef().removeValue();
                DatabaseReference myRef4 = database.getReference("reports").child(CompanyIdWithReports.get(index));
                myRef4.getRef().removeValue();
                // Set the DeletePostOrNot variable to true
                DeletePostOrNot.set(true);
                // Display a toast message to confirm the post was successfully deleted
                Toast.makeText(Home.this, R.string.Files_Delete_successfully, Toast.LENGTH_SHORT).show();
                // Dismiss the dialog
                dialog.dismiss();

            });
            // Set a negative button for the dialog with a lambda expression as the OnClickListener
            alert.setNegativeButton(R.string.no, (dialog, which) -> {
                // Set the DeletePostOrNot variable to false
                DeletePostOrNot.set(false);
                // Dismiss the dialog

                dialog.dismiss();
            });
            // Display the dialog
            alert.show();


        }

    }


    private void checkIfUserHaveCV() {
        // Check if the user is signed in
        if (user != null) {
            // Get the user's ID
            String userId = user.getUid();
            // Get a reference to the Uploads/userId/Uploads node in the Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads").child(userId).child("Uploads");
            // Add a listener for a single value event to the Uploads/userId/Uploads node
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Set the haveCvOrNo variable to true if the Uploads/userId/Uploads node exists, and false otherwise
                    haveCvOrNo = dataSnapshot.exists();


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }

    }


    public  void SearchHelperManager() {
        // Check if the user is signed in
        if (user != null) {
            // Get the user's ID
            String userId = user.getUid();
            // Get a reference to the Managers node in the Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("Managers");
            // Add a listener for a single value event to the Managers node
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through all the children of the Managers node
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        // Check if the user's ID is equal to the value of the current child node
                        if (Objects.requireNonNull(postsnapshot.getValue()).toString().equals(userId)) {
                            // Increment the count2 variable
                            String childKey = postsnapshot.getKey();
                            Log.d("Child key:", childKey);
                            count2++;
                        }


                    }
                    // Check if the count2 variable is greater than 0
                    if (count2 > 0) {
                        // Set the WhichUser variable to 3
                        WhichUser = 3;
                        // Call the CheckWhichUser function
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

        // Set a click listener for the SpinJobsLocation spinner
        SpinJobsLocation.setOnClickListener(view -> {
            dialog2 = new Dialog((Home.this));
            dialog2.setContentView(R.layout.dialog_searchable_spinner);
            // Set the layout width and height of the dialog
            dialog2.getWindow().setLayout(650, 800);
            // Set the background color of the dialog
            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            // Show the dialog
            dialog2.show();
            // Get references to the EditText and ListView in the dialog
            EditText editText = dialog2.findViewById(R.id.edit_text);
            ListView listView = dialog2.findViewById(R.id.list_view);

            // Create an ArrayAdapter with the list of job locations and a custom view for the list items
            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(Home.this,
                    android.R.layout.simple_list_item_1,
                    listItemJobLocations) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the view for the list item
                    View view = super.getView(position, convertView, parent);
                    // Get the text view in the list item
                    TextView text = view.findViewById(android.R.id.text1);
                    // Set the text color to black
                    text.setTextColor(Color.BLACK);

                    // Return the modified view
                    return view;
                }


            };
            // Set the adapter for the ListView
            listView.setAdapter(adapter);
            // Add a text watcher to the EditText to filter the list as the user types
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
            // Set an item click listener for the ListView
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                // Set the text of the SpinJobsLocation spinner to the selected item
                SpinJobsLocation.setText(adapter.getItem(i));
                // Iterate through the list of job locations to find the item user search for.
                for (int j = 0; i < listItemJobLocations.length - 1; j++) {
                    if (listItemJobLocations[j].equals(adapter.getItem(i))) {
                        SpinJobsLocation1Index = j;
                        break;
                    }
                }
                dialog2.dismiss();
            });

        });

        // the same code for job types:

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
        // Set up reference to "usersJobs" node in the database
        databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
        // Add single value event listener to the database reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through each child (user job) in the "usersJobs" node
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    // Check if location and type match the specified values
                    if (Objects.equals(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class), SpinJobsLocation1Index) && Objects.requireNonNull(postsnapshot.child("SpinJobsType11Index").getValue(Integer.class)).equals(SpinJobsType1Index)) {
                        // If values match, store child's key in the CompanyId array and increment i
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

        // Set up click listener for search button
        BtnSearch.setOnClickListener(view -> {
            // Check if elapsed time since last button click is less than 1 second
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                // If it is, return without doing anything else
                return;
            }
            // Update last click time
            mLastClickTime = SystemClock.elapsedRealtime();
            // Reset amountofJobs and i variables, create new empty ArrayList of Contact objects
            amountofJobs = 0;
            i = 0;
            ArrayList<Contact> contacts = new ArrayList<>();
            // Get search values for job type and location from spinners
            JobTypeSearch = SpinJobsType.getText().toString();
            LocationTypeSearch = SpinJobsLocation.getText().toString();
            // Start animation
            searchAnim.playAnimation();
            // Check if user object is not null
            if (user != null) {
                // Set up reference to "usersJobs" node in the database and add single value event listener
                databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Iterate through each child (user job) in the "usersJobs" node
                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            // Check if location and type match the specified values
                            if (Objects.equals(postsnapshot.child("SpinJobsLocation11Index").getValue(Integer.class), SpinJobsLocation1Index) && Objects.requireNonNull(postsnapshot.child("SpinJobsType11Index").getValue(Integer.class)).equals(SpinJobsType1Index)) {
                                // If values match, retrieve values of "Company", "Date", and "JobDescription" properties
                                String CompanyName = postsnapshot.child("Company").getValue(String.class);
                                String Date = postsnapshot.child("Date").getValue(String.class);
                                String JobDescription = postsnapshot.child("JobDescription").getValue(String.class);
                                // Create new Contact object with retrieved values and add to contacts ArrayList
                                contacts.add(new Contact(CompanyName, JobTypeSearch, JobDescription, LocationTypeSearch, Date));
                                // Add toString representation of Contact object to JobInfo ArrayList
                                JobInfo.add(contacts.get(amountofJobs).toString());
                                // Increment amountofJobs
                                amountofJobs++;

                                // Create new ContactsRecViewAdapter and set contacts ArrayList as data source
                                ContactsRecViewAdapter adapter = new ContactsRecViewAdapter(Home.this);
                                // Set adapter on contactsRecView and set layout manager
                                adapter.setContacts(contacts);
                                contactsRecView.setAdapter(adapter);

                                contactsRecView.setLayoutManager(new LinearLayoutManager(Home.this));

                            }


                        }

                        CompanyId = new String[amountofJobs];

                        searchAnim.cancelAnimation();
                        if (amountofJobs == 0) { //show snackbar if jobs not found and if job found set text of the amount of jobs found.
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


                SearchHelper(); //use the 'searchhelper' to find the ids of each jobs to be able to send cv later to them.

            }


        });

    }


    void SendCvFunc(int position) {
        // Initialize count variable to 0
        count = 0;
        // Check if user object is not null
        if (user != null) {
            // Get user's unique identifier (userId) from user object
            String userId = user.getUid();
            // Set up reference to "cvIds" child node of specified user job in "usersJobs" node, add single value event listener
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(CompanyId[position]).child("cvIds");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through each child of "cvIds" node
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                        // Check if value of current child equals userId
                        if (Objects.requireNonNull(postsnapshot.getValue()).toString().equals(userId)) {
                            // If it does, increment count
                            count++;
                        }


                    }
                    // if the job seeker doesn't have cv.
                    if (count == 0 && !haveCvOrNo) {
                        Toast.makeText(Home.this, R.string.You_dont_have_a_cv_to_send, Toast.LENGTH_SHORT).show();
                    } else if (count == 0) { //else if job seeker send the cv successfully.
                        String userId = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(CompanyId[position]).child("cvIds").push();
                        databaseReference.setValue(userId);
                        Toast.makeText(Home.this, R.string.Cv_sent_successfully2, Toast.LENGTH_SHORT).show();

                    } else if (count > 0) { //else if cv already send.
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
    //function needed for the adapter.

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        //function needed for the adapter.
        // TODO Auto-generated method stub

    }


    public static int CheckWhichUserForRecView() {
        //function that help adapter know which user use the program.
        if (WhichUser == 1) {
            return 1;
        } else if (WhichUser == 2) {
            return 2;
        } else if (WhichUser == 3) {

            return 3;
        }
        return 0;
    }


    void CheckWhichUser() { //check which user and start each function that user need.
        // Initialize boolean flag for whether user is a job seeker or not
        boolean whichUserBoolean = false;
        // Check value of WhichUser variable
        if (WhichUser == 1) {
            // If WhichUser is 1, set text for txtWhichUser and set whichUserBoolean to true
            txtWhichUser.setText(R.string.Your_Looking_For_Jobs);
            whichUserBoolean = true;
            // Call checkIfUserHaveCV and Search methods
            checkIfUserHaveCV();
            Search();
            // If WhichUser is 2, set text for txtWhichUser and set whichUserBoolean to true
        } else if (WhichUser == 2) {
            txtWhichUser.setText(R.string.Your_Looking_For_Employees);
            // Call checkIfUserHaveCV and Search methods
            checkIfUserHaveCV();
            Search();
        } else if (WhichUser == 3) {
            // If WhichUser is 3, set text for txtWhichUser and show btnAdmin, hide other views
            txtWhichUser.setText(R.string.Your_Manager_of_The_App);
            btnAdmin.setVisibility(View.VISIBLE);
            txtJobType.setVisibility(View.GONE);
            txtLocation.setVisibility(View.GONE);
            SpinJobsLocation.setVisibility(View.GONE);
            SpinJobsType.setVisibility(View.GONE);
            // Call AdminSearch method
            AdminSearch();
        }
        // Check if user object is not null
        if (user != null) {
            // Get user's unique identifier (userId) from user object
            String userId = user.getUid();
            // Set up reference to "AdminStatistics" child node with userId as key, set value to whichUserBoolean
            databaseReference = FirebaseDatabase.getInstance().getReference("AdminStatistics").child(userId);
            databaseReference.setValue(whichUserBoolean);
        }
    }


    void EnterButtons() { // Sets up the onClickListeners for the navigation buttons

        btnProfile.setOnClickListener(view -> startActivity(new Intent(Home.this, Profile.class)));

        btnAbout.setOnClickListener(view -> startActivity(new Intent(Home.this, About.class)));
    }


}