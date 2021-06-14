import java.io.*;
import java.util.Base64;

public class ReadFileEnc {
    private final String inputFileName;
    private final String outputFileName;
    private final String keyFileName;
    private final String mode;

    public ReadFileEnc(String inputFileName, String outputFileName, String keyFileName, String mode) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.keyFileName = keyFileName;
        this.mode = mode;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public String getKeyFileName() {
        return keyFileName;
    }

    public String getMode() {
        return mode;
    }

    public void readFile(String inputFileName, String outputFileName, String keyFileName, String mode) throws IOException {
        Encryption encryption = new Encryption();
        StringBuilder cipherText;
        StringBuilder binary = new StringBuilder();
        StringBuilder binaryKey = new StringBuilder();

        File plainText = new File(inputFileName);
        File keyText = new File(keyFileName);
        FileReader fr = new FileReader(plainText);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null) {
            binary.append(line);
            line = br.readLine();
        }
        //System.out.println("Binary plaintext = " + binary.toString());
        //System.out.println("Binary plaintext length = " + binary.length());
        br.close();
        fr.close();

        fr = new FileReader(keyText);
        br = new BufferedReader(fr);
        line = br.readLine();

        while (line != null) {
            byte[] decodedString = Base64.getDecoder().decode(line);
            String theKey = new String(decodedString);
            binaryKey = new StringBuilder(theKey);
            //System.out.println("Key length = " + binaryKey.length());
            //System.out.println("Key  = " + binaryKey.toString());
            line = br.readLine();
        }

        br.close();
        fr.close();
        int padding = (binary.length() % 96);
        if (padding != 0) {
            padding = 96 - padding;
            while (padding != 0) {
                binary.append("0");
                padding--;
            }
        }
        //System.out.println("Padding applied to binary text =" + binary.toString());
        //System.out.println("Padding applied to binary text length =" + binary.length());
        //System.out.println(binaryKey.length());
        cipherText = encryption.encryptPlainText(binary, binaryKey, mode);
        //System.out.println("CipherText in binary= " + cipherText.toString());
        try {
            FileWriter myWriter = new FileWriter(outputFileName);
            myWriter.write(String.valueOf(cipherText));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
