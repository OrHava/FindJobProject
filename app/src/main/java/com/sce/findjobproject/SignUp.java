package com.sce.findjobproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.annotations.NonNull;

public class SignUp extends AppCompatActivity {
    public FirebaseAuth mAuth;

    public TextInputEditText edit_email,edit_password;
    public MaterialButton button_register;
    public Button button_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        initialize();
        onStart();
        startButtons();




    }





    void startButtons(){
        button_login.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        });
    }

    void initialize(){
        // Initialize variables for UI elements
        edit_email= findViewById(R.id.edit_email);
        edit_password= findViewById(R.id.edit_password);
        button_register=findViewById(R.id.button_register);
        button_login=findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is already connected.
        if (user != null) {
            Toast.makeText(this, "User is already connected", Toast.LENGTH_SHORT).show();
        } else {
            reload(); // if not start register function name 'reload'
        }
    }


    public  static boolean isEmailValid(String email) { //function that checks if email is valid.
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    } //function that check if password is valid. (password need to be over 6 numbers and letter to work in firebase)



    // Other code

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void reload() {
        // Set up the "Register" button
        button_register.setOnClickListener(view -> {
            //check if email and password are valid.
            if(edit_email==null || Objects.requireNonNull(edit_email.getText()).toString().isEmpty() || !isEmailValid(edit_email.getText().toString())){
                // Email is invalid, show a Snackbar with an error message
                Snackbar snackbar = Snackbar.make(view, R.string.enter_a_valid_email, Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                snackbar.show();
            }
            if(edit_password==null || Objects.requireNonNull(edit_password.getText()).toString().isEmpty() || !isPasswordValid(edit_password.getText().toString())) {
                // Password is invalid, show a Snackbar with an error message
                Snackbar snackbar = Snackbar.make(view, R.string.enter_a_valid_password, Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                snackbar.show();
            }

            if(edit_password!=null && edit_email!=null && !(Objects.requireNonNull(edit_password.getText()).toString().isEmpty()) && !(Objects.requireNonNull(edit_email.getText()).toString().isEmpty()) &&isEmailValid(edit_email.getText().toString())&& isPasswordValid(edit_password.getText().toString())){

                mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(edit_email.getText()).toString().trim(), edit_password.getText().toString().trim())
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in    user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Snackbar snackbar = Snackbar.make(view, R.string.Authentication_successful, Snackbar.LENGTH_SHORT);
                                    snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                                    snackbar.show();


                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Snackbar snackbar = Snackbar.make(view, R.string.Authentication_failed, Snackbar.LENGTH_SHORT);
                                    snackbar.setAction("Dismiss", view1 -> snackbar.dismiss());
                                    snackbar.show();
                                    updateUI(null);
                                }
                            }

                            private void updateUI(FirebaseUser user) {
                                if(user!=null) {
                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            }
                        });

            }

        });
    }



}