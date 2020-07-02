package user;

import java.util.HashMap;

import com.google.gson.JsonObject;

import extra.DBStan;
import extra.HttpEndpoint;
import extra.HttpHost;
import extra.HttpMan;

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
        params.put(DBStan.NAME, owner.getName());
        params.put(DBStan.PASSWORD, owner.getPassword());
        params.put(DBStan.REQUEST, requestType);
        System.out.println(super.getClass() + " " + requestType + " " + params);
        return post(params);
    }

    public JsonObject register() {
        return request(DBStan.REQUEST_REGISTER);
    }

    public JsonObject authenticate() {
        return request(DBStan.REQUEST_AUTHENTICATE);
    }

    public JsonObject spy(String target) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStan.NAME, owner.getName());
        params.put(DBStan.PASSWORD, owner.getPassword());
        params.put(DBStan.USER_TARGET, target);
        params.put(DBStan.REQUEST, DBStan.REQUEST_SPY);
        System.out.println(super.getClass() + " spy " + params);
        return post(params);
    }

    public JsonObject update(boolean played, boolean won, int speed, String language) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStan.NAME, owner.getName());
        params.put(DBStan.PASSWORD, owner.getPassword());
        params.put(DBStan.REQUEST, DBStan.REQUEST_UPDATE);
        params.put(DBStan.UPDATE_GAMES_PLAYED, played ? "1" : "0");
        params.put(DBStan.UPDATE_GAMES_WON, won ? "1" : "0");
        params.put(DBStan.UPDATE_SPEED, Integer.toString(speed));
        params.put(DBStan.UPDATE_LANGUAGE, language);
        System.out.println(super.getClass() + " update " + params);
        return post(params);
    }

    public JsonObject updateLeftSession() {
        return update(true, false, 0, DBStan.NULL_LANGUAGE);
    }

    public JsonObject leaderbord(String leaderbordType) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStan.NAME, owner.getName());
        params.put(DBStan.PASSWORD, owner.getPassword());
        params.put(DBStan.REQUEST, leaderbordType);
        System.out.println(super.getClass() + " " + leaderbordType + " " + params);
        return post(params);
    }

    public JsonObject language(String target) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStan.NAME, owner.getName());
        params.put(DBStan.PASSWORD, owner.getPassword());
        params.put(DBStan.USER_TARGET, target);
        params.put(DBStan.REQUEST, DBStan.REQUEST_LANGUAGE);
        System.out.println(super.getClass() + " language " + params);
        return post(params);
    }

    public JsonObject language() {
        return language(owner.getName());
    }

}