package com.myxspace.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity  extends AppCompatActivity {

    private Button btnSignUp, btnLogin;
    private EditText etName, etEmail, etPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.signup_now);
        etName = findViewById(R.id.name_signup);
        etEmail = findViewById(R.id.email_signup);
        etPassword = findViewById(R.id.pass_signup);
        btnLogin = findViewById(R.id.to_login_screen);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wait...");

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }

        progressDialog.hide();
        progressDialog.setTitle("Signing up");
        progressDialog.setCancelable(false);

        btnSignUp.setOnClickListener((view) -> {
            progressDialog.show();
            final String name = etName.getText().toString().trim();
            final String email = etEmail.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();

            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(name) || !TextUtils.isEmpty(password)) {
                if(password.length() > 8) {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                            String uid = auth.getCurrentUser().getUid();
                            Log.d(TAG, "onCreate: uid " + uid);
                            auth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            reference.child("name").setValue(name);
                            reference.child("email").setValue(email);

//                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("vendor").child(uid);
//                            reference1.setValue(false);
                            initSetup(uid);
                            progressDialog.dismiss();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Length of Password should be greater than 10", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener((view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }));

    }

    private void initSetup(String uid) {
        Map<String, Object> shop = new HashMap<>();
    }

}
