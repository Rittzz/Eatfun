package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/4/15.
 */
public class CurrentOrders {

    @JsonProperty("results")
    private List<CurrentOrderResult> orders = new ArrayList<>();

    public CurrentOrders() {}

    public List<CurrentOrderResult> getOrders() {
        return orders;
    }

    public void setOrders(List<CurrentOrderResult> orders) {
        this.orders = orders;
    }
}
