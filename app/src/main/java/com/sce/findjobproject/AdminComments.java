package com.sce.findjobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminComments extends AppCompatActivity {
    public ListView list_view;
    public EditText edtAddComment ;
    public ImageButton btnAddComment;
    public Button btnclearcomments;
    private FirebaseDatabase mDatabase;
    private FirebaseUser user;
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
        btnclearcomments  = findViewById(R.id.btnclearcomments);
        user= FirebaseAuth.getInstance().getCurrentUser();
        displayComments();
        EnterComment();
        EnterButtons();
        ClearComments();

    }

    private void ClearComments() {
        btnclearcomments.setOnClickListener(view -> {

            if(user!=null ) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AdminComments.this);
                alert.setTitle(R.string.delete_post);
                alert.setMessage(R.string.are_you_sure_you_want_to_clear_comments);
                alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                    mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef3 = mDatabase.getReference("comments");
                    myRef3.removeValue();

                    dialog.dismiss();

                });

                alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

                alert.show();
            }

        });
    }


    private void EnterComment() {
        btnAddComment.setOnClickListener(view -> {
            String text = edtAddComment.getText().toString();
            addComment(text);

        });

    }
    public static class Comment {
        private final String text;
        private final String timestamp;
        public Comment(String text, String timestamp) {
            this.text = text;
            this.timestamp = timestamp;
        }
        public String getText() {
            return text;
        }
        public String getTimestamp() {
            return timestamp;
        }
    }


    public void addComment(String comment) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String dateTime = year + "/" + (month + 1) + "/" + day + " " + hour + ":" + minute + ":" + second;


        // Create a new comment object with the comment text and the current timestamp
        Map<String, Object> newComment = new HashMap<>();
        //newComment.put("name", name);
        newComment.put("text", comment);
        newComment.put("timestamp", dateTime);

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
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the comment text and timestamp from the HashMap
                    Map<String, Object> comment = (Map<String, Object>) childSnapshot.getValue();
                    String text;
                    String timestamp;
                    if (comment != null) {
                        text = (String) comment.get("text");
                        timestamp = (String) comment.get("timestamp");
                        comments.add(new Comment(text, timestamp));
                    }
                }
                Collections.reverse(comments);

                ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(AdminComments.this, android.R.layout.simple_list_item_2, android.R.id.text1, comments) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = view.findViewById(android.R.id.text1);
                        TextView text2 = view.findViewById(android.R.id.text2);
                        Comment comment = comments.get(position);
                        text1.setText(comment.getText());
                        text1.setTextColor(Color.BLACK);
                        text2.setText(String.valueOf(comment.getTimestamp()));
                        text2.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                list_view.setAdapter(adapter);


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