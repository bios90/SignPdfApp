package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Krep;

@Dao
public interface DaoKrep extends BaseDao<Model_Krep>
{
    @Query("SELECT * FROM kreps WHERE product_id=:product_id LIMIT 1")
    Model_Krep findKrepsOfProduct(final long product_id);

    @Query("DELETE FROM kreps")
    public void nukeTable();
}
