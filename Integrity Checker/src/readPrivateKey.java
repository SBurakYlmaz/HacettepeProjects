import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class readPrivateKey {

    private final File privateKeyFile;

    public readPrivateKey(File privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String keyReader() throws IOException {
        FileReader fr = new FileReader(privateKeyFile);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder priKey = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            priKey.append(line);
            line = br.readLine();
        }

        br.close();
        fr.close();

        return priKey.toString();
    }
}
