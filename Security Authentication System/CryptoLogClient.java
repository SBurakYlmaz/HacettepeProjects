import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CryptoLogClient {
    private static final String LOG_FILE = "Alice_Log.txt";

    private static CryptoLogClient sInstance;
    private static FileWriter sFileWriter;

    private static DateTimeFormatter sDateTimeFormatter;

    static {
        try {
            sDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            sInstance = new CryptoLogClient();
            sFileWriter = new FileWriter(LOG_FILE);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private CryptoLogClient() {
    }

    public static CryptoLogClient getInstance() {
        return sInstance;
    }

    public synchronized void write(String str) {
        try {
            LocalDateTime now = LocalDateTime.now();
            sFileWriter.write(sDateTimeFormatter.format(now) + " " + str);
            sFileWriter.flush();

            System.out.print(str);
            System.out.flush();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public synchronized void writeLine(String str) {
        try {
            LocalDateTime now = LocalDateTime.now();
            sFileWriter.write(sDateTimeFormatter.format(now) + " " + str + "\n");
            sFileWriter.flush();

            System.out.println(str);
            System.out.flush();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
