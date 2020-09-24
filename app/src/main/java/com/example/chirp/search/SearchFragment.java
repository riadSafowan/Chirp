package com.example.chirp.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirp.search.findFriends.Profile;
import com.example.chirp.R;
import com.example.chirp.search.findFriends.FindFriendsRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private FindFriendsRecyclerViewAdapter adapter;
    private ArrayList<Profile> profiles = new ArrayList<>();
    private DatabaseReference databaseReference;
    private EditText find_friends_search_input;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        init(view);

        find_friends_search_input.setSingleLine();
        searchUser("");
        find_friends_search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchUser(s.toString().toLowerCase());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void searchUser(String s) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("searchName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                profiles.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Profile profile = dataSnapshot.getValue(Profile.class);

                    assert firebaseUser != null;
                    assert profile != null;
                    if (!profile.getUserID().equals(firebaseUser.getUid())) {
                        profiles.add(profile);
                    }
                }


                adapter = new FindFriendsRecyclerViewAdapter(profiles, getContext());
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(View view) {
        find_friends_search_input = view.findViewById(R.id.find_friends_search_input);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        recyclerView = view.findViewById(R.id.search_result_recyclerView);




    }


}