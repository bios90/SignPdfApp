package com.dimfcompany.signpdfapp.sync;

import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface Synchronizer
{
    void insertDocumentWithSync(Model_Document document, SyncManager.CallbackInsertWithSync callback);
    void deleteDocumentWithSync(Model_Document document);
    void syncronizeNotSynced(SyncManager.CallbackSyncronizeNoSynced callback);
    void putSynchronizeTask();
    void makeSyncFromServer(SyncManager.CallbackSyncFromServer callback);
}
