import java.io.IOException;

public class BBMcrypt {
    public static void main(String[] args) throws IOException {
        /*Checks the arguments of the program if less more than expected it prints an error and exits*/
        if (args.length != 9) {
            System.out.println("Missing argument or more arguments than expected");
            System.exit(0);
        } else {
            String input = "";
            String key = "";
            String output = "";
            String mode = "";
            for (int i = 1; i < 8; i++) {
                switch (args[i]) {
                    case "-I":
                        input = args[i + 1];
                        break;
                    case "-O":
                        output = args[i + 1];
                        break;
                    case "-M":
                        mode = args[i + 1];
                        break;
                    case "-K":
                        key = args[i + 1];
                        break;
                }
            }
            if (args[0].equals("enc")) {
                ReadFileEnc readFileEnc = new ReadFileEnc(input, output, key, mode);
                readFileEnc.readFile(readFileEnc.getInputFileName(), readFileEnc.getOutputFileName(), readFileEnc.getKeyFileName(), readFileEnc.getMode());
            } else if (args[0].equals("dec")) {
                ReadFileDec readFileDec = new ReadFileDec(input, output, key, mode);
                readFileDec.readFile(readFileDec.getInputFileName(), readFileDec.getOutputFileName(), readFileDec.getKeyFileName(), readFileDec.getMode());
            } else {
                System.out.println("You must write enc or dec to use it");
                System.exit(1);
            }
        }
    }
}