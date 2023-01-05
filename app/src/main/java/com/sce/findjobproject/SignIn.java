package com.sce.findjobproject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextInputEditText Email,password;
    private Button signIn, forgotPass;
    private MaterialButton login;
    private ImageView sign_in_button;
    private RadioButton radio_LFJ;
    private RadioButton radio_LFE;
    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]
    public static int WhichUser=1;

    private FirebaseAuth mfireBaseAuth;
    private DatabaseReference mDatabase;
    private String userID;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        initialize();
        retrieveChoices();
        start();




    }


    // Save the user's selection from the radio group to shared preferences
    private void saveRadioChoice(){
        // Get a reference to the shared preferences file named "Settings"
        SharedPreferences mSharedPref = getSharedPreferences("Settings",MODE_PRIVATE);

        // Edit the shared preferences file
        SharedPreferences.Editor editor = mSharedPref.edit();

        // Check which radio button is checked and save the corresponding value to "key" in the shared preferences file
        if(  radio_LFJ.isChecked()){
            editor.putInt("key",0);
            editor.apply();
        }
        else if(radio_LFE.isChecked()){
            editor.putInt("key",1);
            editor.apply();
        }
    }


    // Retrieve the user's saved choices from the shared preferences file
    private void retrieveChoices(){
        // Get a reference to the shared preferences file named "Settings"
        SharedPreferences sharedPref = getSharedPreferences("Settings",MODE_PRIVATE);

        // Retrieve the value saved to "key" in the shared preferences file
        int i = sharedPref.getInt("key",-1);

        // Check the value of "key" and set the corresponding radio button as checked
        // Also set the value of "WhichUser" based on the value of "key"
        if( i == 0){
            radio_LFJ.setChecked(true);
            WhichUser=1;
        }
        else if(i == 1){
            radio_LFE.setChecked(true);
            WhichUser=2;
        }
    }

    // Handle the user clicking on a radio button
    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Check if the radio button is now checked
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_LFJ:
                // If the first radio button was clicked, save the choice and set "WhichUser" to 1
                if (checked)
                    saveRadioChoice();
                WhichUser=1;
                break;
            case R.id.radio_LFE:
                // If the second radio button was clicked, save the choice and set "WhichUser" to 2
                if (checked)
                    saveRadioChoice();
                WhichUser=2;
                break;
        }
    }

    // Check if an EditText field is empty
    private boolean isEmpty(EditText etText) {
        // Trim the text in the EditText field and check if the length is 0 or less
        return etText.getText().toString().trim().length() <= 0;
    }



    // Initialize variables for UI elements
    void initialize(){
        // Initialize variables for UI elements

        signIn =findViewById(R.id.signin);
        sign_in_button=findViewById(R.id.sign_in_button);
        login =findViewById(R.id.loginbtn);
        Email=findViewById(R.id.Email);
        password=findViewById(R.id.password1);
        forgotPass =findViewById(R.id.forgotpass);
        radio_LFJ=findViewById(R.id.radio_LFJ);
        radio_LFE=findViewById(R.id.radio_LFE);

    }
    // Show a Snackbar with the given message and duration
    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }
    // Start the login process
    void start(){

      // Set up the "Forgot Password" button
        forgotPass.setOnClickListener(view -> {
            if(Email == null || isEmpty(Email)){
                // Show a Snackbar with an error message
                Snackbar snackbar = Snackbar.make(view, R.string.Please_Provide_Email, Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                snackbar.show();
            }
            else{
                // Email field is not empty, send a password reset email to the provided email address

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = Objects.requireNonNull(Email.getText()).toString().trim();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Password reset email was sent, show a Snackbar with a success message
                                View view2 = findViewById(R.id.view);
                                String message = "Password Sent to Email "+Email.getText().toString()+" Make Sure to Check Your Spam Folder";
                                int duration = Snackbar.LENGTH_SHORT;
                                showSnackbar(view2, message, duration);
                            }
                            else{
                                // There was an error sending the password reset email, show a Snackbar with an error message
                                Snackbar snackbar = Snackbar.make(view, R.string.Password_Not_Sending_to_Email, Snackbar.LENGTH_SHORT);
                                snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                                snackbar.show();

                            }
                        });

            }
        });

// Check if the user is already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || user != null){
            // User is already logged in, go to the profile screen
            gotoProfile();
        }
        // Set up Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.Google_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // END config_signin

        // START initialize_auth
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // END initialize_auth


        sign_in_button.setOnClickListener(view -> {
            //signIn();
            ResultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
        });
        // Set up the "Sign Up" button

        signIn.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        });

        login.setOnClickListener(view -> signInEmail());
    }


    public void signInEmailFirebase(String Email, String Password){ //function to enter with email.
        mAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        updateUI(true);
                        startActivity(new Intent(SignIn.this, Home.class));
                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(SignIn.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(true);
                    }
                });
    }

    public void signInEmail(){ //check if email and password is valid and sign in.
        if(Email==null || Objects.requireNonNull(Email.getText()).toString().isEmpty() ){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        if(password==null || Objects.requireNonNull(password.getText()).toString().isEmpty() ) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        if(password!=null && Email!=null && !(Objects.requireNonNull(password.getText()).toString().isEmpty()) && !(Objects.requireNonNull(Email.getText()).toString().isEmpty())){
            signInEmailFirebase(Email.getText().toString().trim(),password.getText().toString().trim());
        }

    }




    // START on_start_check_user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(true);
    }
    // END on_start_check_user

    // START onactivityresult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                Toast.makeText(this, "firebaseAuthWithGoogle:" + account.getId(), Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // END onactivityresult

    // START auth_with_google
    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        updateUI(true);
                        //gotoProfile();
                        startActivity(new Intent(SignIn.this, Home.class));
                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(true);
                    }
                });
    }
    // END auth_with_google

    // START signin

    ActivityResultLauncher<Intent> ResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            Intent intent=result.getData();

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    });



    public void updateUI(boolean b) {
        Log.d("Success", "User Logged");

    }
    private void gotoProfile(){ //Function to enter the home layout after sign in was successful.
        Intent intent = new Intent(SignIn.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }




    public void tearDown() throws Exception {
    }

}