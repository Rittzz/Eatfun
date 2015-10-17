package rittz.eatclub.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Constructs eatclub api instances.  Don't call this every time!
 */
public class EatclubFactory {

    private static final String ENDPOINT = "https://myeatclub.com";

    public static Eatclub create() {
        final Eatclub eatclub = new Eatclub();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new JacksonConverter(objectMapper))
                .setRequestInterceptor(eatclub.getRequestInterceptor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        eatclub.setDelegate(restAdapter.create(EatclubRawApi.class));

        return eatclub;
    }
}
