package rittz.eatfun.core;

import rittz.eatclub.api.Eatclub;
import rittz.eatclub.api.EatclubFactory;
import rittz.eatfun.storage.Config;

/**
 * Central point for getting the eatclub API.
 */
public class EatclubInstance {

    public static Eatclub eatclub;

    public static Eatclub get() {
        if (eatclub == null) {
            eatclub = EatclubFactory.create();
        }

        eatclub.setAuthToken(Config.get().getAuthToken());

        return eatclub;
    }
}
