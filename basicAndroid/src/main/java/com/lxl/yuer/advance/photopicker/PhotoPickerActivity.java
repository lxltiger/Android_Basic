package com.lxl.yuer.advance.photopicker;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lxl.yuer.advance.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;

public class PhotoPickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Button mButton;
    List<String> photoDirectoryName = new ArrayList<>();
    private PhotoDirectoryAdapter mPhotoDirectoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mButton = (Button) findViewById(R.id.btn_toggle);
        mButton.setOnClickListener(this);
        //初始化弹出框，显示不同文件夹
        initListPopupWindow();
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoaderManager().initLoader(0, null,this);
    }

    private void initListPopupWindow() {
        ListPopupWindow popupWindow = new ListPopupWindow(this);
        popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        popupWindow.setAnchorView(mButton);
        //setModal(true) popup will receive all touch and key input.
        // If the user touches outside the popup window's content area the popup window will be dismissed.
        popupWindow.setModal(true);
        popupWindow.setDropDownGravity(Gravity.BOTTOM);
        mPhotoDirectoryAdapter = new PhotoDirectoryAdapter(photoDirectoryName, this);
        popupWindow.setAdapter(mPhotoDirectoryAdapter);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new PhotoLoader(this, false);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        //图片文件夹集合
        List<PhotoDirectory> directories = new ArrayList<>();
        //图片文件夹名称集合

        //含所有文件的文件夹
        PhotoDirectory photoDirectoryAll=new PhotoDirectory();
        photoDirectoryAll.setId("all");
        photoDirectoryAll.setName("所有图片");
        //先把所有图片的文件夹添加到集合
        directories.add(photoDirectoryAll);
        //同样添加其名称
        photoDirectoryName.add(photoDirectoryAll.getName());

        while (data.moveToNext()) {
            int imageId  = data.getInt(data.getColumnIndexOrThrow(_ID));
            String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            //每张图片都添加到所有图片文件夹
            photoDirectoryAll.addPhoto(new Photo(imageId, path));
            //-----------以下按文件夹分类放置对应的图片---------------
            //如果图片的文件夹已经存在
            if (photoDirectoryName.contains(name)) {
                //根据文件名找到相应的集合
                directories.get(photoDirectoryName.indexOf(name)).addPhoto(new Photo(imageId, path));
            }else {
                //创建新的文件夹
                PhotoDirectory photoDirectory = new PhotoDirectory();
                //设置名称
                photoDirectory.setName(name);
                photoDirectory.setId(bucketId);
                //第一张图片设为封面
                photoDirectory.setCoverPath(path);
                //添加图片
                photoDirectory.addPhoto(new Photo(imageId, path));
                //添加新的文件夹到集合中
                directories.add(photoDirectory);
                //添加名称到集合中，此名称与directories文件夹一一对应
                photoDirectoryName.add(name);
            }
        }

        List<Photo> mphotos = photoDirectoryAll.getPhotos();
        if (mphotos.size() > 0) {
            PhotoAdapter adapter = new PhotoAdapter(mphotos, this);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

    }
}
