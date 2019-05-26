package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileManager
{
    private static final String TAG = "FileManager";

    private final Context context;

    public FileManager(Context context)
    {
        this.context = context;
    }

    public File createRandomNameFile(String extansion)
    {
        return createRandomNameFile(extansion,Constants.FOLDER_TEMP_FILES);
    }

    public File createRandomNameFile(String extansion,String folder)
    {
        return createFile(StringManager.randomStr(),extansion,folder);
    }

    public File createFile(String name,@Nullable String extension)
    {
        return createFile(name,extension,Constants.FOLDER_TEMP_FILES);
    }

    public File createFile(String name, @Nullable String extension, String folder)
    {
        try
        {
            File file;

            String root = context.getExternalFilesDir(null).toString();
            File folder_file = new File(root + "/" + folder);
            if (!folder_file.exists())
            {
                folder_file.mkdirs();
            }

            if (extension != null)
            {
                name = name + "." + extension;
            }

            file = new File(folder_file, name);
            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();

            return file;
        } catch (Exception e)
        {
            return null;
        }
    }


    public File getFileFromTemp(String fileName, @Nullable String extansion)
    {
        return getFileFromTemp(fileName, Constants.FOLDER_TEMP_FILES, extansion);
    }


    public File getFileFromTemp(String fileName, String folder, @Nullable String extansion)
    {
        try
        {
            File file;
            String root = context.getExternalFilesDir(null).toString();
            File dir_temp_files = new File(root + "/" + folder);
            if (extansion != null)
            {
                fileName += "." + extansion;
            }
            file = new File(dir_temp_files, fileName);

            return file;
        } catch (Exception e)
        {
            Log.e(TAG, "createRandomNameFile: Excetpion on getting file" + e.getMessage());
            return null;
        }
    }

    public static boolean rename(File from, String newName, @Nullable String extansion)
    {
        try
        {
            File dir = from.getParentFile();
            if (extansion != null)
            {
                newName = newName + "." + extansion;
            }

            File destinaition = new File(dir, newName);
            destinaition.createNewFile();

            Log.e(TAG, "rename: new file on rename " + destinaition.getAbsolutePath());

            return from.getParentFile().exists() && from.exists() && from.renameTo(destinaition);

        } catch (Exception e)
        {
            Log.e(TAG, "rename: " + e.getMessage());
            return false;
        }
    }

    public File saveBitmapToFile(Bitmap bitmap)
    {
        try
        {
            File file = createRandomNameFile(Constants.EXTANSION_PNG);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            return file;
        } catch (IOException e)
        {
            Log.e(TAG, "saveBitmapToFile: Error on saving bitmap to file");
            return null;
        }
    }

    public static String getFileName(File file)
    {
        if (file == null)
        {
            return null;
        }
        return file.getName();
    }

    public InputStream getTemplateStream()
    {
        try
        {
            InputStream inputStream = context.getAssets().open("dogovor.pdf");
            return inputStream;
        } catch (Exception e)
        {
            Log.e(TAG, "getTemplateStream: Exception On opening dogovor.pdf");
            return null;
        }
    }

    public void deleteAllFiles()
    {
        deleteSignatures();
        deleteDocuments();
        deleteChecks();
    }

    private void deleteSignatures()
    {
        String root = context.getExternalFilesDir(null).toString();
        File folder = new File(root + "/" + Constants.FOLDER_TEMP_FILES);
        if (folder != null && folder.exists())
        {
            deleteRecursive(folder);
        }
    }

    private void deleteDocuments()
    {
        String root = context.getExternalFilesDir(null).toString();
        File folder = new File(root + "/" + Constants.FOLDER_CONTRACTS);
        if (folder != null && folder.exists())
        {
            deleteRecursive(folder);
        }
    }

    private void deleteChecks()
    {
        String root = context.getExternalFilesDir(null).toString();
        File folder = new File(root + "/" + Constants.FOLDER_CHECKS);
        if (folder != null && folder.exists())
        {
            deleteRecursive(folder);
        }
    }

    void deleteRecursive(File fileOrDirectory)
    {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }
}
