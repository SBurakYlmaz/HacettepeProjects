import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class signature {

    private final String message;
    private String privateKey;

    public signature(String message, String privateKey) {
        this.message = message;
        this.privateKey = privateKey;
    }

    public signature(String message) {
        this.message = message;
    }

    public String CreateSignature() throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privates = fact.generatePrivate(keySpec);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privates);

        byte[] messageBytes = message.getBytes();

        signature.update(messageBytes);

        byte[] digitalSignature = signature.sign();

        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    public boolean VerifySignature(File publicKeyFile, String hashedData) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, CertificateException {

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        FileInputStream is = new FileInputStream(publicKeyFile);
        X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
        PublicKey key = cer.getPublicKey();

        byte[] data = hashedData.getBytes();

        byte[] messageBytes = Base64.getDecoder().decode(this.message);

        Signature signature = Signature.getInstance("SHA256withRSA");

        signature.initVerify(key);

        signature.update(data);
        return signature.verify(messageBytes);
    }

}


