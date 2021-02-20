import java.io.File;

public class CryptoKDC {
    private static final CryptoLogKDC sLogger = CryptoLogKDC.getInstance();

    public static void main(String[] args) {
        CryptoKDC cryptoKDC = new CryptoKDC();
        cryptoKDC.createKeyFiles();
        cryptoKDC.createPasswordFile();

        CryptoServerKDC server = new CryptoServerKDC(CryptoConst.KDC_PORT);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }
    }

    private void createKeyFile(String name) {
        File file;

        try {
            file = new File(CryptoConst.getPublicKeyPath(name));

            if (!file.exists()) {
                CryptoKeyGenerator keyGenerator = new CryptoKeyGenerator(CryptoConst.DEFAULT_KEY_LENGTH);
                keyGenerator.createKeys();

                CryptoFiles.writePublicKey(file.getAbsolutePath(), keyGenerator.getPublicKey());

                file = new File(CryptoConst.getPrivateKeyPath(name));
                CryptoFiles.writePrivateKey(file.getAbsolutePath(), keyGenerator.getPrivateKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPasswordFile() {
        try {
            File file = new File(CryptoConst.PASSWD_FILE_NAME);

            String password = CryptoUtil.createRandom(10);
            String sha1Base64 = CryptoUtil.createSHA1HashBase64(password);
            CryptoConst.PASSWD_SHA1 = sha1Base64;

            CryptoFiles.write(file.getAbsolutePath(), sha1Base64.getBytes());
            sLogger.writeLine(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createKeyFiles() {
        createKeyFile(CryptoConst.KDC_NAME);
        createKeyFile(CryptoConst.ALICE_NAME);
        createKeyFile(CryptoConst.MAIL_NAME);
        createKeyFile(CryptoConst.DATABASE_NAME);
        createKeyFile(CryptoConst.WEB_NAME);
    }
}
