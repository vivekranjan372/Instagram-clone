package com.example.instagrame.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagrame.Model.Post;
import com.example.instagrame.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MyPicturesAdapter extends RecyclerView.Adapter<MyPicturesAdapter.ViewHolder> {
    private Context context;
    private List<Post> myImages;
    private int count;

    public MyPicturesAdapter(Context context, List<Post> myImages) {
        this.context = context;
        this.myImages = myImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view=LayoutInflater.from(context).inflate(R.layout.pictures_item,parent,false);

        return new MyPicturesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Post post=myImages.get(position);
        if(count<1)
        {
            holder.reminderText.setText("you don`t post anything ,post something first");
        }
else {



            Picasso.get().load(post.getImageUrl()).into(holder.myImage);
        }
holder.myImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Dialog dialog=new Dialog(context,android.R.style.Theme_DeviceDefault);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView imageView=new ImageView(context);
        Picasso.get().load(post.getImageUrl()).into(imageView);
        dialog.addContentView(imageView,new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();

    }
});
    }

    @Override
    public int getItemCount() {
        if(myImages.isEmpty())
        {
            count=0;
        }
        else
        {
            count=myImages.size();
        }

        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

    public ImageView myImage;
public TextView reminderText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
myImage=itemView.findViewById(R.id.myImage);
reminderText=itemView.findViewById(R.id.reminderText);
        }
    }
}
