package com.example.foodin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodin.HelperClass.OrderHelperClass;
import com.example.foodin.HomeAdapter.OrderAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Orders extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvOrders;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    OrderAdapter orderAdapter;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        init();

        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("orders");

        //RecyclerView
        orderRecycler();
        orderAdapter.startListening();

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        rvOrders = findViewById(R.id.rvOrders);
    }

    private void orderRecycler() {

        rvOrders.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
        FirebaseRecyclerOptions<OrderHelperClass> Foptions =
                new FirebaseRecyclerOptions.Builder<OrderHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("orders"), OrderHelperClass.class)
                        .build();
        orderAdapter = new OrderAdapter(Foptions, Orders.this);
        rvOrders.setAdapter(orderAdapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finishAffinity();
    }
}