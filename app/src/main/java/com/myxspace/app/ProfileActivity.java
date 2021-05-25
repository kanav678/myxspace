package com.myxspace.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private CircleImageView civProfile;
    private TextView tvName, tvEmail;
    private Button btnLogout;

    private String name, email;
    private String uid;

    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        tvEmail = findViewById(R.id.tv_email);
        tvName = findViewById(R.id.tv_name);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);

        if(auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }else {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }

        reference.child("users")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = (String) snapshot.child("name").getValue();
                        email = (String) snapshot.child("email").getValue();

                        tvName.setText(name);
                        tvEmail.setText(email);

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}

