package com.example.instagrame.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagrame.Adapter.PostAdapter;
import com.example.instagrame.Adapter.TagAdapter;
import com.example.instagrame.Model.Post;
import com.example.instagrame.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import com.example.instagrame.Adapter.UserAdapter;
import com.example.instagrame.Model.User;


public class SearchFragment extends Fragment {
private RecyclerView recyclerView;
private SocialAutoCompleteTextView searchBar;
private List<User> users;
private UserAdapter userAdapter;
private RecyclerView recyclerViewTag;
private List<String> hashTags;
private List<String> hashTagCount;
private TagAdapter tagAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container,
                false);
        recyclerView=view.findViewById(R.id.recycle_view_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

users=new ArrayList<>();
userAdapter=new UserAdapter(getContext(),users,true);
recyclerView.setAdapter(userAdapter);
        searchBar=view.findViewById(R.id.search_bar);
        hashTags=new ArrayList<>();
        hashTagCount=new ArrayList<>();
        recyclerViewTag=view.findViewById(R.id.recycle_view_tag);
        recyclerViewTag.setHasFixedSize(true);
        recyclerViewTag.setLayoutManager(new LinearLayoutManager(getContext()));
        tagAdapter=new TagAdapter(getContext(),hashTags,hashTagCount);
        recyclerViewTag.setAdapter(tagAdapter);

readUser();
readTags();
searchBar.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());

    }

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());

    }
});
        return view;
    }

    private void readTags() {
         FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(
                 new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         hashTagCount.clear();
                         hashTags.clear();
                         for(DataSnapshot snapshot:dataSnapshot.getChildren())
                         {
                             hashTags.add(snapshot.getKey());
                             hashTagCount.add(snapshot.getChildrenCount()+"");

                         }
                         tagAdapter.notifyDataSetChanged();
                     }


                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 }
         );
    }

    private  void readUser()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("User Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(searchBar.getText().toString()))
                {
                    users.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        User user=snapshot.getValue(User.class);
                        users.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void searchUser(String s)
    {
        Query query=FirebaseDatabase.getInstance().getReference()
                .child("User Details").orderByChild("userName").startAt(s)
                .endAt(s +"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
users.clear();
for(DataSnapshot snapshot:dataSnapshot.getChildren())
{
    User user=snapshot.getValue(User.class);
    users.add(user);

}
userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void filter(String text)
    {
        List<String>searchTag=new ArrayList<>();
        List<String> searchTagCount=new ArrayList<>();
        for(String s:hashTags)
        {
           if(s.toLowerCase().contains(text.toLowerCase()))
           {
               searchTag.add(s);
               searchTagCount.add(hashTagCount.get(hashTags.indexOf(s)));

           }
        }
        tagAdapter.filters(searchTag,searchTagCount);
    }
}
