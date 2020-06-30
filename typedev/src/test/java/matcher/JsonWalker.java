package matcher;

import com.google.gson.JsonElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class JsonWalker extends TypeSafeMatcher<JsonElement> {

    private String pathToProperty;
    private String value;
    private String failedAt;

    public JsonWalker(String pathToProperty, String value) {
        this.pathToProperty = pathToProperty;
        this.value = value;
        failedAt = "";
    }

    public static Matcher<JsonElement> hasJsonPropertyAtPath(String pathToProperty, String value) {
        return new JsonWalker(pathToProperty, value);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("has json property \"" + pathToProperty + "\" with value \"" + value + "\". " + failedAt);
    }

    @Override
    protected boolean matchesSafely(JsonElement item) {
        // split path into steps
        String[] pathArray = pathToProperty.split("\\.");
        for (String step : pathArray) {
            int index;
            try {
                // treat natural numbers as array indices
                if ((index = Integer.parseInt(step)) >= 0) {
                    try {
                        item = item.getAsJsonArray().get(index);
                    } catch (IndexOutOfBoundsException e) {
                        failedAt = "Failed at \"" + step + "\", index out of bounds.";
                        return false;
                    }
                } else {
                    failedAt = "Failed at \"" + step + "\", index is negative";
                    return false;
                }
            } catch (NumberFormatException e) {
                // if it's not a natural number, it must be an object property
                item = item.getAsJsonObject().get(step);
            }
            // check if the next step can be taken
            if (item == null) {
                failedAt = "Failed at \"" + step + "\", no such element.";
                return false;
            }
        }
        // when there are no more steps left, we must have arrived at the final property
        if (item.getAsString().equals(value)) {
            return true;
        } else {
            failedAt = "Failed at final evaluation, does not match " + value + ".";
            return false;
        }
    }

}