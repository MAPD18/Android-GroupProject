package com.android.mapd.myplaces;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.android.mapd.myplaces.model.FavoritePlace;
import com.android.mapd.myplaces.model.FavoritePlaceDao;

@Database(entities = {FavoritePlace.class}, version = 1)
@TypeConverters(FavoritePlace.CategoryConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoritePlaceDao favoritePlaceDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "my_places_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
