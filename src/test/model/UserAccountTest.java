package model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountTest {

    @Test
    public void testToJson() {
        String username = "bob27";
        String usernameHash ="94de00cc8687272fb3bfed456910197d743673ce";
        String pswd = "word123";
        String pswdHash = "72ea5dfb06308f17cd06d2d8e2873d5968550d95";
        try {
            UserAccount testAccount = new UserAccount(username, pswd, true);
            testAccount.addData("google", "username", "user35");
            JSONObject testObj = testAccount.toJson();
            assertEquals(testObj.getJSONObject("LoginInfo").get("username"), usernameHash);
            assertEquals(testObj.getJSONObject("LoginInfo").get("password"), pswdHash);

            //data should be encrypted, therefore as long as the data in json doesn't match plain text
            //and exists, test should pass for line 27, AESTest tests if decryption is possible
            assertNotEquals(testObj.getJSONObject("data").getJSONObject("google").get("username"), "user35");
        } catch (NoSuchAlgorithmException e) {
            fail("This shouldn't happen - no such algorithm");
        } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            fail("Something went wrong with toJson");
        }
    }

}
