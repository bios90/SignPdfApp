package com.dimfcompany.signpdfapp.local_db.room;

import android.util.Log;

import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.util.List;

public class RoomCrudHelper implements LocalDatabase
{
    private static final String TAG = "RoomCrudHelper";

    AppDatabase appDatabase;

    public RoomCrudHelper(AppDatabase appDatabase)
    {
        this.appDatabase = appDatabase;
    }

    @Override
    public void insertDocument(Model_Document document)
    {
        long id_document = appDatabase.getDaoDocument().insert(document);

        for (Model_Product product : document.getListOfProducts())
        {
            product.setDocument_id(id_document);

            long id_product = appDatabase.getDaoProduct().insert(product);

            if(product.getColor() != null)
            {
                product.getColor().setProduct_id(id_product);
                appDatabase.getDaoColor().insert(product.getColor());
            }

            if(product.getControl() != null)
            {
                product.getControl().setProduct_id(id_product);
                appDatabase.getDaoControl().insert(product.getControl());
            }

            if(product.getKrep() != null)
            {
                product.getKrep().setProduct_id(id_product);
                appDatabase.getDaoKrep().insert(product.getKrep());
            }

            if(product.getMaterial() != null)
            {
                product.getMaterial().setProduct_id(id_product);
                appDatabase.getDaoMaterial().insert(product.getMaterial());
            }
        }

        Log.e(TAG, "insertDocument: Inserted document all okey");
    }

    @Override
    public List<Model_Document> getAllSavedDocuments()
    {
        List<Model_Document> documents = appDatabase.getDaoDocument().getAllDocuments();

        for (Model_Document document : documents)
        {
            document.setListOfProducts(getDocumentProducts(document));
        }

        return documents;
    }

    @Override
    public List<Model_Document> getNotSyncedDocuments()
    {
        List<Model_Document> documents = appDatabase.getDaoDocument().getNotSyncedDocuments();

        for (Model_Document document : documents)
        {
            document.setListOfProducts(getDocumentProducts(document));
        }

        return documents;
    }

    private List<Model_Product> getDocumentProducts(Model_Document document)
    {
        List<Model_Product> products = appDatabase.getDaoDocument().getDocumentProducts(document.getId());

        for (Model_Product product : products)
        {
            product.setColor(appDatabase.getDaoColor().findColorsOfProduct(product.getId()));
            product.setControl(appDatabase.getDaoControl().findControlsOfProduct(product.getId()));
            product.setKrep(appDatabase.getDaoKrep().findKrepsOfProduct(product.getId()));
            product.setMaterial(appDatabase.getDaoMaterial().findMaterialsOfProduct(product.getId()));
        }

        return products;
    }

    @Override
    public void deleteDocumentSoft(Model_Document document)
    {
        appDatabase.getDaoDocument().deleteSoftByUserId(document.getId(),System.currentTimeMillis());
    }

    @Override
    public boolean hasNotSynced()
    {
        List<Model_Document> notSynced = getNotSyncedDocuments();
        return notSynced != null && notSynced.size() > 0;
    }

    @Override
    public void deleteDocumentFull(Model_Document document)
    {

        for (Model_Product product : document.getListOfProducts())
        {
            if(product.getColor() != null)
            {
                appDatabase.getDaoColor().deleteFull(product.getColor());
            }

            if(product.getControl()!=null)
            {
                appDatabase.getDaoControl().deleteFull(product.getControl());
            }

            if(product.getMaterial() != null)
            {
                appDatabase.getDaoMaterial().deleteFull(product.getMaterial());
            }

            if(product.getKrep() != null)
            {
                appDatabase.getDaoKrep().deleteFull(product.getKrep());
            }

            appDatabase.getDaoProduct().deleteFull(product);
        }

        appDatabase.getDaoDocument().deleteFull(document);
    }

    @Override
    public void deleteAllLocalData()
    {
        List<Model_Document> documents = getAllSavedDocuments();
        for(Model_Document document : documents)
        {
            deleteDocumentFull(document);
        }
    }
}
