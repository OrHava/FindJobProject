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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FirebaseAuth mAuth;
    // [END declare_auth]
    public static int WhichUser=1;
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



    private void saveRadioChoice(){
        SharedPreferences mSharedPref = getSharedPreferences("Settings",MODE_PRIVATE);

        SharedPreferences.Editor editor = mSharedPref.edit();

// Initialize Radiogroup while saving choices
        if(  radio_LFJ.isChecked()){
            editor.putInt("key",0);
            editor.apply();
        }
        else if(radio_LFE.isChecked()){
            editor.putInt("key",1);
            editor.apply();
        }



    }

    private void retrieveChoices(){

        SharedPreferences sharedPref = getSharedPreferences("Settings",MODE_PRIVATE);
        int i = sharedPref.getInt("key",-1);
        if( i == 0){
            radio_LFJ.setChecked(true);
            WhichUser=1;
        }
        else if(i == 1){
            radio_LFE.setChecked(true);
            WhichUser=2;
        }



    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_LFJ:
                if (checked)
                    saveRadioChoice();
                WhichUser=1;
                break;
            case R.id.radio_LFE:
                if (checked)
                    saveRadioChoice();
                WhichUser=2;
                break;

        }

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }


    void initialize(){

        signIn =findViewById(R.id.signin);
        sign_in_button=findViewById(R.id.sign_in_button);
        login =findViewById(R.id.loginbtn);
        Email=findViewById(R.id.Email);
        password=findViewById(R.id.password1);
        forgotPass =findViewById(R.id.forgotpass);
        radio_LFJ=findViewById(R.id.radio_LFJ);
        radio_LFE=findViewById(R.id.radio_LFE);

    }

    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }

    void start(){


        forgotPass.setOnClickListener(view -> {
            if(Email == null || isEmpty(Email)){
                Toast.makeText(SignIn.this, "Please Provide Email", Toast.LENGTH_SHORT).show();
            }
            else{

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = Objects.requireNonNull(Email.getText()).toString().trim();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                View view2 = findViewById(R.id.view);
                                String message = "Password Sent to Email "+Email.getText().toString()+" Make Sure to Check Your Spam Folder";
                                int duration = Snackbar.LENGTH_SHORT;
                                showSnackbar(view2, message, duration);
                            }
                            else{
                                Toast.makeText(SignIn.this, "Password Not Sending to Email", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || user != null){
            gotoProfile();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("494529078470-0dvquaekhka84dn2e7sjsvoauoo6smhk.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        sign_in_button.setOnClickListener(view -> {
            //signIn();
            ResultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
        });

        signIn.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));

        });

        login.setOnClickListener(view -> signInEmail());
    }


    public void signInEmail(){
        if(Email==null || Objects.requireNonNull(Email.getText()).toString().isEmpty() ){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        if(password==null || Objects.requireNonNull(password.getText()).toString().isEmpty() ) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        if(password!=null && Email!=null && !(Objects.requireNonNull(password.getText()).toString().isEmpty()) && !(Objects.requireNonNull(Email.getText()).toString().isEmpty())){

            mAuth.signInWithEmailAndPassword(Objects.requireNonNull(Email.getText()).toString().trim(), Objects.requireNonNull(password.getText()).toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(SignIn.this, Home.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        }

    }




    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
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
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        //gotoProfile();
                        startActivity(new Intent(SignIn.this, Home.class));

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]

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



    private void updateUI(FirebaseUser user) {

    }
    private void gotoProfile(){
        Intent intent = new Intent(SignIn.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}