import java.io.*;
import java.util.Base64;

public class ReadFileDec {
    private final String inputFileName;
    private final String outputFileName;
    private final String keyFileName;
    private final String mode;

    public ReadFileDec(String inputFileName, String outputFileName, String keyFileName, String mode) {
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
        Decryption decryption = new Decryption();
        StringBuilder plainText = new StringBuilder();
        StringBuilder binary = new StringBuilder();
        StringBuilder binaryKey = new StringBuilder();

        File cipherText = new File(inputFileName);
        File keyText = new File(keyFileName);
        FileReader fr = new FileReader(cipherText);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null) {
            binary.append(line);
            line = br.readLine();
        }
        //System.out.println("Binary cipherText = " + binary.toString());
        //System.out.println("Binary cipherText length = " + binary.length());
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
        //System.out.println("Padding applied to binary text =" + binary.toString());
        //System.out.println("Padding applied to binary text length =" + binary.length());
        //System.out.println(binaryKey.length());
        plainText = decryption.decryptCipherText(binary, binaryKey, mode);
        //System.out.println("Plain text in binary= " + plainText.toString());
        try {
            FileWriter myWriter = new FileWriter(outputFileName);
            myWriter.write(String.valueOf(plainText));
            myWriter.close();
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}