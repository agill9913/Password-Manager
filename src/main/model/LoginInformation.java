package model;

import java.security.NoSuchAlgorithmException;

//represents username and password that will be used for login with an object to hash them with
public class LoginInformation {

    private final String usernameHash;
    private final String passwordHash;
    private final Hashing hashGenerator;

    //MODIFIES: this
    //EFFECTS: initialize a LoginInformation with a hashing object and a hash of username, password and checksum
    public LoginInformation(String username, String password) throws NoSuchAlgorithmException {
        hashGenerator = new Hashing();
        usernameHash = hashGenerator.getHash(username);
        passwordHash = hashGenerator.getHash(password);
    }

    //EFFECTS: returns true if given username and password match saved hash, false otherwise
    public boolean checkValues(String username, String password) {
        return usernameHash.equals(hashGenerator.getHash(username))
                && passwordHash.equals(hashGenerator.getHash(password));
    }

}

