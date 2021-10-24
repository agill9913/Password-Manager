package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AESTest {

    //AES cipher;

    @BeforeEach
    public void init() throws NoSuchAlgorithmException {
        //cipher = new AES();
    }

    @Test
    public void encryptionTest() throws Exception {
        //assertNotEquals(new String(cipher.encrypt("Welcome to the dark side")), "Welcome to the dark side");
    }

    @Test
    public void decryptionTest() throws Exception {
        //assertEquals(cipher.decrypt(cipher.encrypt("Welcome to the dark side")), "Welcome to the dark side");
    }
}
