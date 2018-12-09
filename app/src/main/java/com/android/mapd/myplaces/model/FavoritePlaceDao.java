package com.android.mapd.myplaces.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoritePlaceDao {

    @Query("SELECT * from " + FavoritePlace.TABLE_NAME)
    LiveData<List<FavoritePlace>> getAll();

    @Insert
    void insert(FavoritePlace favoritePlace);
}
