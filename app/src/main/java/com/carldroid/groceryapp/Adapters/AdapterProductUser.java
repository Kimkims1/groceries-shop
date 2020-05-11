package com.carldroid.groceryapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carldroid.groceryapp.Models.ModelProduct;
import com.carldroid.groceryapp.R;
import com.carldroid.groceryapp.Search.FilterProductsUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        ModelProduct modelProduct = productList.get(position);
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

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* show product details*/
            }
        });
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
