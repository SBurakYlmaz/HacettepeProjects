import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hash {

    private final String hashMode;
    private final String message;

    public hash(String hashMode, String message) {
        this.hashMode = hashMode;
        this.message = message;
    }

    public byte[] digest() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(this.hashMode);
        return md.digest(this.message.getBytes());
    }

}
