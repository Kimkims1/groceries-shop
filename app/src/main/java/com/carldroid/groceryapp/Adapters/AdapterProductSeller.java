package com.carldroid.groceryapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carldroid.groceryapp.Models.ModelProduct;
import com.carldroid.groceryapp.Activities.EditProductActivity;
import com.carldroid.groceryapp.Search.FilterProducts;
import com.carldroid.groceryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.HolderProductSeller> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList, filterlist;
    private FilterProducts filter;


    public AdapterProductSeller(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterlist = productList;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_products_seller, parent, false);

        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {

        final ModelProduct product = productList.get(position);
        String id = product.getProductId();
        String uid = product.getUid();
        String discNote = product.getDiscountNote();
        String discAvailable = product.getDiscountAvailable();
        String discPrice = product.getDiscountPrice();
        String productCategory = product.getProductCategory();
        String productDescr = product.getProductDescription();
        String icon = product.getProductIcon();
        String quantity = product.getProductQuantity();
        String title = product.getProductTitle();
        String timeStamp = product.getTimestamp();
        String originalPrice = product.getOriginalPrice();

        //set Title
        holder.titleTv.setText(title);
        holder.quantityTv.setText(quantity);
        holder.discountedNoteTv.setText(discNote);
        holder.discountedPriceTv.setText("$" + discPrice);
        holder.originalPriceTv.setText("$" + originalPrice);

        if (discAvailable.equals("true")) {

            holder.discountedPriceTv.setVisibility(View.VISIBLE);
            holder.discountedNoteTv.setVisibility(View.VISIBLE);
            holder.originalPriceTv.setPaintFlags(holder.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {

            holder.discountedPriceTv.setVisibility(View.GONE);
            holder.discountedNoteTv.setVisibility(View.GONE);

        }

        try {
            Picasso.get().load(icon).placeholder(R.drawable.ic_add_shopping_primary).into(holder.productIv);

        } catch (Exception e) {
            holder.productIv.setImageResource(R.drawable.ic_add_shopping_primary);
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details on bottomsheet

                detailsBottomSheet(product);

            }
        });


    }

    private void detailsBottomSheet(ModelProduct product) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_seller_details, null);
        //set View to bottomSheet
        bottomSheetDialog.setContentView(view);


        //initiate views of bottomsheet
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView productIconIv = view.findViewById(R.id.productIconIv);
        TextView discountNoteTv = view.findViewById(R.id.discountedNoteTv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView quantityTv = view.findViewById(R.id.quantityTv);
        TextView discountedPriceTv = view.findViewById(R.id.discountedPriceTv);
        TextView originalPriceTv = view.findViewById(R.id.originalPriceTv);

        //get data
        final String id = product.getProductId();
        String uid = product.getUid();
        String discountNote = product.getDiscountNote();
        String discAvailable = product.getDiscountAvailable();
        String discountPrice = product.getDiscountPrice();
        String Category = product.getProductCategory();
        String description = product.getProductDescription();
        String icon = product.getProductIcon();
        String quantity = product.getProductQuantity();
        final String title = product.getProductTitle();
        String timeStamp = product.getTimestamp();
        String originalPrice = product.getOriginalPrice();

        //set data
        titleTv.setText(title);
        descriptionTv.setText(description);
        categoryTv.setText(Category);
        quantityTv.setText(quantity);
        discountNoteTv.setText(discountNote);
        discountedPriceTv.setText("$" + discountPrice);
        originalPriceTv.setText("$" + originalPrice);


        if (discAvailable.equals("true")) {

            discountedPriceTv.setVisibility(View.VISIBLE);
            discountNoteTv.setVisibility(View.VISIBLE);
            originalPriceTv.setPaintFlags(originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {

            discountedPriceTv.setVisibility(View.GONE);
            discountNoteTv.setVisibility(View.GONE);

        }

        try {
            Picasso.get().load(icon).placeholder(R.drawable.ic_add_shopping_primary).into(productIconIv);

        } catch (Exception e) {
            productIconIv.setImageResource(R.drawable.ic_add_shopping_primary);
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        bottomSheetDialog.show();

        //edit edit button click

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                //open edit product activity,pass id of the product
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("productId", id);
                context.startActivity(intent);

            }
        });

        //edit delete btn click
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

                //show delete confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete product" + title + "?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete product by using its id
                                deleteProduct(id);

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .show();


            }
        });

        //edit back btn click

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dismiss bottomsheet
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void deleteProduct(String id) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //product deleted
                        Toast.makeText(context, "Product deleted...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterProducts(this, filterlist);
        }
        return filter;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder {

        //holds view of recyclerview
        private ImageView productIv;
        private TextView discountedNoteTv, titleTv, quantityTv, discountedPriceTv, originalPriceTv;


        public HolderProductSeller(@NonNull View itemView) {
            super(itemView);

            productIv = itemView.findViewById(R.id.productIconIv);
            discountedNoteTv = itemView.findViewById(R.id.discountedNote);
            titleTv = itemView.findViewById(R.id.titleTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            discountedPriceTv = itemView.findViewById(R.id.discountedPriceTv);
            originalPriceTv = itemView.findViewById(R.id.originalPriceTv);


        }
    }
}
