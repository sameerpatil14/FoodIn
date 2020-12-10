package com.example.foodin.HomeAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodin.FoodInfo;
import com.example.foodin.HomeActivity;
import com.example.foodin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FoodListAdapter extends FirebaseRecyclerAdapter<FoodListHelperClass, FoodListAdapter.FoodListViewHolder> {

    Context context;

    public FoodListAdapter(@NonNull FirebaseRecyclerOptions<FoodListHelperClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodListViewHolder holder, int position, @NonNull FoodListHelperClass model) {

        holder.foodName.setText(model.getFoodName());
        holder.restaurant.setText(model.getRestaurant());
        holder.foodPrice.setText("₹" + model.getFoodPrice() + " for one");

        Glide.with(holder.foodImage.getContext()).load(model.getFoodImage()).into(holder.foodImage);
        holder.cvFoodItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //sharedAnimation
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(holder.foodImage, "tImage");
                pairs[1] = new Pair<View, String>(holder.foodName, "tName");
                pairs[2] = new Pair<View, String>(holder.restaurant, "tRestaurant");
                pairs[3] = new Pair<View, String>(holder.foodPrice, "tPrice");
                Intent i = new Intent(context, FoodInfo.class);
                i.putExtra("image",model.getFoodImage());
                i.putExtra("name",model.getFoodName());
                i.putExtra("restaurant",model.getRestaurant());
                i.putExtra("price",model.getFoodPrice());
                ActivityOptions Aoptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                context.startActivity(i, Aoptions.toBundle());
            }
        });
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items_card, parent, false);
        FoodListViewHolder foodListViewHolder = new FoodListViewHolder(view);
        return foodListViewHolder;
    }


    //VIEW HOLDER CLASS
    public class FoodListViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName, restaurant, foodPrice;
        CardView cvFoodItem;

        public FoodListViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            restaurant = itemView.findViewById(R.id.restaurant);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            cvFoodItem = itemView.findViewById(R.id.cvFoodItem);

        }
    }
}