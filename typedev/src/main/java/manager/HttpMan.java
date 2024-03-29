package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import translator.HttpHost;
import translator.HttpEndpoint;

public class HttpMan {

    private static Exception latestException = null;

    public static JsonObject get(URL host) {
        try {
            InputStream input = host.openStream();
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            latestException = e;
            return null;
        }
    }

    public static JsonObject post(URL host, String params) {
        try {
            HttpURLConnection connection = (HttpURLConnection) host.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();
            // TODO: implement timeouts here at some point so the method wont hang waiting
            // for a response that is not coming
            String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines()
                    .collect(Collectors.joining());
            return JsonParser.parseString(response).getAsJsonObject();
        } catch (IOException e) {
            latestException = e;
            return null;
        }
    }

    @Deprecated
    public static JsonObject post(URL host, HashMap<String, String> params) {
        StringBuilder paramsBuilder = new StringBuilder();
        params.entrySet().forEach(entry -> {
            paramsBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
        });
        return post(host, paramsBuilder.toString());
    }

    public static JsonObject post(HttpHost host, HttpEndpoint endpoint, HashMap<String, String> params) {
        try {
            return post(new URL(host.toString() + endpoint.toString()), params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Exception getLatestException() {
        return latestException;
    }

}