package src.MusicController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encoder {

    private static final String AES_ALGORITHM = "AES";
    private static final String secretKey = "bMHcQYpCAtdbgLANoADZpLc97wIVhGpOXAf1KGFlcXE=";

    public static char[] encrypt(char[] data) throws Exception {
        return Base64.getEncoder().encodeToString(String.valueOf(data).getBytes()).toCharArray();
    }

    public static char[] decrypt(char[] encryptedData) throws Exception {
        return new String(Base64.getDecoder().decode(String.valueOf(encryptedData))).toCharArray();
    }
}
