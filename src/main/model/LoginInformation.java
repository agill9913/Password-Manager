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


    public int checkValues(String username, String password) {
        if (usernameHash.equals(getHash(username)) && passwordHash.equals(password)) {
            if (checksum.equals(username + password)) {
                return 0;
            }
            return 1;
        }
        return -1;
    }

}

