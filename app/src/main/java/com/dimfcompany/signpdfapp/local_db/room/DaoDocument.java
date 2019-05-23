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

    @Query("SELECT * FROM documents WHERE deleted_at IS NULL ORDER BY date DESC")
    List<Model_Document> getAllDocuments();

    @Query("SELECT * FROM documents")
    List<Model_Document> getAllDocumentsWithDeleted();

    @Query("SELECT * FROM documents WHERE sync_status = 0")
    List<Model_Document> getNotSyncedDocuments();

    @Query("SELECT * FROM documents WHERE id=:id LIMIT 1")
    Model_Document getDocumentById(final long id);

    @Query("UPDATE documents SET deleted_at=:deleted_at, sync_status=0 WHERE id=:document_id")
    void deleteSoftByUserId(long document_id, long deleted_at);
}
