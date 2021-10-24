package model;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

//Represents a cipher that uses AES to encrypt strings
public class AES {

    private SecretKeySpec encryptKey;

    //EFFECTS: initializes a key to use when encrypting and decrypting data with aes
    public AES(String pswd) throws NoSuchAlgorithmException {
        encryptKey = makeKey(pswd);
    }

    //MODIFIES: this
    //EFFECTS: updates the AES key with appropriate key value
    public void updateKey(String hash) throws NoSuchAlgorithmException {
        this.encryptKey = makeKey(hash);
    }

    //MODIFIES: this
    //EFFECTS: generates a key for use with AES and returns it
    //Referenced for this method: https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
    private SecretKeySpec makeKey(String mykey) throws NoSuchAlgorithmException {
        SecretKeySpec retKey;
        byte[] keyArr = mykey.getBytes(StandardCharsets.UTF_8);
        MessageDigest hash = MessageDigest.getInstance("SHA-1");
        keyArr = Arrays.copyOf(hash.digest(keyArr), 16);
        retKey =  new SecretKeySpec(keyArr, "AES");
        return retKey;
    }

    //EFFECTS: returns a string encrypted with AES
    //Referenced for this method: https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
    public String encrypt(String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes("UTF-8")));
    }

    //REQUIRES: The String "value" is already encrypted by AES
    //EFFECTS: returns a string decrypted by AES
    //Referenced for this method: https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
    public String decrypt(String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, encryptKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(value)));
    }

}
