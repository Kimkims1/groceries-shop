package com.carldroid.groceryapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carldroid.groceryapp.Adapters.AdapterProductUser;
import com.carldroid.groceryapp.Constants;
import com.carldroid.groceryapp.Models.ModelProduct;
import com.carldroid.groceryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {

    private ImageView shopIv;
    private TextView shopNameTv, phoneTv, emailTv, openCloseTv, deliveryFeeTv, addressTv, filteredPdtsTv;
    private ImageButton callBtn, mapBtn, cartBtn, backBtn, filteredPdtBtn;
    private RecyclerView productsRv;
    private EditText searchProductEt;

    String shopUid;

    private FirebaseAuth firebaseAuth;
    private String myLatitude, myLongitude;
    private String shopLatitude, shopLongitude, shopName, shopAddress, shopEmail, shopPhone;

    private AdapterProductUser adapterProductUser;
    private ArrayList<ModelProduct> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        shopIv = findViewById(R.id.shopIv);
        searchProductEt = findViewById(R.id.searchProductEt);
        shopNameTv = findViewById(R.id.shopNameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        openCloseTv = findViewById(R.id.openCloseTv);
        deliveryFeeTv = findViewById(R.id.deliveryTv);
        addressTv = findViewById(R.id.addressTv);
        filteredPdtsTv = findViewById(R.id.filteredProductsTv);
        callBtn = findViewById(R.id.callBtn);
        mapBtn = findViewById(R.id.mapBtn);
        cartBtn = findViewById(R.id.cartBtn);
        backBtn = findViewById(R.id.backBtn);
        filteredPdtBtn = findViewById(R.id.filteredProductsBtn);
        productsRv = findViewById(R.id.productsRv);

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();
        loadShopDetails();
        loadShopProducts();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adapterProductUser.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* */
                dialPhone();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* open map*/
                openMap();
            }
        });

        filteredPdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsActivity.this);
                builder.setTitle("Choose Category: ")
                        .setItems(Constants.categories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String selected = Constants.categories1[which];
                                filteredPdtsTv.setText(selected);

                                if (selected.equals("All")) {
                                    //load all
                                    loadShopProducts();
                                } else {
                                    adapterProductUser.getFilter().filter(selected);
                                }
                            }
                        })
                        .show();
            }
        });

    }

    private void openMap() {
        /*
         * saddr = means Source address
         * daddr = means Source address
         * */
        String address = "https://maps.google.com/maps?saddr=" + myLatitude + "," + myLongitude + "&daddr=" + shopLatitude + "," + shopLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }

    private void dialPhone() {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(shopPhone))));
        Toast.makeText(this, "" + shopPhone, Toast.LENGTH_SHORT).show();
    }

    private void loadShopProducts() {

        /* init list*/
        productList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //clear list before adding items
                        productList.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }

                        /* Set up adapter*/
                        adapterProductUser = new AdapterProductUser(ShopDetailsActivity.this, productList);
                        /* set adapter*/
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void loadShopDetails() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*Get shop data*/
                String name = "" + dataSnapshot.child("name").getValue();
                shopEmail = "" + dataSnapshot.child("email").getValue();
                shopPhone = "" + dataSnapshot.child("phone").getValue();
                shopName = "" + dataSnapshot.child("shopName").getValue();
                shopAddress = "" + dataSnapshot.child("address").getValue();
                shopLatitude = "" + dataSnapshot.child("latitude").getValue();
                shopLongitude = "" + dataSnapshot.child("longitude").getValue();
                String deliveryFee = "" + dataSnapshot.child("deliveryFee").getValue();
                String profileImage = "" + dataSnapshot.child("profileImage").getValue();
                String shopOpen = "" + dataSnapshot.child("openShop").getValue();

                /*set data to TextViews*/
                shopNameTv.setText(shopName);
                emailTv.setText(shopEmail);
                deliveryFeeTv.setText("Delivery Fee: $" + deliveryFee);
                addressTv.setText(shopAddress);
                phoneTv.setText(shopPhone);

                if (shopOpen.equals("true")) {
                    openCloseTv.setText("Open");
                } else {
                    openCloseTv.setText("Closed");
                }

                try {
                    Picasso.get().load(profileImage).into(shopIv);
                } catch (Exception ignored) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMyInfo() {

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

                            myLatitude = "" + ds.child("latitude").getValue();
                            myLongitude = "" + ds.child("longitude").getValue();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
