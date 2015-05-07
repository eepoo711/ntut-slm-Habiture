package utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class BitmapHelper {
    public static Bitmap loadScaledResource(Resources res, int id, int width, int height) {
        // get raw size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);

        // get sample size
        BitmapFactory.Options optScale = new BitmapFactory.Options();
        optScale.inSampleSize = calculateInSampleSize(optScale, width, height);

        //read resource
        Bitmap bitmapRaw = BitmapFactory.decodeResource(res, id, optScale);
        Bitmap bitmapReturned = Bitmap.createScaledBitmap(bitmapRaw, width, height, false);

        //free raw bitmap
        bitmapRaw.recycle();
        return bitmapReturned;
    }

    public static Bitmap loadScaledInputStream(InputStream inputStream, int width, int height) {
        // get raw size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // get sample size
        BitmapFactory.Options optScale = new BitmapFactory.Options();
        optScale.inSampleSize = calculateInSampleSize(optScale, width, height);

        //read resource
        Bitmap bitmapRaw = BitmapFactory.decodeStream(inputStream, null, optScale);
        Bitmap bitmapReturned = Bitmap.createScaledBitmap(bitmapRaw, width, height, false);

        //free raw bitmap
        bitmapRaw.recycle();
        return bitmapReturned;
    }

    public static Bitmap loadScaledPath(String path, int width, int height) {
        // get raw size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // get sample size
        BitmapFactory.Options optScale = new BitmapFactory.Options();
        optScale.inSampleSize = calculateInSampleSize(optScale, width, height);

        //read resource
        Bitmap bitmapRaw = BitmapFactory.decodeFile(path, optScale);
        Bitmap bitmapReturned = Bitmap.createScaledBitmap(bitmapRaw, width, height, false);

        //free raw bitmap
        bitmapRaw.recycle();
        return bitmapReturned;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
