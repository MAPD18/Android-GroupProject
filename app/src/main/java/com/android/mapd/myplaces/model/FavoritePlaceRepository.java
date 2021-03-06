package com.android.mapd.myplaces.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.android.mapd.myplaces.AppDatabase;

import java.util.List;

public class FavoritePlaceRepository {

    private FavoritePlaceDao favoritePlaceDao;
    private LiveData<List<FavoritePlace>> allFavoritePlaces;

    public FavoritePlaceRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        favoritePlaceDao = appDatabase.favoritePlaceDao();
        allFavoritePlaces = favoritePlaceDao.getAll();
    }

    public void insert(FavoritePlace favoritePlace) {
        new InsertAsyncTask(favoritePlaceDao).execute(favoritePlace);
    }

    public LiveData<List<FavoritePlace>> getAllFavoritePlaces() {
        return allFavoritePlaces;
    }

    public void delete(FavoritePlace favoritePlace) {
        new DeleteAsyncTask(favoritePlaceDao).execute(favoritePlace);
    }

    public void nukeFavoritePlacesTable() {
        new NukeFavoritePlacesTableAsyncTask(favoritePlaceDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<FavoritePlace, Void, Void> {

        private FavoritePlaceDao favoritePlaceDao;

        InsertAsyncTask(FavoritePlaceDao favoritePlaceDao) {
            this.favoritePlaceDao = favoritePlaceDao;
        }

        @Override
        protected Void doInBackground(FavoritePlace... favoritePlaces) {
            favoritePlaceDao.insert(favoritePlaces[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<FavoritePlace, Void, Void> {

        private FavoritePlaceDao favoritePlaceDao;

        DeleteAsyncTask(FavoritePlaceDao favoritePlaceDao) {
            this.favoritePlaceDao = favoritePlaceDao;
        }

        @Override
        protected Void doInBackground(FavoritePlace... favoritePlaces) {
            favoritePlaceDao.delete(favoritePlaces[0]);
            return null;
        }
    }

    private static class NukeFavoritePlacesTableAsyncTask extends AsyncTask<Void, Void, Void> {

        private FavoritePlaceDao favoritePlaceDao;

        NukeFavoritePlacesTableAsyncTask(FavoritePlaceDao favoritePlaceDao) {
            this.favoritePlaceDao = favoritePlaceDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favoritePlaceDao.nukeFavoritePlacesTable();
            return null;
        }
    }
}
