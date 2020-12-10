package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodin.HelperClass.CartHelperClass;
import com.example.foodin.HelperClass.OrderHelperClass;
import com.example.foodin.HomeAdapter.CartAdapter;
import com.example.foodin.HomeAdapter.OrderAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvCart;
    TextView cartTotalAmount;
    Button buttonOrder;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    CartAdapter cartAdapter;

    String uid, Tag, stringTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        //TotalPrice
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringTotalAmount = snapshot.child("cartTotalAmount").getValue(String.class);
                cartTotalAmount.setText("₹" + stringTotalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });

        //RecyclerView
        cartRecycler();
        cartAdapter.startListening();

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        rvCart = findViewById(R.id.rvCart);
        cartTotalAmount = findViewById(R.id.cartTotalAmount);
        buttonOrder = findViewById(R.id.buttonOrder);
    }

    private void cartRecycler() {
        rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
        FirebaseRecyclerOptions<CartHelperClass> Foptions =
                new FirebaseRecyclerOptions.Builder<CartHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("cart"), CartHelperClass.class)
                        .build();
        cartAdapter = new CartAdapter(Foptions, Cart.this);
        rvCart.setAdapter(cartAdapter);
    }

    public void Order(View view) {
        if (cartAdapter.getItemCount() == 0) {
            Toast.makeText(this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Cart.this, PlaceOrder.class);
            i.putExtra("activity", "Cart");
            i.putExtra("totalAmount", stringTotalAmount);
            System.out.println("......................................................................." +
                    "cartAdapter.getItemCount() : " + cartAdapter.getItemCount());
            startActivity(i);
        }
    }
}

