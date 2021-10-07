package model;

import java.security.NoSuchAlgorithmException;

public class LoginInformation extends SecretData {

    private String usernameHash;
    private String passwordHash;
    private String checksum;

    public LoginInformation(String username, String password) {
        usernameHash = getHash(username);
        passwordHash = getHash(password);
        checksum = getHash(username + password);
    }


    public boolean checkValues(String username) {
        return usernameHash.equals(getHash(username))
                ;
    }
}

