package com.android.mapd.myplaces.view;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.mapd.myplaces.AppConstants;
import com.android.mapd.myplaces.view.fragment.ListFragment;
import com.android.mapd.myplaces.view.fragment.MapFragment;
import com.android.mapd.myplaces.R;
import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ListFragment.OnListFragmentInteractionListener {

    private int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.navigation)
    public BottomNavigationView navigation;
    @BindView(R.id.content)
    public FrameLayout content;

    private FragmentManager fragmentManager;
    private static FavoritePlaceViewModel viewModel;
    private static Place selectedPlace;

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
        } else if (item.getItemId() == R.id.actionbar_menu_logout) {
            logoff();
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

    private void logoff() {
        viewModel.nukeFavoritePLacesTable();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedPlace = PlacePicker.getPlace(this, data);
                new ChoseCategoryDialog().show(getSupportFragmentManager(), "categories");
            }
        }
    }

    @Override
    public void onFavoritePlaceDeleted(FavoritePlace item) {
        viewModel.delete(item);
    }

    @Override
    public void onUriClicked(String uri) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AppConstants.PLACE_WEBSITE_URI_KEY, uri);
        startActivity(intent);
    }

    private enum TabFragment {
        MAP, LIST
    }

    public static class ChoseCategoryDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.chose_category_dialog_title)
                    .setItems(R.array.categories, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FavoritePlace.Category selectedCategory = FavoritePlace.Category.RESTAURANT;
                            switch (which) {
                                case 0:
                                    selectedCategory = FavoritePlace.Category.RESTAURANT;
                                    break;

                                case 1:
                                    selectedCategory = FavoritePlace.Category.MOVIE_THEATER;
                                    break;

                                case 2:
                                    selectedCategory = FavoritePlace.Category.BAR;
                                    break;

                            }
                            viewModel.insert(selectedPlace, selectedCategory);
                            selectedPlace = null;
                        }
                    });
            return builder.create();
        }

    }
}
