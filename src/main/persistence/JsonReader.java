package persistence;

import model.AES;
import model.LoginInformation;
import model.PasswordManager;
import model.UserAccount;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Stream;

public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PasswordManager read() throws IOException, NoSuchAlgorithmException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return addAccounts(jsonObject);
    }

    private PasswordManager addAccounts(JSONObject obj) throws NoSuchAlgorithmException {
        PasswordManager manager = new PasswordManager();
        JSONArray arr = obj.getJSONArray("accounts");
        for (Object json: arr) {
            JSONObject currUser = (JSONObject) json;
            addUser(manager, (JSONObject) currUser);
        }
        return manager;
    }

    private LoginInformation addLoginInfo(JSONObject info) throws NoSuchAlgorithmException {
        return new LoginInformation(String.valueOf(info.get("username")), String.valueOf(info.get("password")),
                info.get("hashing").toString().getBytes(StandardCharsets.UTF_8));
    }

    private HashMap<String, String> addData(JSONObject data) {
        HashMap<String, String> siteData = new HashMap<>();
        for (String currData: data.keySet()) {
            siteData.put(currData, data.get(currData).toString());
        }
        return siteData;
    }

    private HashMap<String, HashMap<String, String>> addSite(JSONObject data) {
        HashMap<String, HashMap<String, String>> dataMap = new HashMap<>();
        for (String currData: data.keySet()) {
            dataMap.put(currData, addData((JSONObject) data.get(currData)));
        }
        return dataMap;
    }

    private AES addAES(JSONObject data) throws NoSuchAlgorithmException {
        System.out.println("AES got: " + data.get("key"));
        return new AES(data.get("key").toString());
    }

    private void addUser(PasswordManager manager, JSONObject user) throws NoSuchAlgorithmException {
        UserAccount savedUser = new UserAccount(addLoginInfo((JSONObject) user.get("LoginInfo")),
                addSite((JSONObject) user.get("data")), addAES((JSONObject) user.get("AES")));
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
