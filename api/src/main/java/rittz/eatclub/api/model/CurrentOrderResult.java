package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/4/15.
 */
public class CurrentOrderResult {
    private long id;

    @JsonProperty("delivery_date")
    private String deliveryDate;

    @JsonProperty("order_items")
    private List<PendingOrder> items = new ArrayList<>();

    public CurrentOrderResult() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<PendingOrder> getItems() {
        return items;
    }

    public void setItems(List<PendingOrder> items) {
        this.items = items;
    }
}
