import java.util.HashMap;
import java.util.Map;

public class CryptoConst {
    public static final int DEFAULT_KEY_LENGTH = 2048;

    public static final String KDC_NAME = "KDC";
    public static final String ALICE_NAME = "Alice";
    public static final String MAIL_NAME = "Mail";
    public static final String DATABASE_NAME = "Database";
    public static final String WEB_NAME = "Web";

    public static final int KDC_PORT = 3000;
    public static final int MAIL_PORT = 3001;
    public static final int WEB_PORT = 3002;
    public static final int DATABASE_PORT = 3003;

    public static final String PASSWD_FILE_NAME = "passwd";
    private static final String PUBLIC_PATH = "./certs/";
    private static final String PRIVATE_PATH = "./keys/";
    private static final String KDC_PUBLIC_KEY_NAME = "kdc.cer";
    private static final String ALICE_PUBLIC_KEY_NAME = "alice.cer";
    private static final String MAIL_PUBLIC_KEY_NAME = "mail.cer";
    private static final String DATABASE_PUBLIC_KEY_NAME = "database.cer";
    private static final String WEB_PUBLIC_KEY_NAME = "web.cer";
    private static final String KDC_PRIVATE_KEY_NAME = "kdc.key";
    private static final String ALICE_PRIVATE_KEY_NAME = "alice.key";
    private static final String MAIL_PRIVATE_KEY_NAME = "mail.key";
    private static final String DATABASE_PRIVATE_KEY_NAME = "database.key";
    private static final String WEB_PRIVATE_KEY_NAME = "web.key";
    private static final Map<String, String> privateKeyNameMap = new HashMap<>();
    private static final Map<String, String> publicKeyNameMap = new HashMap<>();
    private static final Map<String, Integer> serverNamePortMap = new HashMap<>();
    public static String PASSWD_SHA1 = "";

    static {
        privateKeyNameMap.put(KDC_NAME, PRIVATE_PATH + KDC_PRIVATE_KEY_NAME);
        privateKeyNameMap.put(ALICE_NAME, PRIVATE_PATH + ALICE_PRIVATE_KEY_NAME);
        privateKeyNameMap.put(MAIL_NAME, PRIVATE_PATH + MAIL_PRIVATE_KEY_NAME);
        privateKeyNameMap.put(DATABASE_NAME, PRIVATE_PATH + DATABASE_PRIVATE_KEY_NAME);
        privateKeyNameMap.put(WEB_NAME, PRIVATE_PATH + WEB_PRIVATE_KEY_NAME);

        publicKeyNameMap.put(KDC_NAME, PUBLIC_PATH + KDC_PUBLIC_KEY_NAME);
        publicKeyNameMap.put(ALICE_NAME, PUBLIC_PATH + ALICE_PUBLIC_KEY_NAME);
        publicKeyNameMap.put(MAIL_NAME, PUBLIC_PATH + MAIL_PUBLIC_KEY_NAME);
        publicKeyNameMap.put(DATABASE_NAME, PUBLIC_PATH + DATABASE_PUBLIC_KEY_NAME);
        publicKeyNameMap.put(WEB_NAME, PUBLIC_PATH + WEB_PUBLIC_KEY_NAME);

        serverNamePortMap.put(KDC_NAME, KDC_PORT);
        serverNamePortMap.put(MAIL_NAME, MAIL_PORT);
        serverNamePortMap.put(DATABASE_NAME, DATABASE_PORT);
        serverNamePortMap.put(WEB_NAME, WEB_PORT);
    }

    public static String getPrivateKeyPath(String name) {
        return privateKeyNameMap.get(name);
    }

    public static String getPublicKeyPath(String name) {
        return publicKeyNameMap.get(name);
    }

    public static int getPort(String name) {
        return serverNamePortMap.get(name);
    }
}
