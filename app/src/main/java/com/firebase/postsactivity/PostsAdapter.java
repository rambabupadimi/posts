package com.firebase.postsactivity;

/**
 * Created by user on 24-09-2016.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.CustomViewHolder>{
        private List<PostsModel> websiteModalList;
        private Context mContext;
        private String TAG = "PostsAdapter.java";
    private List<PostsModel> filtered_items = new ArrayList<>();

    FileOutputStream fileOutputStream;



    public PostsAdapter(Context context, List<PostsModel> websiteModalList) {
        this.mContext       =   context;
        this.filtered_items =   websiteModalList;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_adapter, null);
        CustomViewHolder holder=new CustomViewHolder(view);
        return  holder;

    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {


        final PostsModel postsModel= filtered_items.get(position);
        holder.title.setText(postsModel.getTitle());
        holder.description.setText(postsModel.getDescription());

        Log.i(TAG,"title"+postsModel.getTitle());
        Log.i(TAG,"description"+postsModel.getDescription());
        Log.i(TAG,"image url"+postsModel.getImgUrl());

        if(postsModel.getImgUrl().equalsIgnoreCase("null") || postsModel.getImgUrl().equals("") ) {
            holder.imageView.setImageResource(R.drawable.two);
        }
        else
        {

               holder.imageView.setImageBitmap(BitmapFactory.decodeFile(postsModel.getImgUrl()));

        }


    }


    @Override
    public int getItemCount() {
     //   return (null != filtered_items ? filtered_items.size() : 0);
        return (null != filtered_items ? filtered_items.size() : 0);

  //      return 5;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,description;
        ResizableImageView imageView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.title=(TextView)itemView.findViewById(R.id.post_title);
            this.description=(TextView) itemView.findViewById(R.id.post_description);
            this.imageView  =(ResizableImageView) itemView.findViewById(R.id.post_image);
        }
    }



}
