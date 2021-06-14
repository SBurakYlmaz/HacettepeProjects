import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoFiles {
    public static void write(String filePath, byte[] bytes) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try {
            Files.write(Paths.get(filePath), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] read(String filePath) {
        byte[] bytes = null;

        try {
            bytes = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static void writePrivateKey(String filePath, PrivateKey key) throws IOException {
        Writer out;
        Base64.Encoder encoder = Base64.getEncoder();

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        out = new FileWriter(filePath);
        out.write("-----BEGIN RSA PRIVATE KEY-----\n");
        out.write(encoder.encodeToString(key.getEncoded()));
        out.write("\n-----END RSA PRIVATE KEY-----\n");
        out.close();
    }

    public static PrivateKey readPrivateKey(String filePath) throws Exception {
        File file = new File(filePath);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        Base64.Decoder decoder = Base64.getDecoder();

        String privateKeyPEM = key
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll("\n", "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        byte[] encoded = decoder.decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static void writePublicKey(String filePath, PublicKey key) throws IOException {
        Writer out;
        Base64.Encoder encoder = Base64.getEncoder();

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        out = new FileWriter(filePath);
        out.write("-----BEGIN RSA PUBLIC KEY-----\n");
        out.write(encoder.encodeToString(key.getEncoded()));
        out.write("\n-----END RSA PUBLIC KEY-----\n");
        out.close();
    }

    public static PublicKey readPublicKey(String filePath) throws Exception {
        File file = new File(filePath);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        Base64.Decoder decoder = Base64.getDecoder();

        String publicKeyPEM = key
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replaceAll("\n", "")
                .replace("-----END RSA PUBLIC KEY-----", "");

        byte[] encoded = decoder.decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);

    }
}
