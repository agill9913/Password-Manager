package model;

import java.security.NoSuchAlgorithmException;

public class LoginInformation {

    private final String usernameHash;
    private final String passwordHash;
    private final Hashing hashGenerator;
    private final String checksum;

    //MODIFIES: this
    //EFFECTS: initialize a LoginInformation with a hashing object and a hash of username, password and checksum
    //REQUIRES:
    public LoginInformation(String username, String password) throws NoSuchAlgorithmException {
        hashGenerator = new Hashing();
        usernameHash = hashGenerator.getHash(username);
        passwordHash = hashGenerator.getHash(password);
        checksum = hashGenerator.getHash(username + password);
    }

    public String getChecksum() {
        return checksum;
    }

    public boolean checksumCheck(String hashValue) {
        return checksum.equals(hashGenerator.getHash(hashValue));
    }

    //MODIFIES:
    //EFFECTS: returns 0 if given username and password match saved hash, 1 if checksum mismatch, -1 otherwise
    //REQUIRES:
    public int checkValues(String username, String password) {
        int errorCode = 0;
        if (usernameHash.equals(hashGenerator.getHash(username))
                && passwordHash.equals(hashGenerator.getHash(password))) {
            if (checksumCheck(username + password)) {
                return errorCode;
            } else {
                errorCode = 1;
            }
        } else {
            errorCode = -1;
        }
        return errorCode;
    }

}

