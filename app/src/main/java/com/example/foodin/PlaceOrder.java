package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodin.HelperClass.OrderHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceOrder extends AppCompatActivity {

    Toolbar toolbar;
    //DeliveryDetails
    TextView usersName;
    TextInputLayout foodDeliveryAddress, foodDeliveryPhoneno;
    Button buttonUpdateDeliveryDetails;

    //PriceDetails
    TextView amount, deliveryCharges, totalPayableAmount;

    //PlaceOrder
    Button buttonPlaceOder;

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    //foodinfo
    String uid, Tag, stringUsersName, stringdeliveryAddress, stringdeliveryPhoneno, stringFoodImage, stringFoodName, stringFoodRestaurant,
            stringdeliveryQuantity, stringFoodPrice, stringdeliveryStatus = "Order has been placed";
    Integer IntAmount = 0, TotalAmount = 0, IntQuantity = 0;
    //Cart
    String stringTotalAmount;
    String currentTimeMillis, stringActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

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
        uid = firebaseAuth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());

        stringActivity = getIntent().getStringExtra("activity");

        if (stringActivity.equals("FoodInfo")) {

            //GET STRING EXTRAS
            stringFoodImage = getIntent().getStringExtra("Image");
            stringFoodName = getIntent().getStringExtra("Name");
            stringFoodRestaurant = getIntent().getStringExtra("Restaurant");
            stringdeliveryQuantity = getIntent().getStringExtra("Quantity");
            stringFoodPrice = getIntent().getStringExtra("Price");

            //Price Details Data
            IntQuantity = Integer.parseInt(stringdeliveryQuantity);
            IntAmount = Integer.parseInt(stringFoodPrice);

            TotalAmount = IntQuantity * IntAmount;
        } else if (stringActivity.equals("Cart")) {

            //GET STRING EXTRAS
            stringTotalAmount = getIntent().getStringExtra("totalAmount");
            //Price Details
            TotalAmount = Integer.parseInt(stringTotalAmount);
        }

        loadDeliveryDetails();

        priceDetails();

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        usersName = findViewById(R.id.usersName);
        foodDeliveryAddress = findViewById(R.id.foodDeliveryAddress);
        foodDeliveryPhoneno = findViewById(R.id.foodDeliveryPhoneno);
        buttonUpdateDeliveryDetails = findViewById(R.id.buttonUpdateDeliveryDetails);

        amount = findViewById(R.id.amount);
        deliveryCharges = findViewById(R.id.deliveryCharges);
        totalPayableAmount = findViewById(R.id.totalPayableAmount);

        buttonPlaceOder = findViewById(R.id.buttonPlaceOrder);
    }

    //Load Delivery Details
    private void loadDeliveryDetails() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringUsersName = snapshot.child("name").getValue(String.class);
                stringdeliveryAddress = snapshot.child("address").getValue(String.class);
                stringdeliveryPhoneno = snapshot.child("phoneno").getValue(String.class);

                usersName.setText(stringUsersName);
                foodDeliveryAddress.getEditText().setText(stringdeliveryAddress);
                foodDeliveryPhoneno.getEditText().setText(stringdeliveryPhoneno);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });
    }

    //Price Details
    private void priceDetails() {
        amount.setText(Integer.toString(TotalAmount));
        if (TotalAmount < 100) {
            deliveryCharges.setText("35");
            TotalAmount = TotalAmount + 35;
            totalPayableAmount.setText(Integer.toString(TotalAmount));
        } else {
            deliveryCharges.setText("FREE");
            totalPayableAmount.setText(Integer.toString(TotalAmount));
        }
    }

    public void UpdateDeliveryDetails(View view) {
        if (!validateAddress() | !validatePhoneno()) {
            return;
        } else {
            isAddressChanged();
            isPhonenoChanged();
        }
    }

    //Phone Validations
    private boolean validatePhoneno() {
        String val = foodDeliveryPhoneno.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\d{10,10}\\z";
        if (val.isEmpty()) {
            foodDeliveryPhoneno.setError("Field cannot be Empty");
            return false;
        } else if (val.length() < 10) {
            foodDeliveryPhoneno.setError("Enter 10 digit mobile no.");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            foodDeliveryPhoneno.setError("Only digits are allowed");
            return false;
        } else {
            foodDeliveryPhoneno.setError(null);
            foodDeliveryPhoneno.setErrorEnabled(false);
            return true;
        }
    }

    //address validations
    private boolean validateAddress() {
        String val = foodDeliveryAddress.getEditText().getText().toString();
        if (val.isEmpty()) {
            foodDeliveryAddress.setError("Field cannot be Empty");
            return false;
        } else {
            foodDeliveryAddress.setError(null);
            foodDeliveryAddress.setErrorEnabled(false);
            return true;
        }
    }

    //If Address is changed update to firebase
    private boolean isAddressChanged() {
        if (!stringdeliveryAddress.equals(foodDeliveryAddress.getEditText().getText().toString())) {
            reference.child("address").setValue(foodDeliveryAddress.getEditText().getText().toString());
            stringdeliveryAddress = foodDeliveryAddress.getEditText().getText().toString();
            Toast.makeText(this, "Address Updated", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    //If Phone Number is changed update to firebase
    private boolean isPhonenoChanged() {
        if (!stringdeliveryPhoneno.equals(foodDeliveryPhoneno.getEditText().getText().toString())) {
            reference.child("phoneno").setValue(foodDeliveryPhoneno.getEditText().getText().toString());
            stringdeliveryPhoneno = foodDeliveryPhoneno.getEditText().getText().toString();
            Toast.makeText(this, "Phone Number Updated", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public void placeOrder(View view) {

        if (stringActivity.equals("FoodInfo")) {
            updateOrderToDatabase();
            Intent i = new Intent(PlaceOrder.this, Orders.class);
            startActivity(i);
            finishAffinity();
        } else if (stringActivity.equals("Cart")) {
            loadCartData();
            reference.child("cartTotalAmount").setValue("0");
            Intent i = new Intent(PlaceOrder.this, Orders.class);
            startActivity(i);
            finishAffinity();
        }
    }

    private void loadCartData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid()).child("cart");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String k = ds.getKey();

                    stringFoodImage = ds.child("foodImage").getValue(String.class);
                    stringFoodName = ds.child("foodName").getValue(String.class);
                    stringFoodPrice = ds.child("foodPrice").getValue(String.class);
                    stringdeliveryQuantity = ds.child("foodQuantity").getValue(String.class);
                    stringFoodRestaurant = ds.child("restaurant").getValue(String.class);
                    ref.child(k).removeValue();
                    updateOrderToDatabase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });
    }

    private void updateOrderToDatabase() {
        currentTimeMillis = String.valueOf(System.currentTimeMillis());
        OrderHelperClass orderHelperClass = new OrderHelperClass(stringFoodImage, stringFoodName, stringFoodRestaurant,
                stringFoodPrice, stringdeliveryQuantity, stringdeliveryAddress, stringdeliveryPhoneno, stringdeliveryStatus, currentTimeMillis);
        reference.child("orders").child(uid + currentTimeMillis).setValue(orderHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PlaceOrder.this, "Order Placed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlaceOrder.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
//        saveCurrentDateTime = currentDateTime.format(cal.getTime());