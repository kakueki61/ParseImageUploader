package org.kakueki61.image_uploader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * AsycTaskLoader for decoding Bitmap from ByteArray,
 * whose purpose is to avoid to process this heavy task on UI thread.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 */
public class BitmapDecodeLoader extends AsyncTaskLoader<Bitmap> {

    private static final String TAG = BitmapDecodeLoader.class.getSimpleName();

    private byte[] bitmapByteArray;

    public BitmapDecodeLoader(Context context, byte[] bitmapByteArray) {
        super(context);

        this.bitmapByteArray = bitmapByteArray;
    }

    @Override
    public Bitmap loadInBackground() {
        Log.d(TAG, "loadInBackground");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
        return bitmap;
    }

}
