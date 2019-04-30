package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

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
        try
        {
            File file;

            String root = context.getExternalFilesDir(null).toString();
            File dir_temp_files = new File(root +"/"+ folder);
            if (!dir_temp_files.exists())
            {
                dir_temp_files.mkdirs();
            }
            String randomName = StringManager.randomStr() +"."+extansion;
            file = new File(dir_temp_files, randomName);
            file.createNewFile();

            return file;
        } catch (Exception e)
        {
            Log.e(TAG, "createRandomNameFile: Excetpion on file create" + e.getMessage());
            return null;
        }
    }

    public File getFileFromTemp(String fileName, @Nullable String extansion)
    {
        return getFileFromTemp(fileName,Constants.FOLDER_TEMP_FILES,extansion);
    }


    public File getFileFromTemp(String fileName,String folder, @Nullable String extansion)
    {
        try
        {
            File file;
            String root = context.getExternalFilesDir(null).toString();
            File dir_temp_files = new File(root +"/"+folder);
            if (extansion != null)
            {
                fileName += "."+extansion;
            }
            file = new File(dir_temp_files, fileName);

            return file;
        }
        catch (Exception e)
        {
            Log.e(TAG, "createRandomNameFile: Excetpion on getting file" + e.getMessage());
            return null;
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
        if(file == null)
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
        }
        catch (Exception e)
        {
            Log.e(TAG, "getTemplateStream: Exception On opening dogovor.pdf");
            return null;
        }
    }
}
