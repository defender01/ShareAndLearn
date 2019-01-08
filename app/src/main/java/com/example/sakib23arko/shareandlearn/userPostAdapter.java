package com.example.sakib23arko.shareandlearn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class userPostAdapter extends RecyclerView.Adapter<userPostAdapter.userPostViewHolder> {
    private String[] mDataset;
    private Context context;
    private List<infoOfUser> infoList;
    private int pos;
    private String vis;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class userPostViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView textViewTitle, textViewDescription, textViewDateTime, textViewUserName;
        public ImageView samplePostImageView;

        public userPostViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.ProjectTitleID);
            textViewDescription = itemView.findViewById(R.id.InfoID);
            textViewDateTime = itemView.findViewById(R.id.TimeID);
            textViewUserName = itemView.findViewById(R.id.samplePostTextViewUserID);
            samplePostImageView = itemView.findViewById(R.id.samplePostProPicID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetails.class);
                    //Get the value of the item you clicked

                    infoOfUser itemClicked = infoList.get(getAdapterPosition());
                    intent.putExtra("Title", itemClicked.getTitle());
                    intent.putExtra("Timee", itemClicked.getDateTime());
                    intent.putExtra("Description", itemClicked.getDescription());
                    intent.putExtra("Tag", itemClicked.getTag());
                    intent.putExtra("Chosen", itemClicked.getchosenFileUrl());
                    intent.putExtra("Ext", itemClicked.getExt());
                    intent.putExtra("UID", itemClicked.getUserUid());
                    intent.putExtra("Name", itemClicked.getUserName());
                    intent.putExtra("PostID", itemClicked.getPostID());
                    if (vis.equals("from userProfile"))
                        intent.putExtra("vis", "edit");
                    else
                        intent.putExtra("vis", "no edit");
                    Log.d("postt", "userPostAdapter " + itemClicked.postID + "  " + itemClicked.title + " " + itemClicked.description);
                    context.startActivity(intent);
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public userPostAdapter(Context context, List<infoOfUser> infoList) {
        this.context = context;
        this.infoList = infoList;
        vis = "";
    }

    // Create new views (invoked by the layout manager)
    @Override
    public userPostAdapter.userPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context).inflate(R.layout.sample_post, parent, false);
        return new userPostViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(userPostViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        pos = position;
        final infoOfUser currentInfo = infoList.get(position);

        String showDescription = "", realDescription = currentInfo.getDescription();

        for (int i = 0; i < Math.min(200, realDescription.length()); i++)
            showDescription += realDescription.charAt(i);

        if(realDescription.length()>200)showDescription+="...";

        holder.textViewDateTime.setText(currentInfo.getDateTime());
        holder.textViewDescription.setText(showDescription);
        holder.textViewTitle.setText(currentInfo.getTitle());
        holder.textViewUserName.setText(currentInfo.getUserName());

        StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference("profilePictures/" + currentInfo.getUserUid() + ".jpg");

        GlideApp.with(context/* context */)
                .load(userProfileImageRef)
                .into(holder.samplePostImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
}