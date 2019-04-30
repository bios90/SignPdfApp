package com.dimfcompany.signpdfapp.sqlite;

import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface LocalDatabase
{
    void insertDocument(Model_Document document);
    List<Model_Document> getAllSavedDocuments();
}
