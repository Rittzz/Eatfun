package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created on 8/3/15.
 */
public class Location {
    private long id;
    private String address;

    public Location() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
