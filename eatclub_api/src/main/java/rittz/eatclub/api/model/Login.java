package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created on 8/3/15.
 */
public class Login {

    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public Login() {
        // Empty
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
