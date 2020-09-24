package com.example.chirp.profile;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.chirp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ConstraintLayout profile_constrainLayout;
    private ImageView profile_page_back_button, edit_name_btn_img, changeNameButton;
    private EditText edit_name_edtTxt;
    private TextView profile_userName;
    private Toolbar mToolbar;
    private View add_profile_picture_btn;
    private CircleImageView profileImage;
    private static final int GalleryPick = 1;
    private StorageTask uploadTask;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference UserProfileImageRef;
    private String currentUserID;
    private String visitID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();
        mToolbar = findViewById(R.id.profile_page_toolbar);
        setSupportActionBar(mToolbar);

        if (visitID.equals(currentUserID)) {
            changeName();
            changePhoto();
            RetrieveUserInfo(currentUserID);
        } else {
            RetrieveUserInfo(visitID);
            add_profile_picture_btn.setVisibility(View.GONE);
        }
        profile_page_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePhoto() {
        add_profile_picture_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1) // соотношение сторон 1 : 1
                        .start(ProfileActivity.this); // will use CROP_IMAGE_ACTIVITY_REQUEST_CODE
            }
        });

    }

    private void changeName() {
        profile_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name_btn_img.setVisibility(View.VISIBLE);
                edit_name_btn_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        profile_userName.setVisibility(View.GONE);
                        edit_name_btn_img.setVisibility(View.GONE);
                        edit_name_edtTxt.setHint(profile_userName.getText().toString());
                        edit_name_edtTxt.setSingleLine();
                        edit_name_edtTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
                        edit_name_edtTxt.setVisibility(View.VISIBLE);
                        changeNameButton.setVisibility(View.VISIBLE);
                        changeNameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newName = edit_name_edtTxt.getText().toString().trim();
                                if (!newName.trim().equals("")) {
                                    databaseReference.child("User").child(currentUserID).child("userName").setValue(newName);
                                    databaseReference.child("User").child(currentUserID).child("searchName").setValue(newName.toLowerCase());
                                    profile_userName.setVisibility(View.VISIBLE);
                                    edit_name_btn_img.setVisibility(View.GONE);
                                    edit_name_edtTxt.setVisibility(View.GONE);
                                    changeNameButton.setVisibility(View.GONE);
                                    Toast.makeText(ProfileActivity.this, "Name Changed to '" + newName + "'", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Type your name", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        profile_constrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_userName.setVisibility(View.VISIBLE);
                edit_name_btn_img.setVisibility(View.GONE);
                edit_name_edtTxt.setVisibility(View.GONE);
                changeNameButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                assert result != null;
                Uri resultUri = result.getUri();

                final StorageReference storageReference = UserProfileImageRef.child(currentUserID + ".jpg");
                uploadTask = storageReference.putFile(resultUri);
                Task urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String imageUrl = downloadUri.toString();

                            databaseReference.child("User").child(currentUserID).child("ProfileImageUrl").setValue(imageUrl);

                        } else {
                            // Handle failures
                            // ...
                            Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void RetrieveUserInfo(String id) {


        databaseReference.child("User").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && snapshot.hasChild("userName")) {
                    String retrievedUserName = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();

                    profile_userName.setText(retrievedUserName);
                } else {
                    Toast.makeText(ProfileActivity.this, "Name doesn't exist", Toast.LENGTH_SHORT).show();
                }
                String imageDownloadUrl = Objects.requireNonNull(snapshot.child("ProfileImageUrl").getValue()).toString();
                if (!imageDownloadUrl.equals("default")) {

                    Picasso.get().load(imageDownloadUrl).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void initialize() {

        visitID = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("id")).toString();
        profile_constrainLayout = findViewById(R.id.profile_constraintLayout);
        profile_userName = findViewById(R.id.profile_userName);
        profile_page_back_button = findViewById(R.id.profile_page_back_button);
        add_profile_picture_btn = findViewById(R.id.add_profile_picture);
        profileImage = findViewById(R.id.profile_image);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        edit_name_btn_img = findViewById(R.id.edit_name_btn_img);
        edit_name_edtTxt = findViewById(R.id.edit_name_edtTxt);
        changeNameButton = findViewById(R.id.changeNameBtn);

    }
}