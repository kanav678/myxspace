package com.myxspace.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myxspace.app.MainActivity.RENTAL_ID;

public class RentalActivity extends AppCompatActivity {

    private static final String TAG="RentalActivity";

    private TextView tvRentalName, tvRating, tvRentalDuration, tvRentalCapacity;
    private TextView tvRentalPrice, tvHost, tvRentalDescription;
    private CircleImageView civHostPhoto;
    private ImageView ivRentalImage;
    private RecyclerView rvServices;
    private Button btnBook;

    private RVAdapter adapter;

    private Bundle bundle;
    private String uid;
    private String rid; //Rental ID
    private Rental rental;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rental);

        bundle = getIntent().getExtras();
        rid = bundle.getString(RENTAL_ID);
        Log.d(TAG, "onCreate: " + rid);

        rental = new Rental();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("rentals");

        tvRentalName = findViewById(R.id.tv_RName);
        tvRating = findViewById(R.id.tv_rating);
        tvRentalDuration = findViewById(R.id.tv_duration);
        tvRentalCapacity = findViewById(R.id.tv_capacity);
        tvRentalPrice = findViewById(R.id.tv_price);
        tvHost = findViewById(R.id.tv_HostName);
        tvRentalDescription = findViewById(R.id.tv_RDescription);
        civHostPhoto = findViewById(R.id.civ_HostPhoto);
        ivRentalImage = findViewById(R.id.iv_RImage);
        rvServices = findViewById(R.id.rv_services);
        btnBook = findViewById(R.id.btn_book);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting details");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        if(auth.getCurrentUser() == null) {
            Intent i = new Intent(RentalActivity.this,LoginActivity.class);
            startActivity(i);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
        uid = auth.getCurrentUser().getUid();


        if(rid != null) {
            Log.d(TAG, "onCreate: " + rid);
        reference.child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rental = snapshot.getValue(Rental.class);
                Log.d(TAG, "onDataChange: " + rental.getRentalImageUrl());

                tvRentalName.setText(rental.getName());
                tvRentalCapacity.setText(String.valueOf(rental.getCapacity()));
                tvRentalDescription.setText(rental.getDescription());
                tvRentalPrice.setText(getResources().getString(R.string.price, rental.getPrice()));
                tvRentalDuration.setText(getResources().getString(R.string.duration, rental.getDuration()));
                tvRating.setText(String.valueOf(rental.getRating()));
                tvHost.setText(getResources().getString(R.string.hostName, rental.getHost()));

                Glide.with(RentalActivity.this)
                        .load(rental.getRentalImageUrl())
                        .placeholder(R.drawable.blacked_bg)
                        .into(ivRentalImage);

                Glide.with(RentalActivity.this)
                        .load(rental.getHostPhotoUrl())
                        .placeholder(R.drawable.user)
                        .into(civHostPhoto);


                //RV setup
                rvServices.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                adapter = new RVAdapter(rental.getServices());
                rvServices.setAdapter(adapter);


                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBook.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:9123169564"));
            startActivity(intent);
        });

        }
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

        List<String> services;

        public RVAdapter(List<String> services) {
            this.services = services;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.rv_service_view, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvService.setText(services.get(position));
            Random random = new Random();
            holder.cardView.setCardBackgroundColor(
                    Color.argb(
                            255,
                            random.nextInt(226),
                            random.nextInt(226),
                            random.nextInt(226)
                            )
            );

        }


        @Override
        public int getItemCount() {
            return services.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvService;
            private CardView cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                cardView = itemView.findViewById(R.id.cardView);
                tvService = itemView.findViewById(R.id.tv_service);
            }
        }

    }

}