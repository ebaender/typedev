package extra;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyMan {

    public static String getKey() throws NoSuchAlgorithmException {
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(256);
        SecretKey key = aesGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}