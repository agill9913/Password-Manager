package model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

//Referenced this code to make the class: https://www.quickprogrammingtips.com/java/how-to-encrypt-and-decrypt-data-in-java-using-aes-algorithm.html

//Represents a cipher that uses AES to encrypt strings
public class AES {

    private SecretKey key;

    //EFFECTS: initializes a key to use when encrypting and decrypting data with aes
    AES() throws NoSuchAlgorithmException {
        key = makeKey();
    }

    //MODIFIES: this
    //EFFECTS: generates an aes key of size 128 and returns it
    private SecretKey makeKey() throws NoSuchAlgorithmException {
        KeyGenerator keyMaker = KeyGenerator.getInstance("AES");
        keyMaker.init(128);
        return keyMaker.generateKey();
    }

    //EFFECTS: the string is encrypted by an instance of AES then returned as an encrypted byte array
    public byte[] encrypt(String plain) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.ENCRYPT_MODE, key);
        return aes.doFinal(plain.getBytes());
    }

    //EFFECTS: a byte array is decrypted by an instance of AES then returned as a string
    public String decrypt(byte[] cipher) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.DECRYPT_MODE, key);
        return new String(aes.doFinal(cipher));
    }
}
