package model;

import javax.sound.midi.SysexMessage;

public class LoginInformation extends SecretData {

    private final String usernameHash;
    private final String passwordHash;

    public LoginInformation(String username, String password) {
        super(username + password);
        usernameHash = getHash(username);
        passwordHash = getHash(password);
    }


    public int checkValues(String username, String password) {
        if (usernameHash.equals(getHash(username)) && passwordHash.equals(getHash(password))) {
            if (checksum.equals(getHash(username + password))) {
                return 0;
            }
            return 1;
        }
        return -1;
    }

}

