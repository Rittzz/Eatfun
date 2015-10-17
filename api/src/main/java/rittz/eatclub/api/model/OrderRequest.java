package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/4/15.
 */
public class OrderRequest {
    private Location location;
    @JsonProperty("delivery_date")
    private String deliveryDate;
    private List<Item> items = new ArrayList<>();

    public OrderRequest() {
        // Empty
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private long id;
        private int count;

        public Item() {}

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
