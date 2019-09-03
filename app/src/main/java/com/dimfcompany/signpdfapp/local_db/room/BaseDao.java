package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Color;

@Dao
public interface BaseDao<Model>
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Model model);

    @Update
    void update(Model... models);

    @Delete
    void deleteFull(Model... models);
}
