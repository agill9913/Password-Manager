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

    public void addUser(String username, String password) {
        credentials.add(new LoginInformation(username, password));
    }

    public boolean checkLogin(String name, String password) { //todo - redo to send error codes
        for (LoginInformation currInfo: credentials) {
            if (currInfo.checkValues(name, password) != -1) {
                return true;
            }
        }
        return false;
    }

    public int dataSize(String identifier) {
        return information.get(identifier).length;
    }

    public void editData(String id, int dataId, String newValue) {
        information.get(id)[dataId] = newValue;
    }

    public boolean removeInfo(String identifier) {
        return information.remove(identifier) == null;
    }

    public String displayIDs() {
        StringBuilder retVal = new StringBuilder();
        for (String id: information.keySet()) {
            retVal.append(id);
        }
        return retVal.toString();
    }

    public String toString(String id) {
        StringBuilder retVal = new StringBuilder("ID: " + id + '\n');
        for (int i = 0; i < information.get(id).length; i++) {
            retVal.append(information.get(id)[i]).append(", ");
        }
        return retVal.toString();
    }

    public String displayAllInfo() { //https://www.w3schools.com/java/java_hashmap.asp
        StringBuilder retVal = new StringBuilder();
        for (String id: information.keySet()) {
            retVal.append(toString(id));
        }
        return retVal.toString();
    }
}
