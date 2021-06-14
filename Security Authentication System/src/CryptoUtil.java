import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class CryptoUtil {
    private static final String SYMMETRIC_KEY_ALGORITHM = "AES";
    private static final String SYMMETRIC_KEY_CIPHER = "AES/CBC/PKCS5PADDING";
    private static final int SESSION_KEY_LENGTH = 16;

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static byte[] encrypt(String data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(data));
    }

    public static String encrypt(String plainText, String secret) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), SYMMETRIC_KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(SYMMETRIC_KEY_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), SYMMETRIC_KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(SYMMETRIC_KEY_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Long getTimestamp() {
        //return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        return System.currentTimeMillis() / 1000L;
    }

    public static String createRandom(int size) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < size; i++) {
            int ch = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(ch));
        }

        return builder.toString();
    }

    public static String createSessionKey() {
        return createRandom(SESSION_KEY_LENGTH);
    }

    public static long createNonce() {
        return Math.abs(new Random().nextLong());
    }

    public static String createSHA1HashBase64(String input) {
        String output = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());

            output = Base64.getEncoder().encodeToString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return output;
    }
}
