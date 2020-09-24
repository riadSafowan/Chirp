package com.example.chirp.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chirp.R;
import com.example.chirp.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText regiEdtTxtName, regiEdtTxtNumber, regiEdtTxtPassword, regiEdtTxtRePassword;
    private Button regiBtnSubmit;
    private TextView regiWarnName, regiWarnNumber, regiWarnPassword, regiWarnRePassword, regiWarnIncorrect, regiWarnNumberExist, regiWarnPasswordSize;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);

        initialize();

        regiBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        if (validateData()) {
            CreateNewAccount();
        }
    }

    private void CreateNewAccount() {

        final String userName = regiEdtTxtName.getText().toString();
        final String phoneNumber = regiEdtTxtNumber.getText().toString();
        final String password = regiEdtTxtPassword.getText().toString();

        progressDialog.setTitle("Creating a new account");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(phoneNumber, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                    HashMap<String, String> profileMap = new HashMap<>();
                    profileMap.put("userID", currentUserID);
                    profileMap.put("userName", userName);
                    profileMap.put("Email", phoneNumber);
                    profileMap.put("Password", password);
                    profileMap.put("searchName", userName.toLowerCase());
                    profileMap.put("ProfileImageUrl", "default");

                    databaseReference.child("User").child(currentUserID).setValue(profileMap);
                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivity.this, "Your account has been created successfully", Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();
                } else {
                    progressDialog.dismiss();
                    String errorMessage = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(RegisterActivity.this, "Field:" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validateData() {
        if (regiEdtTxtName.getText().toString().equals("")) {
            regiWarnName.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnName.setVisibility(View.INVISIBLE);
        }
        if (regiEdtTxtNumber.getText().toString().equals("")) {
            regiWarnNumber.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnNumber.setVisibility(View.INVISIBLE);
        }
        if (regiEdtTxtPassword.getText().toString().equals("")) {
            regiWarnPassword.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnPassword.setVisibility(View.INVISIBLE);
        }

        if (regiEdtTxtPassword.getText().length() < 6) {
            regiWarnPasswordSize.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnPasswordSize.setVisibility(View.INVISIBLE);
        }


        if (regiEdtTxtRePassword.getText().toString().equals("")) {
            regiWarnRePassword.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnRePassword.setVisibility(View.INVISIBLE);
        }

        if (!regiEdtTxtRePassword.getText().toString().equals(regiEdtTxtPassword.getText().toString())) {
            regiWarnIncorrect.setVisibility(View.VISIBLE);

            return false;
        } else {
            regiWarnIncorrect.setVisibility(View.INVISIBLE);
        }


        return true;
    }

    private void initialize() {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        regiEdtTxtName = findViewById(R.id.regiEdtTxtName);
        regiEdtTxtNumber = findViewById(R.id.regiEdtTxtNumber);
        regiEdtTxtPassword = findViewById(R.id.regiEdtTxtPassword);
        regiEdtTxtRePassword = findViewById(R.id.regiEdtTxtRePassword);

        regiBtnSubmit = findViewById(R.id.regiBtnSubmit);

        regiWarnName = findViewById(R.id.regiWarnName);
        regiWarnNumber = findViewById(R.id.regiWarnNumber);
        regiWarnPassword = findViewById(R.id.regiWarnPassword);
        regiWarnRePassword = findViewById(R.id.regiWarnRePassword);
        regiWarnIncorrect = findViewById(R.id.regiWarnIncorrect);
        regiEdtTxtName = findViewById(R.id.regiEdtTxtName);
        regiWarnNumberExist = findViewById(R.id.regiWarnNumberExist);
        regiWarnPasswordSize = findViewById(R.id.regiWarnPasswordSize);

        progressDialog = new ProgressDialog(this);

    }

    private void SendUserToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}