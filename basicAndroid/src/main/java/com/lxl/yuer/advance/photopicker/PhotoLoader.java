package com.lxl.yuer.advance.photopicker;

import android.content.Context;
import android.content.CursorLoader;
import android.provider.MediaStore;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by Administrator on 2015/12/10.
 * 图片加载器
 */
public class PhotoLoader extends CursorLoader {

    final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    public PhotoLoader(Context context, boolean showGif) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(
                MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (showGif ? ("or " + MIME_TYPE + "=?") : ""));
        String[] selectionArgs;
        if (showGif) {
            selectionArgs = new String[] { "image/jpeg", "image/png", "image/gif" };
        } else {
            selectionArgs = new String[] { "image/jpeg", "image/png" };
        }
        setSelectionArgs(selectionArgs);
    }

}
