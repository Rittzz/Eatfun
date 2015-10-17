package rittz.eatclub.api;

import java.util.Arrays;

import rittz.eatclub.api.model.Login;
import rittz.eatclub.api.model.Menu;
import rittz.eatclub.api.model.OrderRequest;
import rittz.eatclub.api.model.OrderResult;
import rittz.eatclub.api.model.User;

public class Main {

    public static void main(final String[] args) {
        System.out.println("Hello World");

        final Eatclub eatclub = EatclubFactory.create();

        System.out.println("Logging in...");
        final User user = eatclub.login(new Login("ian@soundhound.com", "bicf-18"));
        System.out.println("Logged in with " + user.getId());

        orderOrangeAcai(eatclub);

//        System.out.println("Queried with " + eatclub.user().getId());
//
//        for (Menu menu : eatclub.menus()) {
//            System.out.println("For " + menu.getDate() + " we have...");
//            for (Menu.Item item : menu.getItems()) {
//                System.out.println("  " + item.getItem());
//            }
//            System.out.println("");
//        }
    }

    public static void orderOrangeAcai(final Eatclub eatclub) {
        // Get the first menu
        final Menu menu = eatclub.menus().get(0);
        // Look for "Orange "
        Menu.Item orangeItem = null;
        for (Menu.Item item : menu.getItems()) {
            if (item.getItem().startsWith("Butter Chicken")) {
                orangeItem = item;
                break;
            }
        }

        // We need the users location
        final User user = eatclub.user();

        final OrderRequest order = new OrderRequest();
        order.setDeliveryDate(menu.getDate());
        order.setLocation(user.getLocation());

        final OrderRequest.Item orderItem = new OrderRequest.Item();
        orderItem.setId(orangeItem.getId());
        orderItem.setCount(1);

        order.setItems(Arrays.asList(orderItem));

        OrderResult result = eatclub.order(order);

        System.out.println("OrderRequest id is " + result.getId());
    }
}
