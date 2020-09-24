package com.example.chirp.login;

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

import com.example.chirp.main.MainActivity;
import com.example.chirp.R;
import com.example.chirp.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText edtTxtNumber, edtTxtPassword;
    private TextView txtWarnNumber, txtWarnPassword, txtWrong, txtForget;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        initialize();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        if (validateData()) {

            String phoneNumber = edtTxtNumber.getText().toString();
            String password = edtTxtPassword.getText().toString();

            progressDialog.setTitle("Signing In");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(phoneNumber, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        SendUserToMainActivity();
                    } else {
                        progressDialog.dismiss();
                        String errorMessage = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(LoginActivity.this, "Faild:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validateData() {

        if (edtTxtNumber.getText().toString().equals("")) {
            txtWarnNumber.setVisibility(View.VISIBLE);

            return false;
        } else {
            txtWarnNumber.setVisibility(View.INVISIBLE);
        }
        if (edtTxtPassword.getText().toString().equals("")) {
            txtWarnPassword.setVisibility(View.VISIBLE);

            return false;
        } else {
            txtWarnPassword.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    private void initialize() {

        edtTxtNumber = findViewById(R.id.edtTxtNumber);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);

        txtWarnNumber = findViewById(R.id.txtWarnNumber);
        txtWarnPassword = findViewById(R.id.txtWarnPassword);
        txtWrong = findViewById(R.id.txtWrong);
        txtForget = findViewById(R.id.txtForget);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
    }

    private void SendUserToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void SendUserToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}