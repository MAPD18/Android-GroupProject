package com.android.mapd.myplaces.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mapd.myplaces.R;
import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceViewModel;
import com.android.mapd.myplaces.view.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    @BindView(R.id.mapView)
    public MapView mapView;

    private MainActivity mainActivity;
    private FavoritePlaceViewModel viewModel;
    private HashMap<FavoritePlace.Category, BitmapDescriptor> markerDictionary = new HashMap<>();

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void refreshMarkers(List<FavoritePlace> favoritePlaces) {
        map.clear();
        for (FavoritePlace favoritePlace : favoritePlaces) {
            map.addMarker(new MarkerOptions()
                    .position(favoritePlace.getCoordinatesAsLatLng())
                    .icon(markerDictionary.get(favoritePlace.getCategory()))
                    .title(favoritePlace.getName()));
        }
    }

    private void initMarkerCustomIcons() {
        markerDictionary.put(FavoritePlace.Category.RESTAURANT, BitmapDescriptorFactory.fromResource(R.drawable.green_pin));
        markerDictionary.put(FavoritePlace.Category.MOVIE_THEATER, BitmapDescriptorFactory.fromResource(R.drawable.blue_pin));
        markerDictionary.put(FavoritePlace.Category.BAR, BitmapDescriptorFactory.fromResource(R.drawable.purple_pin));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        MapsInitializer.initialize(mainActivity);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        initMarkerCustomIcons();
        viewModel = ViewModelProviders.of(mainActivity).get(FavoritePlaceViewModel.class);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43.670233, -79.372494);
        map.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f));

        viewModel.getAllFavoritePlaces().observe(this, new Observer<List<FavoritePlace>>() {
            @Override
            public void onChanged(@Nullable List<FavoritePlace> favoritePlaces) {
                if (favoritePlaces != null)
                    refreshMarkers(favoritePlaces);
            }
        });
    }

}
