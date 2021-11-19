package model;

import org.json.JSONObject;
import persistence.Writable;

import java.security.NoSuchAlgorithmException;

//represents username and password that will be used for login with an object to hash them with
public class LoginInformation implements Writable {

    private final String usernameHash;
    private final String passwordHash;
    private final Hashing hashGenerator;

    //MODIFIES: this
    //EFFECTS: initialize a LoginInformation with a hashing object and a hash of username and password,
    //for use with saved JSON data
    //Note: isHash is used to provide a second constructor of (String, String), there's no use for it
    public LoginInformation(String username, String password, boolean isNew) throws NoSuchAlgorithmException {
        hashGenerator = new Hashing();
        if (isNew) {
            usernameHash = hashGenerator.getHash(username);
            passwordHash = hashGenerator.getHash(password);
        } else {
            usernameHash = username;
            passwordHash = password;
        }
    }

    //EFFECTS: returns true if given username and password match saved hash, false otherwise
    public boolean checkValues(String username, String password) {
        return usernameHash.equals(hashGenerator.getHash(username))
                && passwordHash.equals(hashGenerator.getHash(password));
    }

    //EFFECTS: returns a hash of the username and password
    public String getHash() {
        return hashGenerator.getHash(usernameHash + passwordHash);
    }

    //EFFECTS: Returns a string representation of username and password hash
    @Override
    public String toString() {
        return "Username Hash = " + usernameHash + ", Password Hash = " + passwordHash + '\n';
    }

    //EFFECTS: returns a json representation of username and password of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("username", usernameHash);
        obj.put("password", passwordHash);
        return obj;
    }
}

