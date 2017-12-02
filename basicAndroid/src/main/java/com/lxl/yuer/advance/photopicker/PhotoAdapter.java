package com.lxl.yuer.advance.photopicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lxl.yuer.advance.R;
import com.lxl.yuer.advance.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{
    private List<Photo> mPhotoList;
    private Context mContext;
    private int width;
    public PhotoAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
        width = ScreenUtils.getScreenWidth(mContext)/3;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.photo);
        }

        public void init(Photo photo) {

        }
    }
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout view = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent, false);
        view.setLayoutParams(new FrameLayout.LayoutParams(width-6,width-6));
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder viewHolder, int position) {
        Photo photo = mPhotoList.get(position);
        viewHolder.init(photo);

    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }
}
