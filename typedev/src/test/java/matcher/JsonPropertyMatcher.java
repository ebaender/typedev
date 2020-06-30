package matcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class JsonPropertyMatcher extends TypeSafeMatcher<JsonElement> {

    private String property;
    private String value;

    public JsonPropertyMatcher(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public static Matcher<JsonElement> hasJsonProperty(String property, String value) {
        return new JsonPropertyMatcher(property, value);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has json property \"" + property + "\" with value \"" + value + "\"");

    }

    @Override
    protected boolean matchesSafely(JsonElement item) {
        JsonElement itemValue = null;
        boolean matches = false;
        try {
            // assume the element is an object.
            itemValue = item.getAsJsonObject().get(property);
            matches = itemValue != null && itemValue.getAsString().equals(value);
        } catch (IllegalStateException e) {
            // if it's not an object, it could be an array.
            try {
                JsonArray parentArray = item.getAsJsonArray();
                for (JsonElement child : parentArray) {
                    itemValue = child.getAsJsonObject().get(property);
                    if (itemValue != null && itemValue.getAsString().equals(value)) {
                        matches = true;
                        break;
                    }
                }
            } catch (IllegalStateException f) {
                // if it's not an array either, noone cares. sorry.
                matches = false;
            }
        }
        return matches;
    }

}