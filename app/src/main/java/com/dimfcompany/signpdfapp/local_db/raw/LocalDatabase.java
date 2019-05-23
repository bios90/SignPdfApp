package com.dimfcompany.signpdfapp.local_db.raw;

import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface LocalDatabase
{
    void insertDocument(Model_Document document);
    void deleteDocumentFull(Model_Document document);
    void deleteDocumentSoft(Model_Document document);
    void deleteAllLocalData();
    boolean hasNotSynced();
    List<Model_Document> getAllSavedDocuments();
    List<Model_Document> getNotSyncedDocuments();

}
