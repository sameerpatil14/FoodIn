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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodin.FoodInfo;
import com.example.foodin.HomeActivity;
import com.example.foodin.R;
import com.example.foodin.SigninActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SuggestedAdapter extends FirebaseRecyclerAdapter<SuggestedHelperClass, SuggestedAdapter.SuggestedViewHolder> {
    Context context;

    public SuggestedAdapter(@NonNull FirebaseRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull SuggestedViewHolder holder, int position, @NonNull SuggestedHelperClass model) {
        holder.suggestedTitle.setText(model.getSuggestedTitle());
        holder.suggestedDesc.setText(model.getSuggestedDesc());
        holder.suggestedPrice.setText("₹" + model.getSuggestedPrice() + " for one");

        Glide.with(holder.suggestedImage.getContext()).load(model.getSuggestedImage()).into(holder.suggestedImage);

        holder.cvSuggested.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //sharedAnimation
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(holder.suggestedImage, "tImage");
                pairs[1] = new Pair<View, String>(holder.suggestedTitle, "tName");
                pairs[2] = new Pair<View, String>(holder.suggestedDesc, "tRestaurant");
                Intent i = new Intent(context, FoodInfo.class);
                i.putExtra("image",model.getSuggestedImage());
                i.putExtra("name",model.getSuggestedTitle());
                i.putExtra("restaurant",model.getSuggestedDesc());
                i.putExtra("price",model.getSuggestedPrice());
                ActivityOptions Aoptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                context.startActivity(i, Aoptions.toBundle());

            }
        });
    }

    @NonNull
    @Override
    public SuggestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_card_design, parent, false);
        SuggestedAdapter.SuggestedViewHolder suggestedViewHolder = new SuggestedAdapter.SuggestedViewHolder(view);
        return suggestedViewHolder;
    }

    public class SuggestedViewHolder extends RecyclerView.ViewHolder {

        ImageView suggestedImage;
        TextView suggestedTitle, suggestedDesc, suggestedPrice;
        CardView cvSuggested;

        public SuggestedViewHolder(@NonNull View itemView) {
            super(itemView);

            suggestedImage = itemView.findViewById(R.id.suggestedImage);
            suggestedTitle = itemView.findViewById(R.id.suggestedTitle);
            suggestedDesc = itemView.findViewById(R.id.suggestedDesc);
            suggestedPrice = itemView.findViewById(R.id.suggestedPrice);
            cvSuggested = itemView.findViewById(R.id.cvSuggested);
        }
    }
}
