package com.carldroid.groceryapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carldroid.groceryapp.Adapters.AdapterShop;
import com.carldroid.groceryapp.Models.ModelShop;
import com.carldroid.groceryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {

    private TextView nameTv, emailTv, phoneTv, tabsShopsTv, tabsOrdersTv;
    private ImageButton logoutBtn, editProfileBtn;
    private RelativeLayout shopRl, ordersRl, shopsRv;
    private ImageView profileIv;
    private RecyclerView shopRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelShop> shopList;
    private AdapterShop adapterShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        nameTv = findViewById(R.id.nameTv);
        shopRv = findViewById(R.id.shopRv);
        shopRl = findViewById(R.id.shopRl);
        ordersRl = findViewById(R.id.ordersRl);
        tabsShopsTv = findViewById(R.id.tabsShopsTv);
        tabsOrdersTv = findViewById(R.id.tabsOrdersTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        profileIv = findViewById(R.id.profileIv);
        logoutBtn = findViewById(R.id.logoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        showShopsUi();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile activity
                startActivity(new Intent(MainUserActivity.this, ProfileEditUserActivity.class));
                finish();
            }
        });

        tabsShopsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopsUi();
            }
        });

        tabsOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersUi();
            }
        });
    }

    private void showShopsUi() {
        //show shops ui, hide orders ui
        shopRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.INVISIBLE);

        tabsShopsTv.setTextColor(getResources().getColor(R.color.colorblack));
        tabsShopsTv.setBackgroundResource(R.drawable.shape_rect04);

        tabsOrdersTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabsOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    private void showOrdersUi() {
        //show orders ui, hide shops
        shopRl.setVisibility(View.GONE);
        ordersRl.setVisibility(View.VISIBLE);

        tabsShopsTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabsShopsTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabsOrdersTv.setTextColor(getResources().getColor(R.color.colorblack));
        tabsOrdersTv.setBackgroundResource(R.drawable.shape_rect04);

    }

    private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));

        } else {
            loadMyInfor();
        }

    }

    private void loadMyInfor() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //get user data
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String city = "" + ds.child("city").getValue();


                            //set user data
                            nameTv.setText(name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);
                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_gray).into(profileIv);

                            } catch (Exception e) {
                                profileIv.setImageResource(R.drawable.ic_person_gray);
                            }

                            loadShops(city);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadShops(final String city) {

        //init list
        shopList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //clear list before adding
                        shopList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = "" + ds.child("city").getValue();

                            //show only user city shops
                            if (shopCity.equals(city)) {
                                shopList.add(modelShop);
                            }
                            //if u want to display all shops, skip the if statement and add this
                            //shoplist.add(modelshop);

                        }
                        //set up adapter
                        adapterShop = new AdapterShop(MainUserActivity.this, shopList);
                        //set adapter to recyclerView
                        shopRv.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
