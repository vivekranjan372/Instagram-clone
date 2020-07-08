package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instagrame.Adapter.CommentAdapter;
import com.example.instagrame.Model.Comment;
import com.example.instagrame.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
  private EditText addComment;
  private TextView post;
  private CircleImageView imageProfile;
  private String postId;
  private String authorId;
  private FirebaseUser user;
  private AlertDialog alertDialog;
  private RecyclerView recyclerViewComment;
  private CommentAdapter commentAdapter;
  private List<Comment> comments;
 private  String commentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        alertDialog=new AlertDialog.Builder(CommentActivity.this).create();
        alertDialog.setTitle("Comment Status");
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comments=new ArrayList<>();

        Intent intent=getIntent();
        postId=intent.getStringExtra( "postId");
        authorId=intent.getStringExtra("authorId");


        recyclerViewComment=findViewById(R.id.recycle_view_comment);
        recyclerViewComment.setHasFixedSize(true);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));

        commentAdapter=new CommentAdapter(this,comments,postId);
        recyclerViewComment.setAdapter(commentAdapter);


        addComment=findViewById(R.id.add_comment);

        imageProfile=findViewById(R.id.image_profile);
        post=findViewById(R.id.post);

        user= FirebaseAuth.getInstance().getCurrentUser();
getUserImage();
post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(addComment.getText().toString()))
        {
            alertDialog.setMessage("No comment added");
            alertDialog.show();
        }
        else
        {
            putComment();
        }
    }
});



        getComments();


    }

    private void getComments()
    {
       DatabaseReference reference=FirebaseDatabase.getInstance()
               .getReference().child("Comments").child(postId);
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               comments.clear();
               for(DataSnapshot snapshot:dataSnapshot.getChildren())
               {
                   Comment comment=snapshot.getValue(Comment.class);
                   comments.add(comment);
               }
               commentAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
    private void putComment()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Comments").child(postId);
         commentId=reference.push().getKey();
        HashMap<String,Object>map=new HashMap<>();
        map.put("commentId",commentId);
        map.put("publisher",user.getUid());
        map.put("comment",addComment.getText().toString());
        reference.child(commentId).setValue(map).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       alertDialog.setMessage("Comment added");
                       alertDialog.show();
                       addComment.setText("");
                   }
                   else
                   {
                       alertDialog.setMessage
                               (Objects.requireNonNull(task.
                                       getException()).getMessage());
                       alertDialog.show();
                   }
                    }
                }
        );

    }
  public void  getUserImage()
    {
        FirebaseDatabase.getInstance().getReference()
                .child("User Details").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              User user =dataSnapshot.getValue(User.class);
              if(user.getImageUrl().equals("Default"))
              {
                  imageProfile.setImageResource(R.mipmap.ic_launcher);

              }

             else
              {
                  Picasso.get().load(user.getImageUrl()).into(imageProfile);
              }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
