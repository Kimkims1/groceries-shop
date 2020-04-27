package com.carldroid.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carldroid.groceryapp.Adapters.ProductSeller;
import com.carldroid.groceryapp.Models.ModelProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainSellerActivity extends AppCompatActivity {

    private TextView nameTv, filteredProductsTv, emailTv, shopNameTv, productTab, OrderTab;
    private ImageButton logout, editProfileBtn, addProductBtn, filterProductBtn;
    private ImageView profileIv;
    private EditText searchProductEt;
    private RecyclerView productsRv;

    private FirebaseAuth firebaseAuth;

    private RelativeLayout productsRl, ordersRl;

    private ArrayList<ModelProduct> productList;
    private ProductSeller adapterProductSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        logout = findViewById(R.id.logoutBtn);
        productsRv = findViewById(R.id.productsRv);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        shopNameTv = findViewById(R.id.shopNameTv);
        addProductBtn = findViewById(R.id.addProductBtn);
        profileIv = findViewById(R.id.profileIv);
        productTab = findViewById(R.id.productsTab);
        OrderTab = findViewById(R.id.ordersTab);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.odersRl);
        searchProductEt = findViewById(R.id.searchProductEt);
        filterProductBtn = findViewById(R.id.filterProductBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllProducts();

        showProductsUi();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterProductSeller.getFilter().filter(s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        productTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProductsUi();

            }
        });

        OrderTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showOdersUi();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(MainSellerActivity.this, ProfileEditSellerActivity.class));
                finish();

            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open addProductActivity
                startActivity(new Intent(MainSellerActivity.this, AddProductActivity.class));
                finish();

            }
        });

        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSellerActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constants.categories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.categories1[which];
                                filteredProductsTv.setText(selected);
                                if (selected.equals("All")) {
                                    //load all
                                    loadAllProducts();
                                } else {
                                    loadFilteredProducts(selected);
                                }

                            }
                        })
                        .show();
            }
        });
    }

    private void loadFilteredProducts(final String selected) {
        productList = new ArrayList<>();

        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting data
                        productList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String productCategory = "" + ds.child("productCategory").getValue();

                            if (selected.equals(productCategory)) {

                                ModelProduct product = ds.getValue(ModelProduct.class);
                                productList.add(product);
                            }

                        }

                        //set up adapter
                        adapterProductSeller = new ProductSeller(MainSellerActivity.this, productList);
                        productsRv.setAdapter(adapterProductSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void loadAllProducts() {

        productList = new ArrayList<>();

        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting data
                        productList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelProduct product = ds.getValue(ModelProduct.class);
                            productList.add(product);
                        }

                        //set up adapter
                        adapterProductSeller = new ProductSeller(MainSellerActivity.this, productList);
                        productsRv.setAdapter(adapterProductSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void showOdersUi() {
        productsRl.setVisibility(View.GONE);
        ordersRl.setVisibility(View.VISIBLE);

        productTab.setTextColor(getResources().getColor(R.color.colorWhite));
        productTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        OrderTab.setTextColor(getResources().getColor(R.color.colorblack));
        OrderTab.setBackgroundResource(R.drawable.shape_rect04);

    }

    private void showProductsUi() {

        productsRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.GONE);

        productTab.setTextColor(getResources().getColor(R.color.colorblack));
        productTab.setBackgroundResource(R.drawable.shape_rect04);

        OrderTab.setTextColor(getResources().getColor(R.color.colorWhite));
        OrderTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));


    }

    private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));

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

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String email = "" + ds.child("email").getValue();
                            String shopName = "" + ds.child("shopName").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            nameTv.setText(name);
                            shopNameTv.setText(shopName);
                            emailTv.setText(email);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_gray).into(profileIv);
                            } catch (Exception e) {

                                profileIv.setImageResource(R.drawable.ic_store_gray);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
