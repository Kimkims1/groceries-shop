package com.carldroid.groceryapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carldroid.groceryapp.Models.ModelProduct;
import com.carldroid.groceryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductSeller extends RecyclerView.Adapter<ProductSeller.HolderProductSeller> {

    private Context context;
    public ArrayList<ModelProduct> modelProducts;

    public ProductSeller(Context context, ArrayList<ModelProduct> modelProducts) {
        this.context = context;
        this.modelProducts = modelProducts;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_products_seller, parent, false);

        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {

        ModelProduct product = modelProducts.get(position);
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


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder {

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
