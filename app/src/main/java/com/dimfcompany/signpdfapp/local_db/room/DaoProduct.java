package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimfcompany.signpdfapp.models.Model_Color;
import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Krep;
import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.util.List;

@Dao
public interface DaoProduct extends BaseDao<Model_Product>
{
    @Query("SELECT * FROM colors WHERE product_id=:product_id LIMIT 1")
    Model_Color getProductColor(long product_id);

    @Query("SELECT * FROM controls WHERE product_id=:product_id LIMIT 1")
    Model_Control getProductControl(long product_id);

    @Query("SELECT * FROM kreps WHERE product_id=:product_id LIMIT 1")
    Model_Krep getProductKrep(long product_id);

    @Query("SELECT * FROM materials WHERE product_id=:product_id LIMIT 1")
    Model_Material getProductMaterial(long product_id);

    @Query("DELETE FROM products")
    public void nukeTable();
}
