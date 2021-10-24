package model;

import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//represents an instance of SHA-1 that can be used as a 1 way hash method for strings
public class Hashing {

    private MessageDigest digest;

    //EFFECTS: initialize an object with an instance of SHA-1 to use for hashing
    //Referenced for this method: http://oliviertech.com/java/generate-SHA1-hash-from-a-String/
    Hashing() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-1");
    }

    Hashing(byte[] digest) throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance("SHA-1");
        this.digest.digest(digest);
    }

    //EFFECTS: Takes a string and returns a String of its hash
    //Referenced for this method: http://oliviertech.com/java/generate-SHA1-hash-from-a-String/
    public String getHash(String toHash) {
        digest.reset();
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    public byte[] toByteArray() {
        return digest.digest();
    }
}