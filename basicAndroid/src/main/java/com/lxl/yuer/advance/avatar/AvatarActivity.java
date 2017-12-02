package com.lxl.yuer.advance.avatar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lxl.yuer.advance.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 从图库和相机获取剪裁的头像
 * note：本地存储权限
 */
public class AvatarActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE =0;
    public static final int REQUEST_IMAGE_GALLERY =1;
    private static final int REQUEST_CROP = 3;
    private ImageView mAvatar;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("crop avatar");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mAvatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activiyt_avatar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_camera:
                //不指定uri将返回缩略图,Intent data 不为空
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //judge any camera app exists to process
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
                return true;
            case R.id.menu_gallery:
                cropSmallAvatar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 从相册剪裁小图
     */
    private void cropSmallAvatar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/jpeg,image/png");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    public  boolean saveBitmap(Bitmap bitmap, File file) {
        if (null==bitmap ||  null==file) {
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (data != null) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    //临时文件,私有文件，图库访问不了
                    mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp.png");
                    if(saveBitmap(bitmap, mFile)){
                        startImageZoom(Uri.fromFile(mFile));
                    }else{
                        Toast.makeText(this, "裁剪失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (data != null) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    mAvatar.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CROP:
                if (data != null) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    mAvatar.setImageBitmap(bitmap);
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFile != null && mFile.exists()) {
            mFile.delete();
        }
    }
}
