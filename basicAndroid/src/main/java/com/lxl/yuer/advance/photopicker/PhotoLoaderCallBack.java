package com.lxl.yuer.advance.photopicker;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * Created by Administrator on 2015/12/10.
 */
@Deprecated
public class PhotoLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;

    public PhotoLoaderCallBack(Context context) {
        mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        PhotoDirectory photoDirectory=new PhotoDirectory();
        while (data.moveToNext()) {
            int imageId  = data.getInt(data.getColumnIndexOrThrow(_ID));
            String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            Log.d("PhotoLoaderCallBack", "imageId:" + imageId+
                            "bucketId:" + bucketId+
                            "name:" + name+
                            "path:" + path
            );
            photoDirectory.addPhoto(new Photo(imageId,path));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
