package com.avans.listurmovies.dataacess.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.avans.listurmovies.domain.movie.Movie;

import java.util.Date;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class MovieRoomDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movies")
                            .fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDAO mDao;

        PopulateDbAsync(MovieRoomDatabase db) {
            mDao = db.movieDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();

            Movie m1 = new Movie(1, "test film", "bla bla bla", new Date(), new int[1], "Japans", "alfjaslf", "1", "2", 5.0, 2.0, 2, true);
            mDao.insert(m1);

            return null;
        }
    }
}
