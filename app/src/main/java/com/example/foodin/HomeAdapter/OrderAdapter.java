package com.example.foodin.HomeAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodin.HelperClass.OrderHelperClass;
import com.example.foodin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderHelperClass, OrderAdapter.OrderViewHolder> {

    Context context;

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderHelperClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderHelperClass model) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("orders");


        Integer tAmount = Integer.parseInt(model.getFoodQuantity()) * Integer.parseInt(model.getFoodPrice());
        holder.orderName.setText(model.getFoodName());
        holder.orderAmount.setText("₹" + Integer.toString(tAmount));
        holder.orderQty.setText(model.getFoodQuantity());

        Glide.with(holder.orderImage.getContext()).load(model.getFoodImage()).into(holder.orderImage);

        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = getRef(holder.getAdapterPosition()).getKey();//gets the key of the item clicked
                //deletes item from database and view
                reference.child(key).removeValue();
//                notifyDataSetChanged(); //to prevent IndexOutOfBoundException

            }
        });
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_design, parent, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView orderImage;
        TextView orderName, orderAmount, orderQty;
        LinearLayout cancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.orderImage);
            orderName = itemView.findViewById(R.id.orderName);
            orderAmount = itemView.findViewById(R.id.orderAmount);
            orderQty = itemView.findViewById(R.id.orderQty);
            cancelOrder = itemView.findViewById(R.id.cancelOrder);

        }
    }

}
