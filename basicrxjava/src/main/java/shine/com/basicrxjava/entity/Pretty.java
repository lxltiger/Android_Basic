package shine.com.basicrxjava.entity;

/**
 * Created by Administrator on 2016/7/25.
 */
public class Pretty {
   public String url;
    public String description;

    @Override
    public String toString() {
        return "Pretty{" +
                "url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
