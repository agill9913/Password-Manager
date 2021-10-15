package model;

import java.security.NoSuchAlgorithmException;

public class LoginInformation {

    private final String usernameHash;
    private final String passwordHash;
    private final Hashing hashGenerator;

    //MODIFIES: this
    //EFFECTS: initialize a LoginInformation with a hashing object and a hash of username, password and checksum
    //REQUIRES:
    public LoginInformation(String username, String password) throws NoSuchAlgorithmException {
        hashGenerator = new Hashing();
        usernameHash = hashGenerator.getHash(username);
        passwordHash = hashGenerator.getHash(password);
    }

    //MODIFIES:
    //EFFECTS: returns 0 if given username and password match saved hash, 1 if checksum mismatch, -1 otherwise
    //REQUIRES:
    public boolean checkValues(String username, String password) {
        return usernameHash.equals(hashGenerator.getHash(username))
                && passwordHash.equals(hashGenerator.getHash(password));
    }

}

