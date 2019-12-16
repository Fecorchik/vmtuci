package auth;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {

    public static String toBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decodeBase64(String str) {
        return new String(Base64.getDecoder().decode(str),StandardCharsets.UTF_8);
    }

    public static String decodeBase64(byte[] bytes) {
        return new String(Base64.getDecoder().decode(bytes));
    }

    public static String toSha256(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(str.getBytes(StandardCharsets.UTF_8));

        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for(byte b: digest){
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
