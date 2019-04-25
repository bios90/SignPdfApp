package com.dimfcompany.signpdfapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SqliteHelper extends SQLiteOpenHelper
{
    private static final String TAG = "SqliteHelper";

    public static final String DATABASE_NAME = "wintec_db";
    public static final String TABLE_NAME_DOCUMENTS = "documents";
    public static final int DATABASE_VERSION = 1;

    private final Context context;
    private static SqliteHelper instance;


    public static SqliteHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SqliteHelper(context);
        }
        return instance;
    }

    public SqliteHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createDocumentsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOCUMENTS);
        createDocumentsTable(db);
    }



    private void createDocumentsTable(SQLiteDatabase db)
    {
        final String create_documents_table =
                "CREATE TABLE  "+ TABLE_NAME_DOCUMENTS
                        +"( "+ ColumnsDocuments._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        + ColumnsDocuments.CITY+" INTEGER ,"
                        + ColumnsDocuments.FIO+" TEXT NOT NULL ,"
                        + ColumnsDocuments.ADRESS+" TEXT NOT NULL ,"
                        + ColumnsDocuments.PHONE+" TEXT NOT NULL ,"
                        + ColumnsDocuments.PDF_FILE_NAME+" TEXT NOT NULL ,"
                        + ColumnsDocuments.DATE+" INTEGER "
                        +")";
        db.execSQL(create_documents_table);

    }

    public static class ColumnsDocuments
    {
        public static final String _ID = BaseColumns._ID;
        public static final String CITY = "city";
        public static final String FIO = "fio";
        public static final String ADRESS = "adress";
        public static final String PHONE = "phone";
        public static final String PDF_FILE_NAME = "pdf_file_name";
        public static final String DATE = "date";
    }
}
