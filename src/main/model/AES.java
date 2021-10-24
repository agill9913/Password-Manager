package model;

import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//Represents a cipher that uses AES to encrypt strings
public class AES {

    private final SecretKey key;

    //EFFECTS: initializes a key to use when encrypting and decrypting data with aes
    public AES() throws NoSuchAlgorithmException {
        key = KeyGenerator.getInstance("AES").generateKey();
    }

    public AES(String data) throws NoSuchAlgorithmException {
        byte[] tmpArr = Base64.getDecoder().decode(data);
        key = new SecretKeySpec(tmpArr, 0, tmpArr.length, "AES");
    }

    //MODIFIES: this
    //EFFECTS: generates an aes key of size 128 and returns it
    //Reference used for this method =: https://www.baeldung.com/java-aes-encryption-decryption
    private SecretKey makeKey() throws NoSuchAlgorithmException {
        KeyGenerator keyMaker = KeyGenerator.getInstance("AES");
        keyMaker.init(128);
        return keyMaker.generateKey();
    }

    //EFFECTS: the string is encrypted by an instance of AES then returned as an encrypted byte array
    //Reference used for this method = https://www.javatpoint.com/aes-256-encryption-in-java
    public byte[] encrypt(String plain) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.ENCRYPT_MODE, key);
        return aes.doFinal(plain.getBytes());
    }

    //EFFECTS: a byte array is decrypted by an instance of AES then returned as a string
    //Reference used for this method = https://www.javatpoint.com/aes-256-encryption-in-java
    public String decrypt(byte[] cipher) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.DECRYPT_MODE, key);
        return new String(aes.doFinal(cipher));
    }

    public JSONObject toJson() {
        return new JSONObject().put("key", Base64.getEncoder().encodeToString(key.getEncoded()));
    }

}
