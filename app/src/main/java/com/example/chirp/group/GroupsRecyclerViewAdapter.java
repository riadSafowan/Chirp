package com.example.chirp.group;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirp.R;

import java.util.ArrayList;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Group> groups;
    private Context context;

    public GroupsRecyclerViewAdapter(ArrayList<Group> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        viewHolder.groups_name.setText(groups.get(position).getGroupsName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupsName", groups.get(position).getGroupsName());
                intent.putExtra("groupsID", groups.get(position).getGroupsID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView groups_name;
//
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
//        liuvev
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            groups_name = itemView.findViewById(R.id.groups_name);
        }
    }
}
