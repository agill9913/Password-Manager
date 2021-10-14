package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    private MessageDigest digest;

    Hashing() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-1");
    }

    public String getHash(String toHash) {
        digest.reset();
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}
