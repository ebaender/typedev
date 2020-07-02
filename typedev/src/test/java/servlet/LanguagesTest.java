package servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Set;

import com.google.gson.JsonElement;

import org.junit.Test;

import manager.ResourceMan;
import standard.JsonStd;

public class LanguagesTest extends CommandTest {

    @Override
    public String getBaseCommand() {
        return "languages";
    }

    @Test
    public void languages() throws Exception {
        final JsonElement LANGUAGES_RESP = executeCommand(getValidKey(), getBaseCommand()).get(JsonStd.MSG);
        assertThat(LANGUAGES_RESP, notNullValue());
        final String LANGUAGES_LIST = LANGUAGES_RESP.getAsString();
        final Set<String> EXPECTED_LANGUAGES = ResourceMan.getLanguages();
        assertThat(EXPECTED_LANGUAGES, notNullValue());
        assertThat(LANGUAGES_LIST.toLowerCase(), stringContainsInOrder(EXPECTED_LANGUAGES));
    }

}