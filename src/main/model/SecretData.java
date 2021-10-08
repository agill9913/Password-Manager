package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//todo - add encryption via aes
//todo - create information data class

abstract class SecretData {
    protected MessageDigest digest;
    protected String checksum;

    SecretData(String combinedData) {
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        checksum = getHash(combinedData);
    }


    protected String getHash(String toHash) {
        digest.reset();
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}
