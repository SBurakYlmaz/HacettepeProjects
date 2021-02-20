import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtilServer
{   
    public static String toBase64(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static byte[] fromBase64(String str)
    {
        byte[] bytes = null;
        
        try {
            bytes = Base64.getDecoder().decode(str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
        return bytes;
    }
    
    public static byte[] encrypt(byte[] plainText, byte[] iv, byte[] key, CryptoMethod method, CryptoMode mode)
    {
        byte[] encrypted = null;
        
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, method.toString());
            
            String transformation = method.toString() + "/" + mode.toString() + "/" + CryptoPadding.PKCS5Padding.toString();
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encrypted = cipher.doFinal(plainText);
        } catch (Exception e) {
            System.err.println(e.toString());
        } 
        
        return encrypted;
    }
    
    public static byte[] decrypt(byte[] cipherText, byte[] iv, byte[] key, CryptoMethod method, CryptoMode mode)
    {
        byte[] decrypted = null;
        
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, method.toString());
            
            String transformation = method.toString() + "/" + mode.toString() + "/" + CryptoPadding.PKCS5Padding.toString();
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            decrypted = cipher.doFinal(cipherText);
        } catch (Exception e) {
            System.err.println(e.toString());
        } 
        
        return decrypted;
    }
    
    public static byte[] createRandomIV(int size)
    {
        byte[] iv = new byte[size];
        
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        
        return iv;
    }
    
    public static byte[] createRandomKey(int size)
    {
        byte[] key = new byte[size];
        
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        
        return key;
    }
}
