package com.android.mapd.myplaces.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.view.fragment.ListFragment.OnListFragmentInteractionListener;
import com.android.mapd.myplaces.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFavoritePlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoritePlaceRecyclerViewAdapter.ViewHolder> {

    private List<FavoritePlace> dataList;
    private final OnListFragmentInteractionListener listener;

    public MyFavoritePlaceRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        dataList = new ArrayList<>();
        this.listener = listener;
    }

    public void setDataList(List<FavoritePlace> newList) {
        dataList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favoriteplace, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FavoritePlace favoritePlace = dataList.get(position);
        holder.placeName.setText(favoritePlace.getName());
        holder.address.setText(favoritePlace.getAddress());
        holder.website.setText(favoritePlace.getUriWebsite());
        holder.phoneNumber.setText(favoritePlace.getPhoneNumber());
        holder.ratingBar.setRating(favoritePlace.getRating());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onFavoritePlaceDeleted(favoritePlace);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeName)
        TextView placeName;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.website)
        TextView website;
        @BindView(R.id.phoneNumber)
        TextView phoneNumber;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.deleteButton)
        TextView deleteButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
