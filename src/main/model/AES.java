package model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AES {

    protected SecretKey key;

    AES() throws NoSuchAlgorithmException {
        key = makeKey();
    }

    private SecretKey makeKey() throws NoSuchAlgorithmException {
        KeyGenerator keyMaker = KeyGenerator.getInstance("AES");
        keyMaker.init(128);
        return keyMaker.generateKey();
    }

    public byte[] encrypt(String plain) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.ENCRYPT_MODE, key);
        return aes.doFinal(plain.getBytes());
    }

    public String decrypt(byte[] cipher) throws Exception {
        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.DECRYPT_MODE, key);
        return new String(aes.doFinal(cipher));
    }
}
