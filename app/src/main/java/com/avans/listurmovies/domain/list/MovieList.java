package com.avans.listurmovies.domain.list;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.avans.listurmovies.domain.movie.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "movielist")
public class MovieList implements Serializable {

    private String created_by;

    private String description;

    private int favorite_count;

    @PrimaryKey
    private String id;


    private ArrayList<Movie> items = new ArrayList<>();

    private int item_count;


    private String iso_639_1;


    private String name;


    private String poster_path;

    public MovieList(String created_by, String description, int favorite_count, String id, ArrayList<Movie> items, int item_count, String iso_639_1, String name, String poster_path) {
        this.created_by = created_by;
        this.description = description;
        this.favorite_count = favorite_count;
        this.id = id;
        this.items = items;
        this.item_count = item_count;
        this.iso_639_1 = iso_639_1;
        this.name = name;
        this.poster_path = poster_path;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Movie> getItems() {
        return items;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public int getItem_count() {
        return item_count;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getPoster_path() {
        return poster_path;
    }
}
