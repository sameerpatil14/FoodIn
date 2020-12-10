package com.example.foodin.HomeAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodin.HelperClass.CartHelperClass;
import com.example.foodin.HelperClass.OrderHelperClass;
import com.example.foodin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends FirebaseRecyclerAdapter<CartHelperClass, CartAdapter.CartViewHolder> {
    Context context;
    public int TotalAmount = 0;
    public String stringTotalAmount;
    String Tag;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartHelperClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartHelperClass model) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());


        int cAmount = Integer.parseInt(model.getFoodPrice()) * Integer.parseInt(model.getFoodQuantity());
        holder.cartName.setText(model.getFoodName());
        holder.cartItemAmount.setText("₹" + String.valueOf(cAmount));
        holder.cartQty.setText(model.getFoodQuantity());

        Glide.with(holder.cartImage.getContext()).load(model.getFoodImage()).into(holder.cartImage);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringTotalAmount = snapshot.child("cartTotalAmount").getValue(String.class);
                TotalAmount = Integer.parseInt(stringTotalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });

        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(holder.getAdapterPosition()).getKey();//gets the key of the item clicked

                TotalAmount = (TotalAmount - (Integer.parseInt(model.getFoodPrice()) * Integer.parseInt(model.getFoodQuantity())));
                stringTotalAmount = Integer.toString(TotalAmount);
                reference.child("cartTotalAmount").setValue(stringTotalAmount);

                reference.child("cart").child(key).removeValue();//deletes item from database and view

//                notifyDataSetChanged(); //to prevent IndexOutOfBoundException
            }
        });

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_design, parent, false);
        CartAdapter.CartViewHolder cartViewHolder = new CartAdapter.CartViewHolder(view);
        return cartViewHolder;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImage;
        TextView cartName, cartItemAmount;
        TextView cartQty;
        LinearLayout removeFromCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImage);
            cartName = itemView.findViewById(R.id.cartName);
            cartQty = itemView.findViewById(R.id.cartQty);
            cartItemAmount = itemView.findViewById(R.id.cartItemAmount);
            removeFromCart = itemView.findViewById(R.id.removeFromCart);
        }
    }
}