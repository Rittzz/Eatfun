package rittz.eatclub.api;

import java.util.List;

import retrofit.RequestInterceptor;
import rittz.eatclub.api.model.CurrentOrders;
import rittz.eatclub.api.model.Login;
import rittz.eatclub.api.model.Menu;
import rittz.eatclub.api.model.OrderRequest;
import rittz.eatclub.api.model.OrderResult;
import rittz.eatclub.api.model.User;

/**
 * Created on 8/3/15.
 */
public class Eatclub {

    private EatclubRawApi delegate;
    private String authToken;

    private RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            if (authToken != null) {
                request.addHeader("Authorization", "Token " + authToken);
            }
        }
    };

    // Only let our factory construct us.
    Eatclub() {
        // Empty
    }

    // Package methods

    void setDelegate(EatclubRawApi delegate) {
        this.delegate = delegate;
    }

    RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    // Public methods

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User login(Login login) {
        final User user = delegate.login(login);
        setAuthToken(user.getAuthToken());
        return user;
    }

    public User user() {
        return delegate.user();
    }

    public List<Menu> menus() {
        return delegate.menus();
    }

    public CurrentOrders currentOrders() {
        return delegate.currentOrders();
    }

    public OrderResult order(OrderRequest order) {
        return delegate.order(order);
    }
}
