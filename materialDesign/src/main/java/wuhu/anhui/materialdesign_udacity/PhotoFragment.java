package wuhu.anhui.materialdesign_udacity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *  图片列表的动画
 *
 */
public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";
    //使用百度上明星照片
    private String path;
    private RecyclerView mRecyclerView;
    private FragmentActivity mFragmentActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentActivity=getActivity();
        path= Uri.parse("http://image.baidu.com/channel/listjson")
                .buildUpon()
                .appendQueryParameter("pn","1")
                .appendQueryParameter("rn","30")
                .appendQueryParameter("tag1","明星")
                .appendQueryParameter("tag2","全部")
                .appendQueryParameter("ftags","女明星")
                .appendQueryParameter("ie","utf8").toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 6) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        return 1;
                    case 3:
                        return 2;
                    default:
                        return 3;
                }
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridDecoration(4));
        mRecyclerView.setHasFixedSize(true);
        new PhotoAsyncTask().execute(path);
    }


    private static class GridDecoration extends RecyclerView.ItemDecoration{
        private int space;

        public GridDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.top=space;
            outRect.bottom=space;
        }
    }


    class PhotoAsyncTask extends AsyncTask<String, Void, List<Photo>> {
        @Override

        protected List<Photo>  doInBackground(String... params) {
            List<Photo> photoList=null;
            try {
                String result = getUrlString(params[0]);
                JSONObject json = new JSONObject(result);
                Type type=new TypeToken<List<Photo>>(){}.getType();
                Gson gson=new Gson();
                photoList = gson.fromJson(json.getString("data"), type);

            } catch (IOException e) {
                Log.d(TAG, "doInBackground() returned: " + "io exception");
            } catch (JSONException e) {
                Log.d(TAG, "doInBackground() returned: " + "json exception");
                e.printStackTrace();
            }
            return photoList;
        }

        @Override
        protected void onPostExecute(List<Photo> result) {
            if (result != null) {
                mRecyclerView.setAdapter(new PhotoAdapter(result));

            }
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<ImageHolder>{
            private List<Photo> mPhotos;

        public PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_photo, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            holder.bind(mPhotos.get(position));
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }


    private  class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ForegroundImageView mImageView;
        private Photo mPhoto;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageView = (ForegroundImageView) itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(this);
        }

        public void bind(Photo photo) {
            mPhoto = photo;
            Glide.with(itemView.getContext()).load(photo.getThumbnail_url()).into(mImageView);

        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mFragmentActivity, PhotoDetailActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mPhoto.getImage_url()));
            mFragmentActivity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(mFragmentActivity, mImageView,mImageView.getTransitionName()).toBundle());
        }
    }



    //根据路径获取json数据
    public byte[] getUrlByte(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ":" + path);
            }
            int bufferRead;
            byte[] buffer = new byte[1024];
            while ((bufferRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bufferRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String path) throws IOException {
        return new String(getUrlByte(path));
    }
}
