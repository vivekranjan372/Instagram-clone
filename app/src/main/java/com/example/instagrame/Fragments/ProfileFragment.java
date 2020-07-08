package com.example.instagrame.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagrame.Adapter.MyPicturesAdapter;
import com.example.instagrame.Adapter.PostAdapter;
import com.example.instagrame.EditProfile;
import com.example.instagrame.Model.Post;
import com.example.instagrame.Model.User;
import com.example.instagrame.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
private CircleImageView imageProfile;
private ImageView options;
private TextView posts;
private TextView followers;
private TextView following;
private  TextView fullName;
private  TextView bio;
private Button editProfile;
private  ImageView myPicture;
private  ImageView savedPictures;
private FirebaseUser myUser;
private TextView userName;
private String profileId;
private RecyclerView myPictures;
private  RecyclerView savePosts;
private List<Post> myImages;
private TextView reminderText;
private MyPicturesAdapter myPicturesAdapter;
private MyPicturesAdapter postAdapterSave;
private List<Post> mySaves;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myUser= FirebaseAuth.getInstance().getCurrentUser();
        profileId=myUser.getUid();
        View view=inflater.inflate(R.layout.fragment_profile,
                container, false);
        imageProfile=view.findViewById(R.id.image_profile);
        options=view.findViewById(R.id.options);
        posts=view.findViewById(R.id.posts);
        followers=view.findViewById(R.id.followers);
        fullName=view.findViewById(R.id.full_name);
        following=view.findViewById(R.id.following);
        bio=view.findViewById(R.id.bio);
        editProfile=view.findViewById(R.id.editProfile);
        myPicture=view.findViewById(R.id.my_picture);
        savedPictures=view.findViewById(R.id.saved_picture);
        userName=view.findViewById(R.id.user_name);
        myPictures=view.findViewById(R.id.recycle_view_pictures);
        savePosts=view.findViewById(R.id.recycle_view_saved);
        reminderText=view.findViewById(R.id.reminderText);

        myImages=new ArrayList<>();

        savePosts.setHasFixedSize(true);
        savePosts.setLayoutManager(new GridLayoutManager(getContext(),2));
        mySaves=new ArrayList<>();
        postAdapterSave=new MyPicturesAdapter(getContext(),mySaves);
        savePosts.setAdapter(postAdapterSave);

        mySavedImages();
userInfo();
getFollowersAndFollowing();
getPostCount();
getSavedPosts();
editProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    startActivity(new Intent(getContext(), EditProfile.class).
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
});
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
myPictures.setVisibility(View.VISIBLE);
savePosts.setVisibility(View.GONE);

savedPictures.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        myPictures.setVisibility(View.GONE);
        savePosts.setVisibility(View.VISIBLE);


    }
});
        return view;
    }

    private void getSavedPosts() {

        final List<String> savedIds=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves")
                .child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for(DataSnapshot snapshot:dataSnapshot.getChildren())
{
     savedIds.add(snapshot.getKey());
}
FirebaseDatabase.getInstance().getReference().child("Image Post").addValueEventListener(
        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySaves.clear();
               for(DataSnapshot snapshot:dataSnapshot.getChildren())
               {
                   Post post=snapshot.getValue(Post.class);
                   for(String id:savedIds)
                   {
                       if(post.getPostId().equals(id))
                       {
                           mySaves.add(post);
                       }
                   }
               }
               postAdapterSave.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }
);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mySavedImages() {
        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPictures.setVisibility(View.VISIBLE);
                savePosts.setVisibility(View.GONE);
                addMyImages();
               myPictures.setHasFixedSize(true);

myPictures.setLayoutManager(new GridLayoutManager(getContext(),2));

             myPicturesAdapter=new MyPicturesAdapter(getContext(),myImages);
             myPictures.setAdapter(myPicturesAdapter);

            }
        });

    }

    private void addMyImages() {
        FirebaseDatabase.getInstance().getReference().child("Image Post").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        myImages.clear();
                       for(DataSnapshot snapshot:dataSnapshot.getChildren())
                       {
                           Post post=snapshot.getValue(Post.class);
                           if(post.getPublisher().equals(myUser.getUid()))
                           {
                               myImages.add(post);
                           }

                       }
                        Collections.reverse(myImages);
                       myPicturesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void userInfo()
    {
        FirebaseDatabase.getInstance().getReference().child("User Details")
                .child(profileId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);

                        if(user.getImageUrl().equals("Default"))
                        {
                            Picasso.get().load(R.mipmap.ic_launcher).into(imageProfile);

                        }
                        else
                        {
                            Picasso.get().load(user.getImageUrl()).placeholder(
                                    R.mipmap.ic_launcher
                            ).into(imageProfile);
                        }
                        userName.setText(user.getUserName());
                        fullName.setText(user.getName());
                        bio.setText(user.getBio());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
    private void getFollowersAndFollowing()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId);
        reference.child("Followers").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        followers.setText(""+dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        reference.child("Following").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      following.setText(""+dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
    private void getPostCount()
    {
        FirebaseDatabase.getInstance().getReference().child("Image Post")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count=0;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Post post=snapshot.getValue(Post.class);
                            if(post.getPublisher().equals(profileId))
                            {
                                count++;
                            }
                        }
                        posts.setText(" "+count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
