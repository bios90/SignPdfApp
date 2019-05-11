package com.dimfcompany.signpdfapp.local_db.room;

import android.util.Log;

import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.util.ArrayList;
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

            product.getColor().setProduct_id(id_product);
            product.getControl().setProduct_id(id_product);
            product.getKrep().setProduct_id(id_product);
            product.getMaterial().setProduct_id(id_product);

            appDatabase.getDaoColor().insert(product.getColor());
            appDatabase.getDaoControl().insert(product.getControl());
            appDatabase.getDaoKrep().insert(product.getKrep());
            appDatabase.getDaoMaterial().insert(product.getMaterial());
        }

        Log.e(TAG, "insertDocument: Inserted document all okey");
    }

    @Override
    public List<Model_Document> getAllSavedDocuments()
    {
        List<Model_Document> documents = appDatabase.getDaoDocument().getAllDocuments();

        for (Model_Document document : documents)
        {
            List<Model_Product> products = appDatabase.getDaoDocument().getDocumentProducts(document.getId());
            document.setListOfProducts(products);

            for (Model_Product product : products)
            {
                product.setColor(appDatabase.getDaoColor().findColorsOfProduct(product.getId()));
                product.setControl(appDatabase.getDaoControl().findControlsOfProduct(product.getId()));
                product.setKrep(appDatabase.getDaoKrep().findKrepsOfProduct(product.getId()));
                product.setMaterial(appDatabase.getDaoMaterial().findMaterialsOfProduct(product.getId()));
            }
        }

        return documents;
    }

    @Override
    public void deleteDocument(Model_Document document)
    {

        for (Model_Product product : document.getListOfProducts())
        {
            appDatabase.getDaoColor().delete(product.getColor());
            appDatabase.getDaoControl().delete(product.getControl());
            appDatabase.getDaoMaterial().delete(product.getMaterial());
            appDatabase.getDaoKrep().delete(product.getKrep());

            appDatabase.getDaoProduct().delete(product);
        }

        appDatabase.getDaoDocument().delete(document);
    }

}
