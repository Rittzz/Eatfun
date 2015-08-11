package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created on 8/3/15.
 */
public class User {
    @JsonProperty("id")
    private long id;
    @JsonProperty("auth_token")
    private String authToken;
    @JsonProperty("selected_location")
    private Location location;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
