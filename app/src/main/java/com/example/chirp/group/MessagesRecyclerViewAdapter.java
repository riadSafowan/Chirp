package com.example.chirp.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private ArrayList<Message> messages;
    private Context context;
    FirebaseAuth firebaseAuth;

    public MessagesRecyclerViewAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_right, parent, false);
            MessagesRecyclerViewAdapter.ViewHolder viewHolder = new MessagesRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
            MessagesRecyclerViewAdapter.ViewHolder viewHolder = new MessagesRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessagesRecyclerViewAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.chatDate.setText(messages.get(position).getChatDate());
        viewHolder.chatMessage.setText(messages.get(position).getChatMessage());
        viewHolder.chatName.setText(messages.get(position).getChatName());
        viewHolder.chatTime.setText(messages.get(position).getChatTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView chatDate;
        private TextView chatMessage;
        private TextView chatName;
        private TextView chatTime;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            chatDate = itemView.findViewById(R.id.chatDate);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            chatName = itemView.findViewById(R.id.chatName);
            chatTime = itemView.findViewById(R.id.chatTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (messages.get(position).getChatNameID().equals(currentUserID)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

