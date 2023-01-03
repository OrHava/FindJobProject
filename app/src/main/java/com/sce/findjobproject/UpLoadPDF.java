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
        user= FirebaseAuth.getInstance().getCurrentUser();
        upload_btn=findViewById(R.id.upload_btn);
        pdf_name=findViewById(R.id.name);
        btnAbout=findViewById(R.id.btnAbout);
        btnHome=findViewById(R.id.btnHome);
        btnProfile=findViewById(R.id.btnProfile);
        ViewPdf_btn=findViewById(R.id.ViewPdf_btn);
        EnterButtons();
        //Database

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Uploads");

        upload_btn.setOnClickListener(view -> selectFiles());





        ViewPdf_btn.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),ViewAllFiles.class);
            startActivity(intent);
        });

    }

    void EnterButtons(){

        btnAbout.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, Home.class)));
        btnProfile.setOnClickListener(view -> startActivity(new Intent(UpLoadPDF.this, Home.class)));

    }

    private void selectFiles() {

        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // startActivityForResult(Intent.createChooser(intent,"Select PDF Files..."),1);

        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    if (data != null) {
                        UploadFiles(data.getData());
                    }
                }
            });





    private void UploadFiles(Uri data) {

        if (user != null) {


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            StorageReference reference = storageReference.child("Uploads/" + System.currentTimeMillis() + ".pdf");
            reference.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {


                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri url = uriTask.getResult();
                        String userId = user.getUid();
                        pdfClass pdfClass = new pdfClass(pdf_name.getText().toString(), url.toString());
                        databaseReference.child(userId).child(databaseReference.getKey()).setValue(pdfClass);

                        Toast.makeText(UpLoadPDF.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }).addOnProgressListener(snapshot -> {

                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded" + (int) progress + "%");
                    });
        }
    }
}