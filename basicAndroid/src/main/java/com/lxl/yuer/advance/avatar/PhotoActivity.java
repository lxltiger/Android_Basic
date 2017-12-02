package com.lxl.yuer.advance.avatar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lxl.yuer.advance.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * get big image from camera or gallery
 *skill  mainly from official
 */
public class PhotoActivity extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_PICTURE = 1;
    private static final String PREFIX_PHOTO = "JPEG_";
    private static final String SUFFIX_PHOTO= ".jpg";
    private static final String PREFIX_VIDEO = "VIDEO_";
    private static final String SUFFIX_VIDEO= ".mp4";
    private static final int REQUEST_CAPTURE_VIDEO = 2;

    private ImageView mImageView;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_camera:
                if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
                    takePhotoFromCamera();
                }
                return true;
            case R.id.menu_video:
                if (isIntentAvailable(this, MediaStore.ACTION_VIDEO_CAPTURE)) {
                    takeVideo();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 没有指定存储路径
     * 暂不能显示效果
     */
    private void takeVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
    }

    /**
     *
     * @param context
     * @param action 指定的行为
     *通过package manager 寻找与action匹配的Intent
     * @return
     */
    public static boolean isIntentAvailable(Context context,String action) {
        final PackageManager packageManager=context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfos.size()>0;
    }


    /**
     * 拍照获取大图
     */
    File photo=null;
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                 photo = getOutPutMediaFile(this,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(intent,REQUEST_CAPTURE_PICTURE);
            }
        }
    }


    private File createFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = String.format("%s%s%s", PREFIX_PHOTO, timeStamp, "_");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            photo = File.createTempFile(imageFileName, SUFFIX_PHOTO,storageDir);
        }else {
            Toast.makeText(this, "没有SD卡", Toast.LENGTH_SHORT).show();
        }
        return photo;
    }

    /**
     * 在公共路径下添加了以应用名命名的文件夹，存储拍照或者录制视频的存储路径
     * 比createFile多了文件夹
     * 可在程序间公用
     * @param context
     * @param type 1.图片  2.视频
     * @return
     * @throws IOException
     */
    public static File getOutPutMediaFile(Context context,int type) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "没有SD卡", Toast.LENGTH_SHORT).show();
            return null;
        }
        File mediaFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        if (!mediaFileDir.exists()) {
            if (!mediaFileDir.mkdirs()) {
                Toast.makeText(context, "创建目录失败", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName ;
        File mediaFile=null;
        if (1 == type) {
            imageFileName = String.format("%s%s%s", PREFIX_PHOTO, timeStamp, "_");
            mediaFile = File.createTempFile(imageFileName, SUFFIX_PHOTO, mediaFileDir);
        }else if (2 == type) {
            imageFileName = String.format("%s%s%s", PREFIX_VIDEO, timeStamp, "_");
            mediaFile = File.createTempFile(imageFileName, SUFFIX_VIDEO, mediaFileDir);
        }
            return mediaFile;
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAPTURE_PICTURE:
                if (resultCode == RESULT_OK) {
                    displayPhoto();
                    addPictureTOGallery();
                }
                break;
            case REQUEST_CAPTURE_VIDEO:
                //can not broadcast
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    mVideoView.setVideoURI(uri);
                }
                break;
        }

    }

    /**
     * 通知系统扫描图库添加新图片
     */
    private void addPictureTOGallery() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photo);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    private void displayPhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(photo.getAbsolutePath(), options);
        int photoWidth=options.outWidth;
        int photoHeight = options.outHeight;
        int scaleFactor = Math.min(photoHeight / 200, photoWidth / 200);
        options.inJustDecodeBounds=false;
        options.inSampleSize=scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), options);
        mImageView.setImageBitmap(bitmap);
    }
}
