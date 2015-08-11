package rittz.eatfun.core;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;
import rittz.eatclub.api.Eatclub;
import rittz.eatclub.api.model.CurrentOrderResult;
import rittz.eatclub.api.model.CurrentOrders;
import rittz.eatclub.api.model.Menu;
import rittz.eatclub.api.model.OrderRequest;
import rittz.eatclub.api.model.OrderResult;
import rittz.eatclub.api.model.PendingOrder;
import rittz.eatclub.api.model.User;
import rittz.eatfun.storage.Config;

/**
 * The general logic of ordering stuff from eatclub.
 */
public class OrderTask {

    private final Eatclub eatclub = EatclubInstance.get();

    private final List<String> preferredItemNames;
    private final boolean shuffle;

    public OrderTask() {
        this.preferredItemNames = Config.get().getPreferredItems();
        this.shuffle = Config.get().isShuffleMode();

        if (preferredItemNames.size() == 0) {
            throw new IllegalArgumentException("Preferred item names must be at least of size 1");
        }
    }

    @WorkerThread
    public OrderTaskResult doWork() {
        final OrderTaskResult result = new OrderTaskResult();

        try {
            final User user = eatclub.user();
            final List<Menu> menuList = eatclub.menus();
            final CurrentOrders currentOrders = eatclub.currentOrders();

            // Store the delivery dates so we don't order from menus we already have orders for
            final List<Long> orderedMenuIds = new ArrayList<>();
            for (CurrentOrderResult currentOrderResult : currentOrders.getOrders()) {
                for (PendingOrder pendingOrder : currentOrderResult.getItems()) {
                    orderedMenuIds.add(pendingOrder.getMenu());
                }
            }

            // Finally order the food for menus we haven't ordered from before
            for (Menu menu : menuList) {
                if (!orderedMenuIds.contains(menu.getId())) {
                    result.getResults().add(orderFromMenu(user, menu));
                }
            }
        }
        catch (RetrofitError ex) {
            result.setException(ex);

            if (ex.getKind() == RetrofitError.Kind.NETWORK) {
                result.setErrorType(OrderTaskResult.ErrorType.NETWORK);
            }
            if (ex.getKind() == RetrofitError.Kind.HTTP) {
                result.setErrorType(OrderTaskResult.ErrorType.AUTH_ERROR);
            }
            else {
                result.setErrorType(OrderTaskResult.ErrorType.UNKNOWN);
            }
        }

        return result;
    }

    private OrderStatus orderFromMenu(@NonNull final User user, @NonNull final Menu menu) {
        try {
            final Menu.Item menuItem = pickItemFromMenu(menu);

            if (menuItem != null) {
                orderMenuItem(user, menu, menuItem);
                return OrderStatus.success();
            }
            else {
                return OrderStatus.failure(new IllegalStateException("Nothing from the menu was available to order"), OrderStatus.ErrorType.NONE_AVAILABLE);
            }
        }
        catch (final RetrofitError ex) {
            if (ex.getKind() == RetrofitError.Kind.NETWORK) {
                return OrderStatus.failure(ex, OrderStatus.ErrorType.NETWORK);
            }
            if (ex.getKind() == RetrofitError.Kind.HTTP) {
                return OrderStatus.failure(ex, OrderStatus.ErrorType.AUTH_ERROR);
            }
            else {
                return OrderStatus.failure(ex, OrderStatus.ErrorType.UNKNOWN);
            }
        }
    }

    /**
     * @throws RetrofitError you must catch this!
     */
    private OrderResult orderMenuItem(@NonNull final User user, @NonNull final Menu menu, @NonNull final Menu.Item menuItem) throws RetrofitError {
        final OrderRequest order = new OrderRequest();
        order.setDeliveryDate(menu.getDate());
        order.setLocation(user.getLocation());

        final OrderRequest.Item orderItem = new OrderRequest.Item();
        orderItem.setId(menuItem.getId());
        orderItem.setCount(1);

        order.setItems(Arrays.asList(orderItem));

        return eatclub.order(order);
    }

    // TODO Algorithm based on interface?

    // Stateful
    private final List<String> itemNamePool = new ArrayList<>();

    private Menu.Item pickItemFromMenu(@NonNull final Menu menu) {
        // Refresh the item name pool to pick from if empty
        itemNamePool.clear();
        itemNamePool.addAll(preferredItemNames);

        if (shuffle) {
            Collections.shuffle(itemNamePool);
        }

        // Pick the item!
        for (int i = 0; i < itemNamePool.size(); i++) {
            for (Menu.Item item : menu.getItems()) {
                final String itemName = item.getItem();
                final String preferredItemName = itemNamePool.get(i);
                if (itemName.contains(preferredItemName)) {
                    itemNamePool.remove(i);
                    return item;
                }
            }
        }

        // Uh oh, nothing matched the preferred items, pick one at random!
        final List<Menu.Item> shuffledMenuItems = new ArrayList<>(menu.getItems());
        Collections.shuffle(shuffledMenuItems);

        for (Menu.Item item : menu.getItems()) {
            if (isMenuItemAvailable(item)) {
                return item;
            }
        }

        // There are no items available, oh well
        return null;
    }

    private boolean isMenuItemAvailable(final Menu.Item item) {
        return item.getInventory().getRemaining() > 0;
    }
}
