package com.dimfcompany.signpdfapp.local_db.raw;

import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface LocalDatabase
{
    void insertDocument(Model_Document document);
    void deleteDocument(Model_Document document);
    List<Model_Document> getAllSavedDocuments();
}
