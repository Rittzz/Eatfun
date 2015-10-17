package rittz.eatclub.api.model;

/**
 * Created on 8/4/15.
 */
public class Photo {
    private long id;
    private String url;

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
