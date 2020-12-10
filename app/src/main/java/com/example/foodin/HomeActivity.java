package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodin.HomeAdapter.FoodListAdapter;
import com.example.foodin.HomeAdapter.FoodListHelperClass;
import com.example.foodin.HomeAdapter.SuggestedAdapter;
import com.example.foodin.HomeAdapter.SuggestedHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView cart;
    SearchView searchView;

    RecyclerView rvSuggested;
    RecyclerView rvFoodlist;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String UserId;
    String email, name;

    //Adapters
    FoodListAdapter foodListAdapter;
    SuggestedAdapter suggestedAdapter;

    //Navigation Layout header
    TextView profilName, profileEmail;
    String q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UserId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(UserId);
        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation View
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);

        //Recycler View and start loading
        suggestedRecycler();
        suggestedAdapter.startListening();
        foodListRecycler();
        foodListAdapter.startListening();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                profilName.setText(name);
                profileEmail.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //SearchView
        SearchRecyclerView();

        //Cart
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });
    }

    private void init() {
        rvSuggested = findViewById(R.id.rvSuggested);
        rvFoodlist = findViewById(R.id.rvFoodList);
        cart = findViewById(R.id.cart);
        searchView = findViewById(R.id.searchView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        profilName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        profileEmail = navigationView.getHeaderView(0).findViewById(R.id.profileEmail);
    }


    //SUGGESTED FIREBASE RECYCLER
    private void suggestedRecycler() {
        rvSuggested.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseRecyclerOptions<SuggestedHelperClass> Soptions =
                new FirebaseRecyclerOptions.Builder<SuggestedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("food").child("suggestedFood"), SuggestedHelperClass.class)
                        .build();

        suggestedAdapter = new SuggestedAdapter(Soptions, HomeActivity.this);
        rvSuggested.setAdapter(suggestedAdapter);
    }

    //FOODLIST FIREBASE RECYCLER
    private void foodListRecycler() {
        rvFoodlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        FirebaseRecyclerOptions<FoodListHelperClass> Foptions =
                new FirebaseRecyclerOptions.Builder<FoodListHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("food").child("foodItems").orderByChild("foodName"), FoodListHelperClass.class)
                        .build();

        foodListAdapter = new FoodListAdapter(Foptions, HomeActivity.this);
        rvFoodlist.setAdapter(foodListAdapter);

    }

    private void SearchRecyclerView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchResult(s);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchResult(s);
                return false;
            }
        });
    }


    private void SearchResult(String q) {

        FirebaseRecyclerOptions<FoodListHelperClass> Foptions =
                new FirebaseRecyclerOptions.Builder<FoodListHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("food").child("foodItems").orderByChild("foodName").startAt(q).endAt(q + "\uf8ff"), FoodListHelperClass.class)
                        .build();

        foodListAdapter = new FoodListAdapter(Foptions, HomeActivity.this);
        foodListAdapter.startListening();
        rvFoodlist.setAdapter(foodListAdapter);
    }

    //NAVIGATION ITEM SELECT
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                break;
            case R.id.navigation_profile:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                break;
            case R.id.navigation_orders:
                startActivity(new Intent(getApplicationContext(), Orders.class));
                break;
            case R.id.navigation_cart:
                startActivity(new Intent(getApplicationContext(), Cart.class));
                break;
            case R.id.navigation_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                finishAffinity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //to clear the focus if onBackPressed to current activity
    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
        navigationView.setCheckedItem(R.id.navigation_home);
    }
    //start FirebaseRecyclerView loading
/*    @Override
    protected void onStart() {
        super.onStart();
        suggestedAdapter.startListening();
        foodListAdapter.startListening();
    }

    //stop FirebaseRecyclerView loading
    @Override
    protected void onStop() {
        super.onStop();
        suggestedAdapter.stopListening();
        foodListAdapter.stopListening();
    }*/

    private static long back_pressed_time;

    //Overriding onBackPressed to Open and Close Drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            //Exit if back button is clicked twice
            if (back_pressed_time + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed_time = System.currentTimeMillis();
        }
    }
}