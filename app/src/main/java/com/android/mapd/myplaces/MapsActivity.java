package com.android.mapd.myplaces;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    @BindView(R.id.restaurantFAB)
    FloatingActionButton restaurantFAB;

    @BindView(R.id.movieTheaterFAB)
    FloatingActionButton movieTheaterFAB;

    @BindView(R.id.barFAB)
    FloatingActionButton barFAB;

    @BindView(R.id.floatingActionMenu)
    FloatingActionMenu floatingActionMenu;

    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFABButtons();

    }

    private void initFABButtons() {
        restaurantFAB.setOnClickListener(this);
        movieTheaterFAB.setOnClickListener(this);
        barFAB.setOnClickListener(this);
    }

    private void buildPLacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(LatLngBounds.builder()
                .include(new LatLng(43.670233, -79.372494))
                .include(new LatLng(43.970233, -79.072494))
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
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                floatingActionMenu.close(true);
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43.670233, -79.372494);
        mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f));
    }

    @Override
    public void onClick(View v) {
        buildPLacePicker();
    }
}
