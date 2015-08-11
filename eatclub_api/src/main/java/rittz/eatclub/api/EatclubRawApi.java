package rittz.eatclub.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import rittz.eatclub.api.model.CurrentOrders;
import rittz.eatclub.api.model.Login;
import rittz.eatclub.api.model.Menu;
import rittz.eatclub.api.model.OrderRequest;
import rittz.eatclub.api.model.OrderResult;
import rittz.eatclub.api.model.User;

/**
 * Due to authentication reasons, don't expose this directly.
 */
public interface EatclubRawApi {

    @PUT("/api/v3/log_in/")
    User login(@Body Login login);

    @GET("/api/v3/user/")
    User user();

    @GET("/api/v3/menus/")
    List<Menu> menus();

    @GET("/api/v3/orders/")
    CurrentOrders currentOrders();

    @POST("/api/v3/place_fully_subsidized_order/")
    OrderResult order(@Body OrderRequest order);
}
