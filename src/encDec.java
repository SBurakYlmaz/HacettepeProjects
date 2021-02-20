import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class encDec {

    public static byte[] encrypt(byte[] plainText, byte[] encryptionKey)
            throws Exception {

        byte[] vectorBytes = new byte[16];

        /*Determine the mode and algorithm for cipher*/
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(vectorBytes));
        return cipher.doFinal(plainText);
    }

    public byte[] decrypt(byte[] cipherText, byte[] decKey)
            throws Exception {

        byte[] vectorBytes = new byte[16];

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(decKey, "AES");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(vectorBytes));

        return cipher.doFinal(cipherText);
    }

    public byte[] doEncryption(byte[] plaintext, byte[] encKey) throws Exception {

        String appendText = "roses are red and violets are blue honey is sweet, but not as sweet as you";
        byte[] additionText = Base64.getEncoder().encode(appendText.getBytes());
        byte[] combined = new byte[additionText.length + plaintext.length];

        System.arraycopy(plaintext, 0, combined, 0, plaintext.length);
        System.arraycopy(additionText, 0, combined, plaintext.length, additionText.length);

        return encrypt(combined, encKey);
    }
}
