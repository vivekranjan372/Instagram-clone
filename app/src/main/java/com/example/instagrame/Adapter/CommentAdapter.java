package com.example.instagrame.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagrame.Model.Comment;
import com.example.instagrame.Model.User;
import com.example.instagrame.OtherUserDetails;
import com.example.instagrame.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private  Context context;
    private List<Comment> comments;
    private FirebaseUser firebaseUser;
    private String commentId;

    public CommentAdapter(Context context, List<Comment> comments,String commentId) {
        this.context = context;
        this.comments = comments;
        this.commentId=commentId;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).
                inflate(R.layout.comment_item,parent,false);
        return  new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment=comments.get(position);

            holder.commentView.setText(comment.getComment());


    FirebaseDatabase.getInstance().getReference()
            .child("User Details").child(comment.getPublisher()).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final User user = dataSnapshot.getValue(User.class);
                    holder.userName.setText(user.getUserName());
                    if (user.getImageUrl().equals("Default")) {
                        holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getImageUrl()).
                                placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                    }


holder.imageProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, OtherUserDetails.class);
        intent.putExtra("userId",user.getId());
        intent.putExtra("userName",user.getUserName());
        intent.putExtra("name",user.getName());
        intent.putExtra("profile_image",user.getImageUrl());
        intent.putExtra("bio",user.getBio());
        context.startActivity(intent);
    }
});
                    holder.userName.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(context,
                                            OtherUserDetails.class);
                                    intent.putExtra("userId",user.getId());
                                    intent.putExtra("userName",
                                            user.getUserName());
                                    intent.putExtra("name",user.getName());
                                    intent.putExtra("profile_image",
                                            user.getImageUrl());
                                    intent.putExtra("bio",user.getBio());
                                    context.startActivity(intent);
                                }
                            }
                    );
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(comment.getPublisher().equals(firebaseUser
                            .getUid()))
                            {
                                final AlertDialog alertDialog=new AlertDialog.Builder(context)
                                        .create();
                                alertDialog.setTitle("Alert!!");
                                alertDialog.setMessage("Do you really want " +
                                        "to delete ??");
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE
                                        , "NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE
                                        , "YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
            FirebaseDatabase.getInstance().getReference().
                    child("Comments").child(commentId).
                    child(comment.getCommentId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        AlertDialog alert=new AlertDialog.Builder(context).create();
                        alert.setTitle("Comment Activity");
                        alert.setMessage("Comment is deleted Successfully");
                        alert.show();
                        dialog.dismiss();
                    }
                }
            });

                                            }
                                        });
                                alertDialog.show();

                            }
                            return true;
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }
    );

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
public CircleImageView imageProfile;
public TextView userName;
public  TextView commentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile=itemView.findViewById(R.id.image_profile);
            userName=itemView.findViewById(R.id.user_name);
            commentView=itemView.findViewById(R.id.comment);
        }
    }

}
