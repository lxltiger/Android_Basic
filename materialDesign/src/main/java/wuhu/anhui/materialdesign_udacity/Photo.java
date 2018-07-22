package wuhu.anhui.materialdesign_udacity;

/**
 * Created by lixiaolin on 15/11/7.
 */


public class Photo {
    private String id;
    private String image_url;
    private String download_url;
    private String thumbnail_url;
    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}



