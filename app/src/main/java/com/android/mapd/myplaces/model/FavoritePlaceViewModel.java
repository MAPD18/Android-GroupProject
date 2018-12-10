package com.android.mapd.myplaces.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;

import java.util.List;

public class FavoritePlaceViewModel extends AndroidViewModel {

    private FavoritePlaceRepository favoritePlaceRepository;
    private LiveData<List<FavoritePlace>> allFavoritePlaces;

    public FavoritePlaceViewModel(@NonNull Application application) {
        super(application);
        favoritePlaceRepository = new FavoritePlaceRepository(application);
        allFavoritePlaces = favoritePlaceRepository.getAllFavoritePlaces();
    }

    public LiveData<List<FavoritePlace>> getAllFavoritePlaces() {
        return allFavoritePlaces;
    }

    public void insert(Place place, FavoritePlace.Category selectedCategory) {
        favoritePlaceRepository.insert(new FavoritePlace(place, selectedCategory));
    }

    public void delete(FavoritePlace favoritePlace) {
        favoritePlaceRepository.delete(favoritePlace);
    }
}
