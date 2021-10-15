package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//represents a instance of SHA-1 that can be used as a 1 way hash method for strings
public class Hashing {

    private MessageDigest digest;

    //EFFECTS: initialize a object with a instance of SHA-1 to use for hashing
    Hashing() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-1");
    }

    //EFFECTS: Takes a string and returns a String of it's hash
    public String getHash(String toHash) {
        digest.reset();
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}
