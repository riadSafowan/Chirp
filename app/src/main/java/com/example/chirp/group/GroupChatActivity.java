package com.example.chirp.group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;


public class GroupChatActivity extends AppCompatActivity {
    private DatabaseReference userDatabaseReference, databaseReference, messageKeyDatabaseReference;
    private FirebaseAuth firebaseAuth;
    //    private ScrollView myScrollView;
    private Toolbar group_page_toolbar;
    private ImageView group_back_button;
    private TextView groupName;
    private EditText text_input;
    private View text_send;
    MessagesRecyclerViewAdapter adapter;
    private String currentUserName, currentUserID, currentDate, currentTime, groupsID;

    private RecyclerView recyclerView;
    private ArrayList<Message> messages = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        recyclerView = findViewById(R.id.messages_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessagesRecyclerViewAdapter(messages, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        init();
        userInfo();

        text_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!messages.isEmpty()) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(messages.size() - 1);
                        }
                    });
                }
                return false;
            }
        });

        text_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToDataBase();
                text_input.setText("");
                text_input.setSelected(true);
            }
        });

        group_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    displayMessages(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    displayMessages(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();
        while (iterator.hasNext()) {

            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatNameID = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();
            messages.add(new Message(chatDate, chatMessage, chatName, chatNameID, chatTime));
        }
        adapter.notifyDataSetChanged();

        if (!messages.isEmpty()) {

            recyclerView.scrollToPosition(messages.size() - 1);

        }
//        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
//        recyclerView.smoothScrollToPosition(messages.size()-1);
    }

    private void sendMessageToDataBase() {

        String message = text_input.getText().toString();
        String adjusted = message.replaceAll("(?m)^[ \t]*\r?\n", "");
        String messageKey = databaseReference.push().getKey();

        if (!adjusted.trim().equals("")) {

            Calendar date = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = dateFormat.format(date.getTime());

            Calendar time = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
            currentTime = timeFormat.format(time.getTime());
            messageKeyDatabaseReference = databaseReference.child(messageKey);

            HashMap<String, Object> messageInfo = new HashMap<>();
            messageInfo.put("name", currentUserName);
            messageInfo.put("senderID", currentUserID);
            messageInfo.put("message", adjusted);
            messageInfo.put("date", currentDate);
            messageInfo.put("time", currentTime);


            messageKeyDatabaseReference.updateChildren(messageInfo);
        }
    }

    private void userInfo() {
        userDatabaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUserName = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {

        group_page_toolbar = findViewById(R.id.group_page_toolbar);
        setSupportActionBar(group_page_toolbar);
        group_back_button = findViewById(R.id.group_back_button);
        groupName = findViewById(R.id.groupName);
        text_input = findViewById(R.id.text_input);
        text_send = findViewById(R.id.text_send);

        String groupsName = "Groups name Not Set";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupsName = bundle.getString("groupsName");
            groupsID = bundle.getString("groupsID");
        }
        groupName.setText(groupsName);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        assert groupsName != null;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Group").child(groupsID).child("Message");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }
}