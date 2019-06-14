package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Query;

import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Vaucher;

@Dao
public interface DaoVaucher extends BaseDao<Model_Vaucher>
{
    @Query("SELECT * FROM vauchers WHERE document_id=:document_id LIMIT 1")
    Model_Vaucher getVaucherOfDocument(final long document_id);
}
