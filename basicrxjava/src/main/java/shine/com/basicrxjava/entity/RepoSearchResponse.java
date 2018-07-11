package shine.com.basicrxjava.entity;

public class RepoSearchResponse {

    /**
     * total_count : 11077
     * incomplete_results : false
     */

    private int total_count;
//    private List<Repo> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    /*public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }
*/

}
