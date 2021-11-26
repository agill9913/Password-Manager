package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Stream;

//initializes a reader to allow the reading of a json file and transforming its data to an object
//Class is referenced from class demo code taken from:
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads PasswordManager from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PasswordManager read() throws IOException, NoSuchAlgorithmException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return addAccounts(jsonObject);
    }

    //EFFECTS: gets the accounts from JSON file and returns a passwordmanager with the accounts added to it
    private PasswordManager addAccounts(JSONObject obj) throws NoSuchAlgorithmException {
        PasswordManager manager = new PasswordManager();
        JSONArray arr = obj.getJSONArray("accounts");
        for (Object json: arr) {
            JSONObject currUser = (JSONObject) json;
            addUser(manager, (JSONObject) currUser);
        }
        return manager;
    }

    //EFFECTS: returns a new LoginInformation with the values from a json file
    private LoginInformation addLoginInfo(JSONObject info) throws NoSuchAlgorithmException {
        return new LoginInformation(String.valueOf(info.get("username")),
                String.valueOf(info.get("password")), false);
    }

    //EFFECTS: returns a hash map of user data, generated from a json file
    private HashMap<String, String> addData(JSONObject data) {
        HashMap<String, String> siteData = new HashMap<>();
        for (String currData: data.keySet()) {
            siteData.put(currData, data.get(currData).toString());
        }
        return siteData;
    }

    //EFFECTS: returns a hashmap that contains the site name as key and user data hashmap from a json file
    private HashMap<String, HashMap<String, String>> addSite(JSONObject data) {
        HashMap<String, HashMap<String, String>> dataMap = new HashMap<>();
        for (String currData: data.keySet()) {
            dataMap.put(currData, addData((JSONObject) data.get(currData)));
        }
        return dataMap;
    }

    //MODIFIES: manager
    //EFFECTS: creates a UserAccount from a json file and adds it to a passwordManager object
    private void addUser(PasswordManager manager, JSONObject user) throws NoSuchAlgorithmException {
        UserAccount savedUser = new UserAccount(addLoginInfo((JSONObject) user.get("LoginInfo")),
                addSite((JSONObject) user.get("data")));
        manager.addUser(savedUser);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

}
