package rittz.eatfun.core;

import android.app.Application;

import rittz.eatfun.storage.Config;

/**
 * Created on 8/5/15.
 */
public class EatfunApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.createInstance(this);
    }
}
