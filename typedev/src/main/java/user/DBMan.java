package user;

import java.util.HashMap;

import com.google.gson.JsonObject;

import extra.DBStandard;
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
        params.put(DBStandard.NAME, owner.getName());
        params.put(DBStandard.PASSWORD, owner.getPassword());
        params.put(DBStandard.REQUEST, requestType);
        System.out.println(super.getClass() + " " + requestType + " " + params);
        return post(params);
    }

    public JsonObject register() {
        return request(DBStandard.REQUEST_REGISTER);
    }

    public JsonObject authenticate() {
        return request(DBStandard.REQUEST_AUTHENTICATE);
    }

    public JsonObject spy(String target) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStandard.NAME, owner.getName());
        params.put(DBStandard.PASSWORD, owner.getPassword());
        params.put(DBStandard.SPY_TARGET, target);
        params.put(DBStandard.REQUEST, DBStandard.REQUEST_SPY);
        System.out.println(super.getClass() + " spy " + params);
        return post(params);
    }

    public JsonObject update(boolean won, int speed) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DBStandard.NAME, owner.getName());
        params.put(DBStandard.PASSWORD, owner.getPassword());
        params.put(DBStandard.REQUEST, DBStandard.REQUEST_UPDATE);
        params.put(DBStandard.UPDATE_GAMES_PLAYED, "1");
        params.put(DBStandard.UPDATE_GAMES_WON, won ? "1" : "0");
        params.put(DBStandard.UPDATE_SPEED, Integer.toString(speed));
        System.out.println(super.getClass() + " update " + params);
        return post(params);
    }

}