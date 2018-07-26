package com.l3.one_up.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l3.one_up.R;
import com.l3.one_up.adapters.CategoryAdapter;


public class CategorySelectionFragment extends Fragment
        implements CategoryAdapter.OnCategoryItemListener {

    private RecyclerView rvCategories;
    private CategoryAdapter categoriesAdapter;
    private TypedArray categoryIcons;
    private String[] categories;

    private OnCategorySelectedListener mListener;

    public CategorySelectionFragment() {
        // Required empty public constructor
    }

    public static CategorySelectionFragment newInstance() {
        CategorySelectionFragment fragment = new CategorySelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categories = getResources().getStringArray(R.array.categories);
        categoryIcons = getResources().obtainTypedArray(R.array.your_array_name);

        categoriesAdapter = new CategoryAdapter(categories, categoryIcons, this);

        rvCategories = (RecyclerView) getActivity().findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parent = getParentFragment();
        if (parent instanceof OnCategorySelectedListener) {
            mListener = (OnCategorySelectedListener) parent;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategorySelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClicked(String itemName) {
        if (mListener != null) {
            mListener.onCategoryClick(itemName);
        }
    }

    public interface OnCategorySelectedListener {
        void onCategoryClick(String categoryName);
    }

}
