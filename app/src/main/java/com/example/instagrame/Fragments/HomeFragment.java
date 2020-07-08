package com.example.instagrame.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagrame.Adapter.PostAdapter;
import com.example.instagrame.Model.Post;
import com.example.instagrame.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
private RecyclerView recyclerViewPosts;
private PostAdapter postAdapter;
private List<Post> postList;
private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,
                container, false);

        recyclerViewPosts=view.findViewById(R.id.recycle_view_post);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList=new ArrayList<>();
        followingList=new ArrayList<>();
        checkFollowingUsers();
        postAdapter=new PostAdapter(getContext(),postList);
        recyclerViewPosts.setAdapter(postAdapter);

        return view;
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().
                child("Follow").child(FirebaseAuth.getInstance().getCurrentUser()
        .getUid()).child("Following")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    followingList.add(snapshot.getKey());
                }
                readData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readData() {
        FirebaseDatabase.getInstance().getReference().
                child("Image Post").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Post post=snapshot.getValue(Post.class);
                            for(String id:followingList)
                            {
                                if(post.getPublisher().equals(id))
                                {
                                    postList.add(post);
                                }
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

}
