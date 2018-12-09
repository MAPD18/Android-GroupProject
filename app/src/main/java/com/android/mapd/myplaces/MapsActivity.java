package com.android.mapd.myplaces;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceViewModel;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap map;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.restaurantFAB)
    FloatingActionButton restaurantFAB;

    @BindView(R.id.movieTheaterFAB)
    FloatingActionButton movieTheaterFAB;

    @BindView(R.id.barFAB)
    FloatingActionButton barFAB;

    @BindView(R.id.floatingActionMenu)
    FloatingActionMenu floatingActionMenu;

    private FavoritePlaceViewModel viewModel;
    int PLACE_PICKER_REQUEST = 1;
    private FavoritePlace.Category selectedCategory;
    private HashMap<FavoritePlace.Category, BitmapDescriptor> markerDictionary = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(FavoritePlaceViewModel.class);
        viewModel.getAllFavoritePlaces().observe(this, new Observer<List<FavoritePlace>>() {
            @Override
            public void onChanged(@Nullable List<FavoritePlace> favoritePlaces) {
                if (favoritePlaces != null)
                    refreshMarkers(favoritePlaces);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFABButtons();
        initMarkerCustomIcons();
    }

    private void initMarkerCustomIcons() {
        markerDictionary.put(FavoritePlace.Category.RESTAURANT, BitmapDescriptorFactory.fromResource(R.drawable.green_pin));
        markerDictionary.put(FavoritePlace.Category.MOVIE_THEATER, BitmapDescriptorFactory.fromResource(R.drawable.blue_pin));
        markerDictionary.put(FavoritePlace.Category.BAR, BitmapDescriptorFactory.fromResource(R.drawable.purple_pin));
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

    private void initFABButtons() {
        restaurantFAB.setOnClickListener(this);
        movieTheaterFAB.setOnClickListener(this);
        barFAB.setOnClickListener(this);
    }

    private void buildPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(LatLngBounds.builder()
                .include(new LatLng(43.632318, -79.454857))
                .include(new LatLng(43.750260, -79.351694))
                .build());
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                FavoritePlace favoritePlace = new FavoritePlace(PlacePicker.getPlace(this, data), selectedCategory);
                viewModel.insert(favoritePlace);

                String toastMsg = String.format("New Place: %s added to Favorites", favoritePlace.getName());
                Snackbar.make(coordinatorLayout, toastMsg, Snackbar.LENGTH_LONG).show();
                floatingActionMenu.close(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43.670233, -79.372494);
        map.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restaurantFAB:
                selectedCategory = FavoritePlace.Category.RESTAURANT;
                break;

            case R.id.movieTheaterFAB:
                selectedCategory = FavoritePlace.Category.MOVIE_THEATER;
                break;

            case R.id.barFAB:
                selectedCategory = FavoritePlace.Category.BAR;
                break;
        }
        buildPlacePicker();
    }
}
