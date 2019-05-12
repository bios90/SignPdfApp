package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Dao;
import androidx.room.Query;

import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.util.List;

@Dao
public interface DaoDocument extends BaseDao<Model_Document>
{
    @Query("SELECT * FROM products WHERE document_id=:document_id")
    List<Model_Product> getDocumentProducts(long document_id);

    @Query("SELECT * FROM documents ORDER BY date DESC")
    List<Model_Document> getAllDocuments();

    @Query("SELECT * FROM documents WHERE id=:id LIMIT 1")
    Model_Document getDocymentById(final long id);
}
