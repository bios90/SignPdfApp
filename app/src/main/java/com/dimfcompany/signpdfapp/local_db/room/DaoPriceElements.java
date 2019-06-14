package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Query;

import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Vaucher;

import java.util.List;

@Dao
public interface DaoPriceElements extends BaseDao<Model_Price_Element>
{
    @Query("SELECT * FROM price_elements WHERE vaucher_id=:vaucher_id")
    List<Model_Price_Element> getPriceElementsOfVaucher(final long vaucher_id);
}
