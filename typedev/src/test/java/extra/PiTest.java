package extra;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;

import com.google.gson.JsonObject;

import org.junit.Test;

public class PiTest {

    @Test
    public void testAllPiEndpoints() throws Exception {
        JsonObject response = null;

        // go through all known endpoints and make sure a valid request returns a response
        // with code 200
        for (HttpEndpoint endpoint : HttpEndpoint.values()) {
            URL piWithEndpoint = new URL(HttpsHost.PI + endpoint.getSubdomain());

            // if there is no parameter specified, assume this is a get endpoint
            if (endpoint.getParamExample() == null) {
                response = HttpMan.get(piWithEndpoint);
            } else {
                response = HttpMan.post(piWithEndpoint, endpoint.getParamExample());
            }
            assertThat("http ok", response, hasJsonProperty("cod", "200"));
        }
    }

}