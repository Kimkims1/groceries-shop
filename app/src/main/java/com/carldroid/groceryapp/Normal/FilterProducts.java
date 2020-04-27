package com.carldroid.groceryapp.Normal;

import android.widget.Filter;

import com.carldroid.groceryapp.Adapters.ProductSeller;
import com.carldroid.groceryapp.Models.ModelProduct;

import java.util.ArrayList;

public class FilterProducts extends Filter {

    private ProductSeller adapter;
    private ArrayList<ModelProduct> filterList;

    public FilterProducts(ProductSeller adapter, ArrayList<ModelProduct> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults filterResults = new FilterResults();
        //validate data for search query
        if (constraint != null && constraint.length() > 0) {
            //search filled not empty,searching sth, perform search

            //change to upper case, to make case insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered results
            ArrayList<ModelProduct> filteredModels = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //check, search by title or category
                if (filterList.get(i).getProductTitle().toUpperCase().contains(constraint) ||
                        filterList.get(i).getProductCategory().toUpperCase().contains(constraint)) {
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));

                }
            }
            filterResults.count = filteredModels.size();
            filterResults.values = filteredModels;
        } else {
            //search filled empty,not searching,return original content/text

            filterResults.count = filterList.size();
            filterResults.values = filterList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.modelProducts = (ArrayList<ModelProduct>) results.values;

        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
