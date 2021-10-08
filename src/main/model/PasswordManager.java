package model;

import java.util.ArrayList;
import java.util.HashMap;

public class PasswordManager {
    ArrayList<LoginInformation> credentials;
    HashMap<String, String[]> information;

    public PasswordManager() {
        credentials = new ArrayList<>();
        information = new HashMap<>();
    }

    public void addInfo(String identifier, String[] data) {
        information.put(identifier, data);
    }

    public boolean checkLogin(String name, String password) {
        for (LoginInformation currInfo: credentials) {
            if (currInfo.checkValues(name, password) != -1) {
                return true;
            }
        }
        return false;
    }

    public boolean removeInfo(String identifier) {
        return information.remove(identifier) == null;
    }

    public String toString(String id) {
        String retVal = "ID: " + id + '\n';
        for (int i = 0; i < information.get(id).length; i++) {
            retVal += information.get(id)[i] + ", ";
        }
        return retVal;
    }

    public String displayAllInfo() { //https://www.w3schools.com/java/java_hashmap.asp
        String retVal = "";
        for (String id: information.keySet()) {
            retVal += toString(id);
        }
        return retVal;
    }
}
