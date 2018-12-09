package com.android.mapd.myplaces.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.mapd.myplaces.view.fragment.ListFragment;
import com.android.mapd.myplaces.view.fragment.MapFragment;
import com.android.mapd.myplaces.R;
import com.android.mapd.myplaces.dummy.DummyContent;
import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ListFragment.OnListFragmentInteractionListener {

    private int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.navigation)
    public BottomNavigationView navigation;
    @BindView(R.id.content)
    public FrameLayout content;

    private FragmentManager fragmentManager;
    private FavoritePlaceViewModel viewModel;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_map:
                startFragment(TabFragment.MAP);
                return true;
            case R.id.navigation_list:
                startFragment(TabFragment.LIST);
                return true;
        }
        return false;
    }

    private void startFragment(TabFragment tabFragment) {
        Fragment fragmentToCommit;
        if (tabFragment == TabFragment.LIST)
            fragmentToCommit = ListFragment.newInstance();
        else
            fragmentToCommit = MapFragment.newInstance();
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content, fragmentToCommit)
                .commit();
    }

    private void startFragment() {
        startFragment(TabFragment.MAP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(this);
        viewModel = ViewModelProviders.of(this).get(FavoritePlaceViewModel.class);

        startFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionbar_menu_add) {
            buildPlacePicker();
        }
        return super.onOptionsItemSelected(item);
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
                Place place = PlacePicker.getPlace(this, data);
                viewModel.insert(place, FavoritePlace.Category.BAR); // TODO ALterar Category, fazer selecao

                String toastMsg = String.format("New Place: %s added to Favorites", place.getName());
                //Snackbar.make(coordinatorLayout, toastMsg, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    private enum TabFragment {
        MAP, LIST
    }
}
