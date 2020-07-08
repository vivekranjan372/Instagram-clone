package com.example.instagrame.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagrame.OtherUserDetails;
import com.example.instagrame.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.instagrame.Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewModler>{
    private Context context;
    private List<User> users;
    private boolean isFragment;
    private FirebaseUser firebaseUser;


    public UserAdapter(Context context, List<User> users, boolean isFragment) {
        this.context = context;
        this.users = users;
        this.isFragment = isFragment;
    }



    @NonNull
    @Override
    public ViewModler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout
        .user_item,parent,false);
        return new UserAdapter.ViewModler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewModler holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user=users.get(position);
        holder.buttonFollow.setVisibility(View.VISIBLE);
        holder.userName.setText(user.getName());
        holder.fullName.setText(user.getUserName());
        Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher)
                .into(holder.imageProfile);
        isFollowed(user.getId(),holder.buttonFollow);


        if(user.getId().equals(firebaseUser.getUid())) {
            holder.buttonFollow.setVisibility(View.GONE);
        }
        holder.buttonFollow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        if (holder.buttonFollow.getText().toString().equals("Follow now")) {

                            alert.setMessage("Do you want Really want to follow ~ " +
                                    user.getName()).setCancelable(false).setPositiveButton(
                                    "Follow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            FirebaseDatabase.getInstance().getReference().child("Follow")
                                                    .child(firebaseUser.getUid()).child("Following").child(user.getId()).setValue(true);
                                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).
                                                    child("Followers").child(firebaseUser.getUid()).setValue(true);
                                            AlertDialog alertFollow = new AlertDialog.Builder(context).create();
                                            alertFollow.setTitle("Instagrame Operation");
                                            alertFollow.setMessage("you have  Followed " + user.getName());
                                            alertFollow.show();
                                        }
                                    }
                            ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                        }

                        else if(holder.buttonFollow.getText().toString().equals("Following"))
                        {

                            alert.setMessage("Do you Really want to Follow Back").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            FirebaseDatabase.getInstance().getReference().child("Follow")
                                                    .child(firebaseUser.getUid()).child("Following").child(user.getId()).removeValue();
                                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).
                                                    child("Followers").child(firebaseUser.getUid()).removeValue();
                                            AlertDialog followBack=new AlertDialog.Builder(context).create();
                                            followBack.setTitle("Instagrame Operation");
                                            followBack.setMessage("you have Followed Back "+user.getName());
                                            followBack.show();
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                        }

                      AlertDialog  dialogNotification=alert.create();
                      dialogNotification.setTitle("Instagrame Operation");
                        dialogNotification.show();
                    }
                }
        );
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OtherUserDetails.class);

               intent.putExtra("name",user.getName());
               intent.putExtra("profile_image",user.getImageUrl());
               intent.putExtra("userName",user.getUserName());
               intent.putExtra("bio",user.getBio());
               intent.putExtra("userId",user.getId());
         context.startActivity(intent);

            }
        });

    }

    private void isFollowed(final String id, final Button buttonFollow) {
         FirebaseDatabase.getInstance().getReference().child("Follow").
                 child(firebaseUser.getUid()).child("Following").
                 addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists())
                {
                    buttonFollow.setText("Following");


                }
                else
                {
                    buttonFollow.setText("Follow now");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    public class ViewModler extends RecyclerView.ViewHolder

    {

        public CircleImageView imageProfile;
        public TextView userName;
        public TextView fullName;
        public Button buttonFollow;
        public RelativeLayout layout;

        public ViewModler(@NonNull View itemView) {
            super(itemView);
            imageProfile=itemView.findViewById(R.id.image_profile);
            userName=itemView.findViewById(R.id.user_name);
            fullName=itemView.findViewById(R.id.full_name);
            buttonFollow=itemView.findViewById(R.id.button_follow);
layout=itemView.findViewById(R.id.user_view);

        }
    }

}
