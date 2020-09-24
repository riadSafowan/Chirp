package com.example.chirp.search.findFriends;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chirp.profile.ProfileActivity;
import com.example.chirp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsRecyclerViewAdapter extends RecyclerView.Adapter<FindFriendsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Profile> profiles;
    private Context context;

    public FindFriendsRecyclerViewAdapter(ArrayList<Profile> profiles, Context context) {
        this.profiles = profiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_friend_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String url = profiles.get(position).getProfileImageUrl();
        viewHolder.name.setText(profiles.get(position).getUserName());

        if (!(url.equals("default"))) {
            viewHolder.imageView.setBorderOverlay(true);
            Picasso.get().load(url).into(viewHolder.imageView);

        }
        viewHolder.imageView.setBorderOverlay(false);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = profiles.get(position).getUserID();
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CircleImageView imageView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.find_friend_textView_name);
            imageView = itemView.findViewById(R.id.find_friend_image);
        }
    }
}