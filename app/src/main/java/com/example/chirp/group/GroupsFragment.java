package com.example.chirp.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GroupsFragment extends Fragment {

    private FloatingActionButton create_new_group;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private GroupsRecyclerViewAdapter adapter;
    private ArrayList<Group> groups = new ArrayList<>();
    private String userName, creatorsID, gName, gID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        init(view);
        gettingUserInfo();

        RetrieveAndDisplayGroups();

        recyclerView = view.findViewById(R.id.groups_recyclerView);
        adapter = new GroupsRecyclerViewAdapter(groups, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewGroup();
            }
        });

        return view;
    }

    private void RetrieveAndDisplayGroups() {

        databaseReference.child("Group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groups.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    gName = Objects.requireNonNull(dataSnapshot.child("groupsName").getValue()).toString();
                    gID = Objects.requireNonNull(dataSnapshot.child("groupsID").getValue()).toString();
                    groups.add(new Group(gName, gID));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void gettingUserInfo() {
        creatorsID = FirebaseAuth.getInstance().getUid();
        databaseReference.child("User").child(creatorsID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CreateNewGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Group Name: ");

        final EditText newGroupName = new EditText(getActivity());
        newGroupName.setHint("Group name...");
        newGroupName.setSingleLine();
//        int maxLength = 50;
//        newGroupName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        builder.setView(newGroupName);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = newGroupName.getText().toString();

                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(getActivity(), "Enter Group name", Toast.LENGTH_SHORT).show();
                } else {

                    createGroup(groupName);

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    private void createGroup(String name) {
        String groupIDKeyRef = databaseReference.child("Group").push().getKey();

//        ArrayList<Group>newGroup=new ArrayList<>();
//        newGroup.add(new Group( name , groupIDKeyRef ,userName,creatorsID, "msg"));

        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("groupsName", name);
        profileMap.put("creatorsName", userName);
        profileMap.put("creatorsID", creatorsID);
        profileMap.put("groupsID", groupIDKeyRef);

//      databaseReference.child("Group").child(groupIDKeyRef).setValue(profileMap);
        assert groupIDKeyRef != null;
        databaseReference.child("Group").child(groupIDKeyRef).updateChildren(profileMap);

    }


    private void init(View view) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        create_new_group = view.findViewById(R.id.create_new_group);
    }


}