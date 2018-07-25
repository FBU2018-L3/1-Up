package com.l3.one_up.adapters;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private String[] categories;
    private TypedArray categoryIcons;

    public OnCategoryItemListener categoryClickListener;

    public CategoryAdapter(String[] categories, TypedArray categoryIcons, Fragment parentFragment) {
        this.categories = categories;
        this.categoryIcons = categoryIcons;
        this.categoryClickListener = (OnCategoryItemListener) parentFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View activityViewer = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int fallbackImageId = android.R.drawable.ic_menu_crop;
        holder.ivCategoryIcon.setImageResource(categoryIcons.getResourceId(position, fallbackImageId));
        holder.tvCategoryName.setText(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvCategoryName;
        public ImageView ivCategoryIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                categoryClickListener.onItemClicked(categories[position]);
            }
        }
    }

    public interface OnCategoryItemListener {
        void onItemClicked(String itemName);
    }
}
