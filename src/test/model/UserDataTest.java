package model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.fail;

public class UserDataTest {

    @Test
    public void testUserDataToJson() {
        try {
            UserData testData = new UserData("pswd");
            JSONObject testObj = testData.toJson();
        } catch (NoSuchAlgorithmException e) {
            fail("There's no such algorithm exception");
        } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            fail("toJson went wrong");
        }
    }

}
