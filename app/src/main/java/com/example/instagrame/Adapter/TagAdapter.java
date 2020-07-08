package com.example.instagrame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagrame.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHandler> {


    private Context context;
    private List<String> listTag;
    private List<String> countTag;

    public TagAdapter(Context context, List<String> listTag, List<String> countTag) {
        this.context = context;
        this.listTag = listTag;
        this.countTag = countTag;
    }


    @NonNull
    @Override
    public ViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate
               (R.layout.tag_item,parent,false);
       return new TagAdapter.ViewHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHandler holder, int position) {
        holder.tag.setText("#"+listTag.get(position));
        holder.noOfPosts.setText(countTag.get(position)+" posts");

    }

    @Override
    public int getItemCount() {

        return listTag.size();
    }

    public  class ViewHandler extends RecyclerView.ViewHolder
    {
        private TextView tag;
        private TextView noOfPosts;

        public ViewHandler(@NonNull View itemView) {

            super(itemView);

            tag=itemView.findViewById(R.id.hash_tag);
            noOfPosts=itemView.findViewById(R.id.no_of_post);

        }
    }
    public void filters(List<String> filterTags,List<String>filterTagCount)

    {
this.listTag=filterTags;
this.countTag=filterTagCount;
notifyDataSetChanged();
    }


}
