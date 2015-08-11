package rittz.eatfun.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import rittz.eatfun.core.EatclubInstance;

/**
 * Global configuration values go here.
 */
public class Config {

    private static final String TAG = "Config";

    private static final String PREFS_NAME = "eatfun";

    private static final String KEY_AUTO_ORDER = "auto_order";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_PREFERRED_ITEMS = "preferred_items";
    private static final String KEY_SHUFFLE_MODE = "shuffle_mode";
    private static final String KEY_LAST_SYNC = "last_sync";

    private static Config instance;

    private final Context context;
    private final SharedPreferences prefs;

    public static void createInstance(final Context context) {
        instance = new Config(context);
    }

    public static Config get() {
        return instance;
    }

    private Config(Context context) {
        this.context = context.getApplicationContext();

        prefs = this.context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void setAutoOrder(final boolean enabled) {
        prefs.edit().putBoolean(KEY_AUTO_ORDER, enabled).apply();;
    }

    public boolean isAutoOrder() {
        return prefs.getBoolean(KEY_AUTO_ORDER, false);
    }

    public void setAuthToken(final String authToken) {
        prefs.edit().putString(KEY_AUTH_TOKEN, authToken).apply();
        EatclubInstance.get().setAuthToken(authToken);
    }

    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    // User JSON to store of the string lists as shared prefs can't do it natively

    public List<String> getPreferredItems() {
        try {
            final String json = prefs.getString(KEY_PREFERRED_ITEMS, "[]");
            final JSONArray jsonArray = new JSONArray(json);
            final List<String> values = new ArrayList<>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                values.add(jsonArray.getString(i));
            }

            return values;
        }
        catch (JSONException ex) {
            Log.wtf(TAG, "Corrupted shared prefs value, clearing");
            prefs.edit().remove(KEY_PREFERRED_ITEMS).apply();

            return new ArrayList<>(0);
        }
    }

    public void setPreferredItems(final List<String> preferredItems) {
        final JSONArray jsonArray = new JSONArray();
        for (String item : preferredItems) {
            jsonArray.put(item);
        }

        prefs.edit().putString(KEY_PREFERRED_ITEMS, jsonArray.toString()).apply();
    }

    public void setShuffleMode(final boolean enabled) {
        prefs.edit().putBoolean(KEY_SHUFFLE_MODE, enabled).apply();;
    }

    public boolean isShuffleMode() {
        return prefs.getBoolean(KEY_SHUFFLE_MODE, false);
    }

    public void setLastSync(final long timestamp) {
        prefs.edit().putLong(KEY_LAST_SYNC, timestamp).apply();;
    }

    public long getLastSync() {
        return prefs.getLong(KEY_LAST_SYNC, 0);
    }
}
