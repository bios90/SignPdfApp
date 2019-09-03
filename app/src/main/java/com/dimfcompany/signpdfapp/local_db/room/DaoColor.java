package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Color;

@Dao
public interface DaoColor extends BaseDao<Model_Color>
{
    @Query("SELECT * FROM colors WHERE product_id=:product_id LIMIT 1")
    Model_Color findColorsOfProduct(final long product_id);

    @Query("DELETE FROM colors")
    public void nukeTable();
}
