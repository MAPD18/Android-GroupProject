package com.android.mapd.myplaces.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mapd.myplaces.R;
import com.android.mapd.myplaces.adapter.MyFavoritePlaceRecyclerViewAdapter;
import com.android.mapd.myplaces.dummy.DummyContent;
import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private FavoritePlaceViewModel viewModel;
    private MyFavoritePlaceRecyclerViewAdapter adapter;

    public ListFragment() { }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoriteplace_list, container, false);


        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new MyFavoritePlaceRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(adapter);
        }

        viewModel = ViewModelProviders.of(getActivity()).get(FavoritePlaceViewModel.class);
        viewModel.getAllFavoritePlaces().observe(this, new Observer<List<FavoritePlace>>() {
            @Override
            public void onChanged(@Nullable List<FavoritePlace> favoritePlaces) {
                if (favoritePlaces != null)
                    adapter.setDataList(favoritePlaces);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onFavoritePlaceDeleted(FavoritePlace item);
        void onUriClicked(String uri);
    }
}
