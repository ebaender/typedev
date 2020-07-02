package manager;

import java.util.HashMap;

import com.google.gson.JsonObject;

import standard.DBStd;
import translator.HttpEndpoint;
import translator.HttpHost;
import user.User;

public class DBMan {

    private User owner;

    public DBMan(User owner) {
        this.owner = owner;
    }

    private JsonObject post(HashMap<String, String> params) {
        return HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
    }

    private JsonObject request(String requestType) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStd.NAME, owner.getName());
        params.put(DBStd.PASSWORD, owner.getPassword());
        params.put(DBStd.REQUEST, requestType);
        System.out.println(super.getClass() + " " + requestType + " " + params);
        return post(params);
    }

    public JsonObject register() {
        return request(DBStd.REQUEST_REGISTER);
    }

    public JsonObject authenticate() {
        return request(DBStd.REQUEST_AUTHENTICATE);
    }

    public JsonObject spy(String target) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStd.NAME, owner.getName());
        params.put(DBStd.PASSWORD, owner.getPassword());
        params.put(DBStd.USER_TARGET, target);
        params.put(DBStd.REQUEST, DBStd.REQUEST_SPY);
        System.out.println(super.getClass() + " spy " + params);
        return post(params);
    }

    public JsonObject update(boolean played, boolean won, int speed, String language) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStd.NAME, owner.getName());
        params.put(DBStd.PASSWORD, owner.getPassword());
        params.put(DBStd.REQUEST, DBStd.REQUEST_UPDATE);
        params.put(DBStd.UPDATE_GAMES_PLAYED, played ? "1" : "0");
        params.put(DBStd.UPDATE_GAMES_WON, won ? "1" : "0");
        params.put(DBStd.UPDATE_SPEED, Integer.toString(speed));
        params.put(DBStd.UPDATE_LANGUAGE, language);
        System.out.println(super.getClass() + " update " + params);
        return post(params);
    }

    public JsonObject updateLeftSession() {
        return update(true, false, 0, DBStd.NULL_LANGUAGE);
    }

    public JsonObject leaderbord(String leaderbordType) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStd.NAME, owner.getName());
        params.put(DBStd.PASSWORD, owner.getPassword());
        params.put(DBStd.REQUEST, leaderbordType);
        System.out.println(super.getClass() + " " + leaderbordType + " " + params);
        return post(params);
    }

    public JsonObject language(String target) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStd.NAME, owner.getName());
        params.put(DBStd.PASSWORD, owner.getPassword());
        params.put(DBStd.USER_TARGET, target);
        params.put(DBStd.REQUEST, DBStd.REQUEST_LANGUAGE);
        System.out.println(super.getClass() + " language " + params);
        return post(params);
    }

    public JsonObject delete() {
        return request(DBStd.REQUEST_DELETE);
    }

    public JsonObject language() {
        return language(owner.getName());
    }

}