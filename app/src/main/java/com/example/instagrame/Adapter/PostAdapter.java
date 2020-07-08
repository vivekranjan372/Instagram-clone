package com.example.instagrame.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagrame.CommentActivity;
import com.example.instagrame.Model.Post;
import com.example.instagrame.Model.User;
import com.example.instagrame.OtherUserDetails;
import com.example.instagrame.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private Context context;
    private List<Post> posts;
    private int postCount;

    private FirebaseUser firebaseUser;
    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.post_item,parent
      ,false);
      return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

  final Post post=posts.get(position);

 Picasso.get().load(post.getImageUrl()).into(holder.posImage);
        holder.description.setText(post.getDescription());

         FirebaseDatabase.getInstance().getReference().child("User Details")
                .child(post.getPublisher()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
final User user=dataSnapshot.getValue(User.class);
if(user.getImageUrl().equals("Default"))
{
    holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
}
else
{
    Picasso.get().load(user.getImageUrl()).placeholder(R
            .mipmap.ic_launcher).into(holder.imageProfile);
}

holder.userName.setText(user.getUserName());
holder.author.setText(user.getName());


                        holder.horizontalView.setOnClickListener(new View.OnClickListener() {
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
                        holder.author.setOnClickListener(new View.OnClickListener() {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
         isLike(post.getPostId(),holder.like);
         noOfLikes(post.getPostId(),holder.noOfLikes);
         getComments(post.getPostId(),holder.noOfComments);
         isSaved(post.getPostId(),holder.save);

         holder.like.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(holder.like.getTag().equals("like")) {
                     FirebaseDatabase.getInstance().getReference()
                             .child("Like").child(post.getPostId()).child(
                             firebaseUser.getUid()).setValue(true);
                 }
                     else
                     {
                         FirebaseDatabase.getInstance().getReference()
                                 .child("Like").child(post.getPostId()).child(
                                 firebaseUser.getUid()).removeValue();
                     }

             }
         });
         holder.comment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context, CommentActivity.class);
                 intent.putExtra("postId",post.getPostId());
                 intent.putExtra("authorId",post.getPublisher());
                  context.startActivity(intent);

             }
         });
         holder.noOfComments.setOnClickListener(
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent=new Intent(context, CommentActivity.class);
                         intent.putExtra("postId",post.getPostId());
                         intent.putExtra("authorId",post.getPublisher());
                         context.startActivity(intent);
                     }
                 }
         );
         holder.save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(holder.save.getTag().equals("save"))
                 {
                     FirebaseDatabase.getInstance().getReference()
                             .child("Saves").child(firebaseUser.getUid()).child(post
                     .getPostId()).setValue(true);
                     Toast.makeText(context,"your picture is saved",Toast.LENGTH_LONG).show();
                 }
                 else
                 {
                     FirebaseDatabase.getInstance().getReference()
                             .child("Saves").child(firebaseUser.getUid()).child(post
                             .getPostId()).removeValue();
                 }
             }
         });
         holder.posImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Dialog dialog=new Dialog(context,android.R.style.Theme_DeviceDefault);
                 dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                 ImageView imageView=new ImageView(context);
                 Picasso.get().load(post.getImageUrl()).into(imageView);
                 dialog.addContentView(imageView,new
                         RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                         ViewGroup.LayoutParams.MATCH_PARENT));
                 dialog.show();


             }
         });




    }



    @Override
    public int getItemCount() {

        postCount=posts.size();
        return postCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageProfile;
        public ImageView posImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
public ImageView more;
public TextView userName;
public TextView noOfLikes;
public  TextView author;
 public TextView noOfComments;
public  SocialTextView description;
public LinearLayout horizontalView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile=itemView.findViewById(R.id.profile_image);
            posImage=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            save=itemView.findViewById(R.id.save);
            more=itemView.findViewById(R.id.more);
            userName=itemView.findViewById(R.id.user_name);
            noOfLikes=itemView.findViewById(R.id.no_of_likes);
            author=itemView.findViewById(R.id.author);
            noOfComments=itemView.findViewById(R.id.no_of_comments);
            description=itemView.findViewById(R.id.description);
            horizontalView=itemView.findViewById(R.id.horizontal_view);


        }

    }

    private void isLike(String postId, final ImageView imageView)
    {
        FirebaseDatabase.getInstance().getReference().child("Like")
        .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.child(firebaseUser.getUid()).exists())
              {
                 imageView.setImageResource(R.drawable.ic_heart);
                 imageView.setTag("liked");
              }
              else {
                  imageView.setImageResource(R.drawable.ic_like);
                  imageView.setTag("like");
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })   ;
    }
    private void noOfLikes(String postId, final TextView likesCount)
    {
        FirebaseDatabase.getInstance().getReference()
                .child("Like").child(postId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   likesCount.setText(dataSnapshot.getChildrenCount()+" likes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
    private  void getComments(String postId, final TextView getComment)

    {
        FirebaseDatabase.getInstance().getReference().
                child("Comments").child(postId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getComment.setText("View All "+dataSnapshot.getChildrenCount()
                        +" Comments");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void isSaved(final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser
        .getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postId).exists())
                {
image.setImageResource(R.drawable.ic_save_black);
image.setTag("saved");

                }
                else
                {
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
