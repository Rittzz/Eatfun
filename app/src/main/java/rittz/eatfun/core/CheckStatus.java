package rittz.eatfun.core;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import rittz.eatclub.api.Eatclub;
import rittz.eatclub.api.model.CurrentOrderResult;
import rittz.eatclub.api.model.CurrentOrders;
import rittz.eatclub.api.model.Menu;
import rittz.eatclub.api.model.PendingOrder;
import rittz.eatfun.storage.Config;

/**
 * Created on 8/5/15.
 */
public class CheckStatus extends AsyncTask<Void, Void, String> {

    private Listener listener;

    public CheckStatus() {
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onComplete(String status);
    }

    @Override
    protected String doInBackground(Void... params) {
        // Are we logged in?
        if (Config.get().getAuthToken() == null) {
            return "Please login to your eatclub account";
        }

        // Does the server like us?
        final String validCredMessage = validCredentialsCheck();
        if (validCredMessage != null) {
            return validCredMessage;
        }

        // Have we ordered everything yet?
        final String ordersCheck = ordersLeft();
        if (ordersCheck != null) {
            return ordersCheck;
        }

        return "Everything looks good";
    }

    private String validCredentialsCheck() {
        try {
            EatclubInstance.get().user();
            return null;
        }
        catch (RetrofitError ex) {
            if (ex.getKind() == RetrofitError.Kind.NETWORK) {
                return "No connectivity";
            }
            else if (ex.getKind() == RetrofitError.Kind.HTTP) {
                return "There is an error with your credentials, please login again";
            }

            throw ex;
        }
    }

    private String ordersLeft() {
        try {
            final Eatclub eatclub = EatclubInstance.get();

            final List<Menu> menuList = eatclub.menus();
            final CurrentOrders currentOrders = eatclub.currentOrders();

            final List<Long> orderedMenuIds = new ArrayList<>();
            for (CurrentOrderResult currentOrderResult : currentOrders.getOrders()) {
                for (PendingOrder pendingOrder : currentOrderResult.getItems()) {
                    orderedMenuIds.add(pendingOrder.getMenu());
                }
            }

            int ordersLeftToDo = 0;
            for (Menu menu : menuList) {
                if (!orderedMenuIds.contains(menu.getId())) {
                    ordersLeftToDo++;
                }
            }

            if (ordersLeftToDo > 0) {
                return ordersLeftToDo + " pending order(s)";
            }

            return null;
        }
        catch (RetrofitError ex) {
            if (ex.getKind() == RetrofitError.Kind.NETWORK) {
                return "No connectivity";
            }
            else if (ex.getKind() == RetrofitError.Kind.HTTP) {
                return "There is an error with your credentials, please login again";
            }

            throw ex;
        }
    }


    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);
        if (listener != null) {
            listener.onComplete(status);
        }

        listener = null;
    }

}
