package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Control;

@Dao
public interface DaoControl extends BaseDao<Model_Control>
{
    @Query("SELECT * FROM controls WHERE product_id=:product_id LIMIT 1")
    Model_Control findControlsOfProduct(final long product_id);

    @Query("DELETE FROM controls")
    public void nukeTable();
}
