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
import java.util.Objects;
import java.util.stream.Collectors;

public class ViewCvsApplys extends AppCompatActivity {
    ListView listview;
    DatabaseReference databaseReference;
    List<pdfClass> uploads;
    pdfClass pdfuploadhelper;
    private FirebaseUser user;
    StorageReference storageReference;
    private ImageButton btnHome;
    private ImageButton btnProfile;
    private ArrayList<String> userApplied;
    private List<String> newList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_files);
        btnHome=findViewById(R.id.btnHome);
        btnProfile=findViewById(R.id.btnProfile);
        listview=findViewById(R.id.listview2);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uploads=new ArrayList<>();
        userApplied= new ArrayList<>();

        storageReference= FirebaseStorage.getInstance().getReference();

        GetAllUsers();

        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            pdfClass pdfupload=uploads.get(i);
            pdfuploadhelper=pdfupload;
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setType("application/pdf");
            intent.setData(Uri.parse(pdfupload.getUrl()));

            AlertDialog.Builder alert = new AlertDialog.Builder(ViewCvsApplys.this);
            alert.setTitle(R.string.Delete_Watch);
            alert.setMessage(R.string.You_want_to_see_this_File);
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                startActivity(intent);
                dialog.dismiss();
            });

            alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

            alert.show();

        });
        EnterButtons();
    }

    private void GetAllUsers() {
        if(user!=null) {
            String userId = user.getUid();
            final int[] i = {0};
            databaseReference = FirebaseDatabase.getInstance().getReference("usersJobs").child(userId).child("cvIds");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                        userApplied.add(Objects.requireNonNull(postsnapshot.getValue()).toString());
                        i[0]++;
                    }
                    newList = userApplied.stream()
                            .distinct()
                            .collect(Collectors.toList());
                    viewAllFiles();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

        }



    }


    private void viewAllFiles() {

        if(user!=null) {
            String userId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
            databaseReference.addValueEventListener(new ValueEventListener() {
int matchs=0;
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(int i=0;i<newList.size();i++){
                        for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                            if(newList.get(i).equals(postsnapshot.getKey()) && !(userId.equals(postsnapshot.getKey()))){

                                matchs++;
                                pdfClass pdfClass= postsnapshot.child("Uploads").getValue(com.sce.findjobproject.pdfClass.class);
                                uploads.add(pdfClass);
                            }


                        }
                    }

                    Toast.makeText(ViewCvsApplys.this, getString(R.string.there_is)+matchs+ getString(R.string.cvsforyou), Toast.LENGTH_SHORT).show();

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





    void EnterButtons(){



        btnHome.setOnClickListener(view -> startActivity(new Intent(ViewCvsApplys.this, Home.class)));
        btnProfile.setOnClickListener(view -> startActivity(new Intent(ViewCvsApplys.this, Profile.class)));

    }
}