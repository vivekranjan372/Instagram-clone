package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instagrame.Adapter.PostAdapter;
import com.example.instagrame.Adapter.UserAdapter;
import com.example.instagrame.Fragments.SearchFragment;
import com.example.instagrame.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserDetails extends AppCompatActivity {
private CircleImageView imageProfile;
private TextView name;
private TextView userName;
private TextView bio;
private SocialAutoCompleteTextView search;
private Button otherInfo;
private Button follow;
private FirebaseUser currentUser;
private String userId;
  private  String getProfileImage;
  private RecyclerView recyclerViewUserPosts;
  private List<Post> userPosts;
  private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_details);

        imageProfile=findViewById(R.id.profile_image);
        name=findViewById(R.id.name);
        userName=findViewById(R.id.user_name);
        bio=findViewById(R.id.bio);
        search=findViewById(R.id.search_bar);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        otherInfo=findViewById(R.id.info);
        follow=findViewById(R.id.follow);
        recyclerViewUserPosts=findViewById(R.id.recycle_view_user_posts);


        Toolbar toolbar=findViewById(R.id.search_tool);
      search.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addUserPosts();


        recyclerViewUserPosts.setHasFixedSize(true);
        recyclerViewUserPosts.setLayoutManager(new LinearLayoutManager(this));
        userPosts=new ArrayList<>();
        postAdapter=new PostAdapter(this,userPosts);
        recyclerViewUserPosts.setAdapter(postAdapter);


//      Intent intent=getIntent();
//      String getProfileImage=intent.getStringExtra("profile_image");
//      String getName=intent.getStringExtra("name");
//      String getUserName=intent.getStringExtra("userName");
//      String getBio=intent.getStringExtra("bio");

        Bundle getInfo=getIntent().getExtras();
         getProfileImage=getInfo.getString("profile_image");
        String getUserName=getInfo.getString("userName");
        String getName=getInfo.getString("name");
        String getBio=getInfo.getString("bio");
userId=getInfo.getString("userId");


       if(getProfileImage.equals("Default"))
       {
           Picasso.get().load(R.mipmap.ic_launcher).into(imageProfile);
       }
       else {
           Picasso.get().load(getProfileImage).
                   placeholder(R.mipmap.ic_launcher).into(imageProfile);
       }
        name.setText(getName);
        userName.setText(getUserName);
        bio.setText(getBio);

//imageProfile.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Intent intent=new Intent(OtherUserDetails.this,PopUpImage.class);
//        intent.putExtra("imageUri",getProfileImage);
//        startActivity(intent);
//        finish();
//    }
//});

isFollow();
followAndUnFollow();

    }

    private void addUserPosts() {
        FirebaseDatabase.getInstance().getReference().child("Image Post").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
userPosts.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                           Post post=snapshot.getValue(Post.class);
                           if(userId.equals(post.getPublisher()))
                           {
                               userPosts.add(post);
                           }
                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

    }

    private void followAndUnFollow() {
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(follow.getText().toString().equals("Follow Now"))
            {
                FirebaseDatabase.getInstance().getReference()
                        .child("Follow").child(currentUser.getUid()).child("Following")
                        .child(userId).setValue(true);
                FirebaseDatabase.getInstance()
                        .getReference().child("Follow").child(userId)
                        .child("Followers").child(currentUser.getUid()).setValue(true);
            }
            else
            {
                FirebaseDatabase.getInstance().getReference()
                        .child("Follow").child(currentUser.getUid()).child("Following")
                        .child(userId).removeValue();
                FirebaseDatabase.getInstance()
                        .getReference().child("Follow").child(userId)
                        .child("Followers").child(currentUser.getUid()).removeValue();
            }
            }
        });
    }

    private void isFollow() {
        FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(currentUser.getUid()).child("Following").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(userId.equals(currentUser.getUid()))
                        {
                            follow.setVisibility(View.GONE);
                            otherInfo.setVisibility(View.GONE);

                        }
if(dataSnapshot.child(userId).exists())
{
    follow.setText("Following");
}
else
{
    follow.setText("Follow Now");
}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

    }
    public void imagePopUp(View view)
    {
        Dialog dialog=new Dialog(this,android.R.style.Theme_DeviceDefault);
        dialog.getWindow().requestFeature(Window
                .FEATURE_ACTION_BAR);

//        dialog.setContentView(getLayoutInflater().inflate
//                (R.layout.activity_pop_up_image,null));
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });
        ImageView imageView=new ImageView(this);
        if(getProfileImage.equals("Default"))
        {
            Picasso.get().load(R.mipmap.ic_launcher).into(imageView);
        }
        else {
            Picasso.get().load(getProfileImage).
                    placeholder(R.mipmap.ic_launcher).into(imageView);
        }

        dialog.addContentView(imageView,new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.
                        MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT
        ));


        dialog.show();
    }

}
