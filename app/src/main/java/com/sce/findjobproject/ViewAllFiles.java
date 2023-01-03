package com.sce.findjobproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewAllFiles extends AppCompatActivity {
    ListView listview;
    DatabaseReference databaseReference;
    List<pdfClass> uploads;
    pdfClass pdfuploadhelper;
    private FirebaseUser user;
    StorageReference storageReference;
    private ImageButton btnHome,btnAbout,btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_files);
        // Initialize variables for UI elements
        btnHome=findViewById(R.id.btnHome);
        btnProfile=findViewById(R.id.btnProfile);
        btnAbout=findViewById(R.id.btnAbout);
        listview=findViewById(R.id.listview2);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uploads=new ArrayList<>();

        storageReference= FirebaseStorage.getInstance().getReference();


        viewAllFiles();
        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            pdfClass pdfupload=uploads.get(i);
            pdfuploadhelper=pdfupload;
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setType("application/pdf");
            intent.setData(Uri.parse(pdfupload.getUrl()));

            AlertDialog.Builder alert = new AlertDialog.Builder(ViewAllFiles.this);
            alert.setTitle(R.string.Delete_Watch);
            alert.setMessage(R.string.Watch_or_Delete_File);
            alert.setPositiveButton(R.string.Delete_The_File, (dialog, which) -> {
                //do here
                deleteFile();
                Toast.makeText(ViewAllFiles.this, ""+R.string.Files_Delete_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            alert.setNegativeButton(R.string.Yo_want_to_see_this_File, (dialog, which) -> {
                startActivity(intent);
                dialog.dismiss();
            });

            alert.show();

        });
        EnterButtons();
    }

    private void deleteFile(){ //function to delete cv.

        FirebaseStorage mFirebaseStorage;
        mFirebaseStorage=FirebaseStorage.getInstance();
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(pdfuploadhelper.getUrl());


        photoRef.delete().addOnSuccessListener(aVoid -> {
            // File deleted successfully
            Toast.makeText(ViewAllFiles.this, "onSuccess: deleted files", Toast.LENGTH_SHORT).show();

            removeUser();
        }).addOnFailureListener(exception -> {
            // error.
            Toast.makeText(ViewAllFiles.this, "onFailure: did not delete file", Toast.LENGTH_SHORT).show();
        });
    }

    private void viewAllFiles() { //function  to view all files.

        if(user!=null) {
            String userId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                        pdfClass pdfClass= postsnapshot.getValue(com.sce.findjobproject.pdfClass.class);

                        uploads.add(pdfClass);
                    }
                    String[] Uploads=new String[uploads.size()];
                    for (int i=0;i<Uploads.length;i++){
                        Uploads[i]=uploads.get(i).getName();
                    }

                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,Uploads){

                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view= super.getView(position, convertView, parent);
                            TextView text = view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            text.setTextSize(22);
                            return view;
                        }
                    };
                    listview.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }

    }



    void removeUser(){ //remove user and its file.
        if(user!=null){
            String userId = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef3 = database.getReference("Uploads").child(userId);
            myRef3.getRef().removeValue();

        }

    }


    void EnterButtons(){  // Sets up the onClickListeners for the navigation buttons

        btnAbout.setOnClickListener(view -> startActivity(new Intent(ViewAllFiles.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(ViewAllFiles.this, Home.class)));
        btnProfile.setOnClickListener(view -> startActivity(new Intent(ViewAllFiles.this, Profile.class)));

    }
}