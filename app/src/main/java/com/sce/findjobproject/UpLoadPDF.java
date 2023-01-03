package com.sce.findjobproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpLoadPDF extends AppCompatActivity {
    Button upload_btn,ViewPdf_btn;
    EditText pdf_name;
    private ImageButton btnHome,btnAbout,btnProfile;
    private FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_pdf);
        // Initialize variables for UI elements
        user= FirebaseAuth.getInstance().getCurrentUser();
        upload_btn=findViewById(R.id.upload_btn);
        pdf_name=findViewById(R.id.name);
        btnAbout=findViewById(R.id.btnAbout);
        btnHome=findViewById(R.id.btnHome);
        btnProfile=findViewById(R.id.btnProfile);
        ViewPdf_btn=findViewById(R.id.ViewPdf_btn);
        EnterButtons();


        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Uploads");

        upload_btn.setOnClickListener(view -> selectFiles());





        ViewPdf_btn.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),ViewAllFiles.class);
            startActivity(intent);
        });

    }

    void EnterButtons(){
       // Sets up the onClickListeners for the navigation buttons
        btnAbout.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, Home.class)));
        btnProfile.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, Home.class)));

    }
    // Method to select a file from the device's storage
    private void selectFiles() {

// Create an Intent to open the file picker
        Intent intent= new Intent();
        // Set the type of file to PDF
        intent.setType("application/pdf");
        // Set the action to get the content of the file
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Start the activity to select a file
        someActivityResultLauncher.launch(intent);
    }
    // Create an ActivityResultLauncher to handle the result of the file selection activity
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Check if the activity was successful
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Get the data from the activity result
                    // There are no request codes
                    Intent data = result.getData();
                    if (data != null) {
                        // Call the UploadFiles method and pass in the selected file's URI
                        UploadFiles(data.getData());
                    }
                }
            });




    // Method to upload files to Firebase Storage
    private void UploadFiles(Uri data) {
        // Check if the user is not null

        if (user != null) {
            // Create a ProgressDialog to show the progress of the upload


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            // Create a reference to the file in Firebase Storage
            StorageReference reference = storageReference.child("Uploads/" + System.currentTimeMillis() + ".pdf");
            // Upload the file to Firebase Storage
            reference.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {

                        // Get the download URL for the uploaded file
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        // Wait for the download URL to be available
                        while (!uriTask.isComplete()) ;
                        // Get the download URL
                        Uri url = uriTask.getResult();
                        // Get the user's ID
                        String userId = user.getUid();
                        // Create a new pdfClass object with the PDF name and download URL
                        pdfClass pdfClass = new pdfClass(pdf_name.getText().toString(), url.toString());
                        // Add the pdfClass object to the database under the user's ID
                        databaseReference.child(userId).child(databaseReference.getKey()).setValue(pdfClass);
                        // Show a Toast message to confirm that the file has been uploaded
                        Toast.makeText(UpLoadPDF.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }).addOnProgressListener(snapshot -> {
                      //show the progress bar of file upload.
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded" + (int) progress + "%");
                    });
        }
    }
}