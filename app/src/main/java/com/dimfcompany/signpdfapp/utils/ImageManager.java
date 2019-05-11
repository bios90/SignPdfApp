package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageManager
{
    private static final String TAG = "ImageManager";

    public static void setFileToImageView(File file, ImageView imageView)
    {
        Picasso.get().load(file).fit().centerCrop().into(imageView);
    }

    public static Bitmap getBitmapFromFile(File file)
    {
        if(file == null)
        {
            return null;
        }

        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    public static Bitmap newResizedBitmap(File file, int maxSize)
    {
        String filePath = file.getPath();
        Bitmap image = BitmapFactory.decodeFile(filePath);
        image = ExifUtil.rotateBitmap(filePath, image);

        int width = image.getWidth();
        int height = image.getHeight();

        if (height <= (maxSize + 50) || width <= (maxSize + 50))
        {
            Log.e(TAG, "newResizedBitmap: size is less returninig original");
            return image;
        }

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1)
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        Log.e(TAG, "****** newResizedBitmap: will image,  new width is " + width + " new hieght is " + height + "********");
        return Bitmap.createScaledBitmap(image, width, height, false);
    }

    public static void setBackgroundDrawable(View view, @DrawableRes int resId)
    {
        Context context = view.getContext();
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(context.getDrawable(resId) );
        } else {
            view.setBackground(ContextCompat.getDrawable(context, resId));
        }
    }
}
