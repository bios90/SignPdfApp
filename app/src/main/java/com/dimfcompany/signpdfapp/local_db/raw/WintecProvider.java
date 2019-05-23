package com.dimfcompany.signpdfapp.local_db.raw;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WintecProvider extends ContentProvider
{
    private static final String TAG = "WintecProvider";

    private static SqliteHelper sqliteHelper;

    public static String PROVIDER_NAME = "com.dimfcompany.signpdfapp.wintecprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME);
    public static final Uri CONTENT_URI_TABLE_DOCUMENTS = Uri.withAppendedPath(CONTENT_URI, SqliteHelper.TABLE_NAME_DOCUMENTS);

    private static final int MATCHER_INT_DOCUMENTS = 100;
    private static final int MATCHER_INT_DOCUMENTS_WITH_ID = 101;

    public static final String CONTENT_TYPE_DOCUMENTS_TABLE = "vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "." + SqliteHelper.TABLE_NAME_DOCUMENTS;
    public static final String CONTENT_ITEM_DOCUMENTS_ITEM = "vnd.android.cursor.item/vnd." + PROVIDER_NAME + "." + SqliteHelper.TABLE_NAME_DOCUMENTS;

    public static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PROVIDER_NAME, SqliteHelper.TABLE_NAME_DOCUMENTS, MATCHER_INT_DOCUMENTS);
        matcher.addURI(PROVIDER_NAME, SqliteHelper.TABLE_NAME_DOCUMENTS + "/#", MATCHER_INT_DOCUMENTS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate()
    {
        sqliteHelper = SqliteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        Log.e(TAG, "query: Begin Query on WintecProvider");

        final int matched = uriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (matched)
        {
            case MATCHER_INT_DOCUMENTS:
                queryBuilder.setTables(SqliteHelper.TABLE_NAME_DOCUMENTS);
                break;
            case MATCHER_INT_DOCUMENTS_WITH_ID:
                queryBuilder.setTables(SqliteHelper.TABLE_NAME_DOCUMENTS);
                long documentId = getIdFromUri(uri);
                queryBuilder.appendWhere(SqliteHelper.ColumnsDocuments._ID + " = " + documentId);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        Log.e(TAG, "insert: Called Insert on Wintec Provider");

        final int matched = uriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long insertedId;

        switch (matched)
        {
            case MATCHER_INT_DOCUMENTS:
                db = sqliteHelper.getWritableDatabase();
                insertedId = db.insert(SqliteHelper.TABLE_NAME_DOCUMENTS, null, values);

                if (insertedId < 1)
                {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }

                returnUri = buildUriForElementId(insertedId, SqliteHelper.TABLE_NAME_DOCUMENTS);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri :" + uri);
        }

        Log.e(TAG, "insert: Made succesfull insert into Rocc Database");
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        Log.e(TAG, "deleteFull: Called Delete on Wintec Provider");

        final int matched = uriMatcher.match(uri);
        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (matched)
        {
            case MATCHER_INT_DOCUMENTS:
                db = sqliteHelper.getWritableDatabase();
                count = db.delete(SqliteHelper.TABLE_NAME_DOCUMENTS, selection, selectionArgs);
                break;

            case MATCHER_INT_DOCUMENTS_WITH_ID:
                db = sqliteHelper.getWritableDatabase();
                long documentId = getIdFromUri(uri);
                selectionCriteria = SqliteHelper.ColumnsDocuments._ID + " = " + documentId;
                if (selection != null && selection.length() > 0)
                {
                    selectionCriteria += " AND ( " + selection + " )";
                }
                count = db.delete(SqliteHelper.TABLE_NAME_DOCUMENTS, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri :" + uri);
        }


        if (count > 0)
        {
            Log.d(TAG, "deleteFull: setting notify changed with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else
        {
            Log.d(TAG, "deleteFull: nothing deleted");
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match)
        {
            case MATCHER_INT_DOCUMENTS:
                db = sqliteHelper.getWritableDatabase();
                count = db.update(SqliteHelper.TABLE_NAME_DOCUMENTS, values, selection, selectionArgs);
                break;

            case MATCHER_INT_DOCUMENTS_WITH_ID:
                db = sqliteHelper.getWritableDatabase();
                long documentId = getIdFromUri(uri);

                selectionCriteria = SqliteHelper.ColumnsDocuments._ID + " = " + documentId;
                if (selection != null && selection.length() > 0)
                {
                    selectionCriteria += " AND ( " + selection + " )";
                }
                count = db.update(SqliteHelper.TABLE_NAME_DOCUMENTS, values, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri :" + uri);
        }

        if (count > 0)
        {
            Log.d(TAG, "updateFinishedCardUi: setting notify changed with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else
        {
            Log.d(TAG, "updateFinishedCardUi: nothing updateFinishedCardUi");
        }

        return count;
    }

    public static long getIdFromUri(Uri uri)
    {
        return ContentUris.parseId(uri);
    }


    public static Uri buildUriForElementId(long id, String tableName)
    {
        if (tableName.equals(SqliteHelper.TABLE_NAME_DOCUMENTS))
        {
            return ContentUris.withAppendedId(CONTENT_URI_TABLE_DOCUMENTS, id);
        }

        Log.e(TAG, "buildUriForElementId: Error on building URI with id ");
        return null;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        final int mathcer = uriMatcher.match(uri);
        switch (mathcer)
        {
            case MATCHER_INT_DOCUMENTS:
                return CONTENT_TYPE_DOCUMENTS_TABLE;

            case MATCHER_INT_DOCUMENTS_WITH_ID:
                return CONTENT_ITEM_DOCUMENTS_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
