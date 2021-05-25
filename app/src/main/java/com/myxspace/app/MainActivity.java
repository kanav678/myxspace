package com.myxspace.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String RENTAL_ID = "rentalID";

    private CardView cvJharkhand, cvKerala, cvDelhi, cvAll, rent1,  rent2,  rent3, rent4;
    private FirebaseAuth auth;
    private String uid;
    private TextView name;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private CircleImageView userImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.greeting);
        cvDelhi = findViewById(R.id.cv_delhi);
        cvJharkhand = findViewById(R.id.cv_jharkhand);
        cvKerala = findViewById(R.id.cv_kerala);
        cvAll = findViewById(R.id.cv_all);
        rent1 = findViewById(R.id.rent1);
        rent2 = findViewById(R.id.rent2);
        rent3 = findViewById(R.id.rent3);
        rent4 = findViewById(R.id.rent4);
        userImg = findViewById(R.id.iv_user);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Personalizing Dashboard");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (auth.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
        else {
            uid = auth.getCurrentUser().getUid();
            progressDialog.dismiss();
        }

        reference.child("users")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("name").getValue();
                name.setText(getResources().getString(R.string.greeting, username));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Thread t = new Thread(() -> {

            cvAll.setOnClickListener(view -> {
                rent1.setVisibility(View.VISIBLE);
                rent2.setVisibility(View.VISIBLE);
                rent3.setVisibility(View.VISIBLE);
                rent4.setVisibility(View.VISIBLE);
            });

            cvJharkhand.setOnClickListener(view -> {
                rent1.setVisibility(View.VISIBLE);
                rent2.setVisibility(View.VISIBLE);
                rent3.setVisibility(View.GONE);
                rent4.setVisibility(View.GONE);
            });

            cvKerala.setOnClickListener(view -> {
                rent1.setVisibility(View.GONE);
                rent2.setVisibility(View.GONE);
                rent3.setVisibility(View.GONE);
                rent4.setVisibility(View.VISIBLE);
            });

            cvDelhi.setOnClickListener(view -> {
                rent1.setVisibility(View.GONE);
                rent2.setVisibility(View.GONE);
                rent3.setVisibility(View.VISIBLE);
                rent4.setVisibility(View.GONE);
            });

        });
        t.start();



        rent1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RentalActivity.class);
            intent.putExtra( RENTAL_ID,"1");
            startActivity(intent);
        });

        rent2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RentalActivity.class);
            intent.putExtra( RENTAL_ID,"2");
            startActivity(intent);
        });

        rent3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RentalActivity.class);
            intent.putExtra( RENTAL_ID,"3");
            startActivity(intent);
        });

        rent4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RentalActivity.class);
            intent.putExtra( RENTAL_ID,"4");
            startActivity(intent);
        });


        userImg.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(i);
        });

    }
}

