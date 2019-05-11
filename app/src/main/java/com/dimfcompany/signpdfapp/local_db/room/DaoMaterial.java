package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Material;

@Dao
public interface DaoMaterial extends BaseDao<Model_Material>
{
    @Query("SELECT * FROM materials WHERE product_id=:product_id LIMIT 1")
    Model_Material findMaterialsOfProduct(final long product_id);
}
