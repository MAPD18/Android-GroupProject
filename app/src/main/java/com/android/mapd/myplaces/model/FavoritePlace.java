package com.android.mapd.myplaces.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import static com.android.mapd.myplaces.model.FavoritePlace.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class FavoritePlace {

    static final String TABLE_NAME = "favorite_place";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "address")
    private String address;
    @Embedded
    private Coordinates coordinates;
    @ColumnInfo(name = "uri_website")
    private String uriWebsite;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @ColumnInfo(name = "rating")
    private float rating;
    @TypeConverters(CategoryConverter.class)
    private Category category;


    public FavoritePlace() {
    }

    public FavoritePlace(Place place, Category category) {
        this.name = place.getName().toString();
        this.address = place.getAddress() == null ? "" : place.getAddress().toString();
        this.coordinates = new Coordinates(place.getLatLng());
        this.uriWebsite = place.getWebsiteUri() == null ? "" : place.getWebsiteUri().toString();
        this.phoneNumber = place.getPhoneNumber() == null ? "" : place.getPhoneNumber().toString();
        this.rating = place.getRating();
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getCoordinatesAsLatLng() {
        return new LatLng(coordinates.lat, coordinates.lng);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = new Coordinates(coordinates);
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getUriWebsite() {
        return uriWebsite;
    }

    public void setUriWebsite(String uriWebsite) {
        this.uriWebsite = uriWebsite;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public static class Coordinates {
        double lat;
        double lng;

        public Coordinates() {
        }

        Coordinates(LatLng latLng) {
            this.lat = latLng.latitude;
            this.lng = latLng.longitude;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public enum Category {
        RESTAURANT(0), MOVIE_THEATER(1), BAR(2);

        private final int value;

        Category(int value) {
            this.value = value;
        }
    }

    public static class CategoryConverter {

        @TypeConverter
        public static Category toCategory(int value) {
            if (value == Category.RESTAURANT.value)
                    return Category.RESTAURANT;
            else if (value == Category.MOVIE_THEATER.value)
                return Category.MOVIE_THEATER;
            else
                return Category.BAR;
        }

        @TypeConverter
        public static int toInt(Category category) {
            return category.value;
        }
    }

}
