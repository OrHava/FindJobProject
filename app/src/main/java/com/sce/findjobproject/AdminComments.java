package com.sce.findjobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminComments extends AppCompatActivity {
    public ListView list_view;
    public EditText edtAddComment ;
    public ImageButton btnAddComment;
    private ImageButton btnHome,btnAbout,btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comments);
        list_view=findViewById(R.id.list_view);
        btnHome=findViewById(R.id.btnHome);
        btnProfile=findViewById(R.id.btnProfile);
        btnAbout=findViewById(R.id.btnAbout);
        edtAddComment = findViewById(R.id.edtAddComment);
        btnAddComment = findViewById(R.id.btnAddComment);
        displayComments();
        EnterComment();
        EnterButtons();

    }



    private void EnterComment() {
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtAddComment.getText().toString();
                addComment(text);

            }
        });

    }


    public void addComment(String comment) {

        // Create a new comment object with the comment text and the current timestamp
        Map<String, Object> newComment = new HashMap<>();
        //newComment.put("name", name);
        newComment.put("text", comment);
        newComment.put("timestamp", System.currentTimeMillis());

        // Add the new comment to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("comments");
        commentsRef.push().setValue(newComment);
    }



    public void displayComments(){
        // Get a reference to the database node containing the HashMap
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("comments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> comments = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the comment text from the "text" field of the HashMap
                    Map<String, Object> comment = (Map<String, Object>) childSnapshot.getValue();
                    String text = null;
                    if (comment != null) {
                        text = (String) comment.get("text");
                        comments.add(text);
                    }

                }

                Collections.reverse(comments);

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AdminComments.this, android.R.layout.simple_list_item_1, comments) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text = view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                return view;
                            }
                        };

                list_view.setAdapter(adapter2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


    }

    void EnterButtons(){

        btnAbout.setOnClickListener(view -> startActivity(new Intent(AdminComments.this, About.class)));

        btnHome.setOnClickListener(view -> startActivity(new Intent(AdminComments.this, Home.class)));
        btnProfile.setOnClickListener(view -> startActivity(new Intent(AdminComments.this, Profile.class)));

    }

}