package com.carldroid.groceryapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carldroid.groceryapp.Models.ModelProduct;
import com.carldroid.groceryapp.R;
import com.carldroid.groceryapp.Search.FilterProductsUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.HolderProductUser> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProductsUser filterProductsUser;

    public AdapterProductUser(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = filterList;
    }

    @NonNull
    @Override
    public HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_products_user, parent, false);

        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductUser holder, int position) {

        /* get data*/
        final ModelProduct modelProduct = productList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String originalPrice = modelProduct.getOriginalPrice();
        String productDescription = modelProduct.getProductDescription();
        String productTitle = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimestamp();
        String productIcon = modelProduct.getProductIcon();

        /* Set data*/
        holder.titleTv.setText(productTitle);
        holder.discountNoteTv.setText(discountNote);
        holder.descriptionTv.setText(productDescription);
        holder.originalPriceTv.setText("$" + originalPrice);
        holder.discountedPriceTv.setText("$" + discountPrice);
        holder.discountNoteTv.setText(discountNote);

        if (discountAvailable.equals("true")) {

            holder.discountedPriceTv.setVisibility(View.VISIBLE);
            holder.discountNoteTv.setVisibility(View.INVISIBLE);
            holder.originalPriceTv.setPaintFlags(holder.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {

            holder.discountedPriceTv.setVisibility(View.GONE);
            holder.discountNoteTv.setVisibility(View.GONE);
            holder.originalPriceTv.setPaintFlags(0);

        }

        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_add_shopping_primary).into(holder.productIconIv);

        } catch (Exception e) {
            holder.productIconIv.setImageResource(R.drawable.ic_add_shopping_primary);
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Add product to cart*/

                showQuantityDialog(modelProduct);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* show product details*/
            }
        });
    }

    private double cost = 0;
    private double finalCost = 0;
    private int quantity = 0;

    private void showQuantityDialog(ModelProduct modelProduct) {

        //inflate layout dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity, null);

        /* init layout views*/
        ImageView productIv = view.findViewById(R.id.productIv);
        final TextView titleTv = view.findViewById(R.id.titleTv);
        TextView pQuantityTv = view.findViewById(R.id.quantityTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView discountedNoteTvTv = view.findViewById(R.id.discountedNoteTvTv);
        final TextView originalPriceTv = view.findViewById(R.id.originalPriceTv);
        TextView discountedPriceTv = view.findViewById(R.id.discountedPriceTv);
        final TextView finalPriceTv = view.findViewById(R.id.finalPriceTv);
        ImageButton decrement_button = view.findViewById(R.id.decrement_button);
        ImageButton increment_button = view.findViewById(R.id.increment_button);
        final TextView quantityCountTv = view.findViewById(R.id.quantityCountTv);
        Button continueBtn = view.findViewById(R.id.continueBtn);

        //get data from model
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String originalPrice = modelProduct.getOriginalPrice();
        String description = modelProduct.getProductDescription();
        String title = modelProduct.getProductTitle();
        String pQuantity = modelProduct.getProductQuantity();
        final String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimestamp();
        String image = modelProduct.getProductIcon();

        String price;
        if (modelProduct.getDiscountAvailable().equals("true")) {
            //product has discount
            price = modelProduct.getDiscountPrice();
            discountedPriceTv.setVisibility(View.VISIBLE);
            originalPriceTv.setPaintFlags(originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            //product has no discount
            discountedNoteTvTv.setVisibility(View.GONE);
            discountedPriceTv.setVisibility(View.GONE);
            price = modelProduct.getOriginalPrice();
        }

        cost = Double.parseDouble(price.replace("$", ""));
        finalCost = Double.parseDouble(price.replace("$", ""));
        quantity = 1;

        //alertDialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_shopping_cart_gray).into(productIv);

        } catch (Exception e) {

            productIv.setImageResource(R.drawable.ic_shopping_cart_gray);
        }

        titleTv.setText("" + title);
        pQuantityTv.setText("" + pQuantity);
        descriptionTv.setText("" + description);
        discountedNoteTvTv.setText("" + discountNote);
        quantityCountTv.setText("" + quantity);
        originalPriceTv.setText("" + modelProduct.getOriginalPrice());
        discountedPriceTv.setText("" + modelProduct.getDiscountPrice());
        finalPriceTv.setText("" + finalCost);


        final AlertDialog dialog1 = dialog.create();
        dialog1.show();

        //increase quantity of pdt
        increment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCost = finalCost + cost;
                quantity++;

                finalPriceTv.setText("$" + finalCost);
                quantityCountTv.setText("" + quantity);
            }
        });

        //do decrement of quantity
        decrement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    finalCost = finalCost - cost;
                    quantity--;

                    finalPriceTv.setText("$" + finalCost);
                    quantityCountTv.setText("" + quantity);
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleTv.getText().toString().trim();
                String priceEach = originalPriceTv.getText().toString().trim().replace("$", "");
                String price = finalPriceTv.getText().toString().trim().replace("$", "");
                String quantity = quantityCountTv.getText().toString().trim();

                //add to db(SQLite)
                addToCart(productId, title, priceEach, price, quantity);
                dialog1.dismiss();
            }
        });

    }

    private int itemId = 1;

    private void addToCart(String productId, String title, String priceEach, String price, String quantity) {
        itemId++;

        EasyDB easyDB = EasyDB.init(context, "ITEMS_DB") // TEST is the name of the DATABASE
                .setTableName("ITEMS_TABLE")  // You can ignore this line if you want
                .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                .doneTableColumn();

        Boolean b = easyDB
                .addData("Item_Id", itemId)
                .addData("Item_PID", productId)
                .addData("Item_Name", title)
                .addData("Item_Price_Each", priceEach)
                .addData("Item_Quantity", quantity)
                .doneDataAdding();

        Toast.makeText(context, "Added to cart...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filterProductsUser == null) {
            filterProductsUser = new FilterProductsUser(this, filterList);
        }
        return filterProductsUser;
    }

    class HolderProductUser extends RecyclerView.ViewHolder {


        //ui views
        private ImageView productIconIv;
        private TextView discountNoteTv, titleTv, descriptionTv,
                addToCartTv, discountedPriceTv, originalPriceTv;

        public HolderProductUser(@NonNull View itemView) {
            super(itemView);


            //product ui views
            productIconIv = itemView.findViewById(R.id.productIconIv);
            discountNoteTv = itemView.findViewById(R.id.discountedNoteTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            addToCartTv = itemView.findViewById(R.id.addToCartTv);
            discountedPriceTv = itemView.findViewById(R.id.discountedPriceTv);
            originalPriceTv = itemView.findViewById(R.id.originalPriceTv);


        }
    }
}
