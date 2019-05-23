package com.dimfcompany.signpdfapp.local_db.raw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.local_db.raw.SqliteHelper.ColumnsDocuments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrudHelper implements LocalDatabase
{
    private static final String TAG = "CrudHelper";

    private final Context context;

    public CrudHelper(Context context)
    {
        this.context = context;
    }

    @Override
    public List<Model_Document> getNotSyncedDocuments()
    {
        return null;
    }

    @Override
    public void deleteDocumentSoft(Model_Document document)
    {
            //???
    }

    @Override
    public void deleteAllLocalData()
    {

    }

    @Override
    public boolean hasNotSynced()
    {
        //???
        return false;
    }

    public void insertDocument(Model_Document document)
    {
        ContentValues values = new ContentValues();
        values.put(ColumnsDocuments.CITY,document.getCity());
        values.put(ColumnsDocuments.FIO,document.getFio());
        values.put(ColumnsDocuments.ADRESS,document.getAdress());
        values.put(ColumnsDocuments.PHONE,document.getPhone());
        values.put(ColumnsDocuments.PDF_FILE_NAME,document.getPdf_file_name());
//  ????      values.put(ColumnsDocuments.DATE,document.getDate()/1000);

        Uri uri = context.getContentResolver().insert(WintecProvider.CONTENT_URI_TABLE_DOCUMENTS,values);
        Log.e(TAG, "INSERTED URI IS " +uri);
    }

    public List<Model_Document> getAllSavedDocuments()
    {
        String sort = ColumnsDocuments._ID+" DESC";
        Cursor cursor = context.getContentResolver().query(WintecProvider.CONTENT_URI_TABLE_DOCUMENTS,null,null,null,sort);

        if(cursor == null)
        {
            Log.e(TAG, "getAllSavedDocuments: Error cursor is null" );
            return null;
        }

        if(cursor.getCount() == 0)
        {
            Log.e(TAG, "Cursor size is 0" );
            return null;
        }

        List<Model_Document> listOfDocuments = new ArrayList<>();

        while (cursor.moveToNext())
        {
            long id = cursor.getInt(cursor.getColumnIndex(ColumnsDocuments._ID));
            int city = cursor.getInt(cursor.getColumnIndex(ColumnsDocuments.CITY));
            String fio = cursor.getString(cursor.getColumnIndex(ColumnsDocuments.FIO));
            String adress = cursor.getString(cursor.getColumnIndex(ColumnsDocuments.ADRESS));
            String phone = cursor.getString(cursor.getColumnIndex(ColumnsDocuments.PHONE));
            String pdf_file_name = cursor.getString(cursor.getColumnIndex(ColumnsDocuments.PDF_FILE_NAME));
            long date = cursor.getInt(cursor.getColumnIndex(ColumnsDocuments.DATE));
            date *= 1000;

            Log.e(TAG, "getAllSavedDocuments: Getted date is "+date );

            Model_Document document = new Model_Document();
            document.setId(id);
            document.setCity(city);
            document.setFio(fio);
            document.setAdress(adress);
            document.setPhone(phone);
            document.setPdf_file_name(pdf_file_name);
            document.setDate(new Date(date));

            listOfDocuments.add(document);
        }

        return listOfDocuments;
    }

    @Override
    public void deleteDocumentFull(Model_Document document)
    {

    }
}
