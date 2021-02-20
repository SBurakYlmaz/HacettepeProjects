import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class keyGeneration {
    private final File certificatePath;
    private KeyPair keyPair;

    public keyGeneration(File certificatePath) {
        this.certificatePath = certificatePath;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void exportCertificate() throws IOException, InterruptedException {
        String command = "keytool -export -keystore keys.jks -alias CertificateBBM465 -file X509_certificate.cer -rfc -storepass bbm465";
        Process process1 = Runtime.getRuntime().exec(command);
        process1.waitFor();
    }

    public void createPairs() throws IOException, InterruptedException {
        String command = "keytool -genkeypair " +
                " -alias CertificateBBM465 " +
                " -keyalg RSA " +
                " -dname CN=Java " +
                " -keypass bbm465 " +
                " -keystore keys.jks " +
                " -storepass bbm465" +
                " -keysize 2048";
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    public void generateKeyPair() throws IOException, InterruptedException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        File f = new File("keys.jks");
        File f1 = new File("X509_certificate.cer");

        if (f.exists() && !f.isDirectory()) {
            if (f.delete()) {
                createPairs();
                exportCertificate();
            }
        } else {
            createPairs();
            exportCertificate();
        }

        FileInputStream stream = new FileInputStream("keys.jks");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(stream, "bbm465".toCharArray());

        String username = "CertificateBBM465";

        char[] password = "bbm465".toCharArray();

        Key key = keyStore.getKey(username, password);

        if (key instanceof PrivateKey) {
            Certificate certificate = keyStore.getCertificate(username);

            PublicKey publicKey = certificate.getPublicKey();

            keyPair = new KeyPair(publicKey, (PrivateKey) key);

            /*System.out.println("Public Key = " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            System.out.println("Private Key = " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));*/
        }

        /*Move the certificate to the given path*/
        try {
            Files.move(f1.toPath(), this.certificatePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("System cannot move the certificate to given path");
        }
    }
}