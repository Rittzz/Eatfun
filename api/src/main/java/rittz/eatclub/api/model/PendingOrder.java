package rittz.eatclub.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/4/15.
 */
public class PendingOrder
{
    private long id;

    private long menu;

    private int count;

    @JsonProperty("order_items")
    private List<Menu.Item> items = new ArrayList<>();

    public PendingOrder() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMenu() {
        return menu;
    }

    public void setMenu(long menu) {
        this.menu = menu;
    }

    public List<Menu.Item> getItems() {
        return items;
    }

    public void setItems(List<Menu.Item> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
