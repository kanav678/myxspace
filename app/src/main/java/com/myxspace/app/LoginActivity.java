package com.myxspace.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private String username, password;
    private EditText etUser, etPass;
    private Button btnLogin, btnSignUp;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.login_now);
        btnSignUp = findViewById(R.id.sign_up_screen);
        etUser = findViewById(R.id.email);
        etPass = findViewById(R.id.pass);

        progressDialog = new ProgressDialog(this);

        if(auth.getCurrentUser() != null ){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        progressDialog.setTitle("Signing in");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.hide();

        btnLogin.setOnClickListener((view) -> {
            progressDialog.show();
            username = etUser.getText().toString().trim();
            password = etPass.getText().toString().trim();

            if(!TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener((task) -> {
                            if(task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Successfully logged In!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Enter all the details!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUp.setOnClickListener((view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        }));
    }
}
