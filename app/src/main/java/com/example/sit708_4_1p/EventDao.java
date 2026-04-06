package com.example.sit708_4_1p;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface EventDao {


    @Insert
    void insert(Event event);


    @Update
    void update(Event event);


    @Delete
    void delete(Event event);


    @Query("SELECT * FROM event_table ORDER BY date ASC")
    LiveData<List<Event>> getAllEvents();
}