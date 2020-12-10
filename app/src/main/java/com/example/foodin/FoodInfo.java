package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodin.HelperClass.CartHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FoodInfo extends AppCompatActivity {

    Toolbar toolbar;
    ImageView foodImage;
    TextView foodName, foodRestaurant, foodPrice;
    //    TextInputLayout foodDeliveryAddress, foodDeliveryPhoneno;
    TextInputLayout foodQuantity;
    Button buttonCart, buttonOrder;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String uid, Tag, stringdeliveryQuantity,
            stringFoodImage, stringFoodName, stringFoodRestaurant, stringFoodPrice;

    String stringCartTotalAmount;
    Integer cartTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        stringFoodImage = getIntent().getStringExtra("image");
        stringFoodName = getIntent().getStringExtra("name");
        stringFoodRestaurant = getIntent().getStringExtra("restaurant");
        stringFoodPrice = getIntent().getStringExtra("price");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringCartTotalAmount = snapshot.child("cartTotalAmount").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });

        Glide.with(foodImage).load(stringFoodImage).into(foodImage);
        foodName.setText(stringFoodName);
        foodRestaurant.setText(stringFoodRestaurant);
        foodPrice.setText("₹" + stringFoodPrice + " for one");

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        foodRestaurant = findViewById(R.id.foodRestaurant);
        foodPrice = findViewById(R.id.foodPrice);
        foodQuantity = findViewById(R.id.foodQuantity);
        buttonCart = findViewById(R.id.buttonCart);
        buttonOrder = findViewById(R.id.buttonOrder);
    }


    //quantity validations
    private boolean validateQuantity() {
        String val = foodQuantity.getEditText().getText().toString();
        if (val.isEmpty()) {
            foodQuantity.setError("Field cannot be Empty");
            return false;
        } else if (Integer.parseInt(val) < 1) {
            foodQuantity.setError("Quantity cannot be less than 1");
            return false;
        } else if (Integer.parseInt(val) > 10) {
            foodQuantity.setError("Quantity cannot be greater than 10");
            return false;
        } else {
            foodQuantity.setError(null);
            foodQuantity.setErrorEnabled(false);
            return true;
        }
    }


    public void Order(View view) {
        if (!validateQuantity()) {
            return;
        } else {
            stringdeliveryQuantity = foodQuantity.getEditText().getText().toString();
            Intent i = new Intent(FoodInfo.this, PlaceOrder.class);
            i.putExtra("Image", stringFoodImage);
            i.putExtra("Name", stringFoodName);
            i.putExtra("Price", stringFoodPrice);
            i.putExtra("Restaurant", stringFoodRestaurant);
            i.putExtra("Quantity", stringdeliveryQuantity);
            i.putExtra("activity",  "FoodInfo");
            startActivity(i);

        }

    }

    public void AddToCart(View view) {

        if (!validateQuantity()) {
            return;
        } else {
            stringdeliveryQuantity = foodQuantity.getEditText().getText().toString();

            CartHelperClass cartHelperClass = new CartHelperClass(stringFoodImage, stringFoodName, stringFoodRestaurant,
                    stringFoodPrice, stringdeliveryQuantity);
            reference.child("cart").child("cart" + uid + System.currentTimeMillis()).setValue(cartHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        cartTotalAmount = Integer.parseInt(stringCartTotalAmount) + (Integer.parseInt(stringFoodPrice)*Integer.parseInt(stringdeliveryQuantity));
                        reference.child("cartTotalAmount").setValue(Integer.toString(cartTotalAmount));
                        Toast.makeText(FoodInfo.this, "Added to cart", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(FoodInfo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}