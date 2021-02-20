import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class ichecker {

    public static void main(String[] args) throws Exception {
        File privateKeyFile = null;
        File certificateFile = null;
        File regFile = null;
        File observingFile = null;
        File logFile = null;
        String hashMode = null;
        String privateKey;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        for (int i = 1; i < args.length; i += 2) {
            switch (args[i]) {
                case "-r":
                    regFile = new File(args[i + 1]);
                    regFile.getParentFile().mkdirs();
                    break;
                case "-p":
                    observingFile = new File(args[i + 1]);
                    break;
                case "-l":
                    logFile = new File(args[i + 1]);
                    logFile.getParentFile().mkdirs();
                    break;
                case "-h":
                    hashMode = args[i + 1];
                    break;
                case "-k":
                    privateKeyFile = new File(args[i + 1]);
                    privateKeyFile.getParentFile().mkdirs();
                    break;
                case "-c":
                    certificateFile = new File(args[i + 1]);
                    certificateFile.getParentFile().mkdirs();
                    break;
                default:
                    System.out.println("Undefined argument");
                    System.exit(2);
            }
        }

        switch (args[0]) {
            /*createCert -k C:\Users\Asus\IdeaProjects\private\privateKey.txt -c C:\Users\Asus\IdeaProjects\public\cert\certificate*/
            case "createCert":
                keyGeneration keyGeneration = new keyGeneration(certificateFile);
                encDec enc = new encDec();
                keyGeneration.generateKeyPair();

                System.out.println("Enter password : ");

                /*Waiting password from the user*/
                Scanner readInput = new Scanner(System.in);
                String password = readInput.nextLine();
                readInput.close();

                /*Take the hash of the password by using MD5 algorithm*/
                hash hashPassword = new hash("MD5", password);

                /*Hashed value of the password*/
                byte[] hashedPasswordKey = hashPassword.digest();

                byte[] cipher = enc.doEncryption(Base64.getEncoder().encode(keyGeneration.getKeyPair().getPrivate().getEncoded()), hashedPasswordKey);

                //System.out.println("Encrypted private key in base64 form new string= " + new String(Base64.getEncoder().encode(cipher)));

                /*Write private key to txt file*/
                FileWriter privateKeyWriter = new FileWriter(privateKeyFile.getAbsolutePath());
                privateKeyWriter.write(new String(Base64.getEncoder().encode(cipher)));
                privateKeyWriter.close();

                break;
            case "createReg":
 /*createReg -r C:\Users\Asus\IdeaProjects\regFile\register.txt -p C:\Users\Asus\Desktop\deneme -l C:\Users\Asus\IdeaProjects\log\logfile.txt
                -h MD5 -k C:\Users\Asus\IdeaProjects\private\privateKey.txt*/

                FileWriter log = null;

                if (logFile != null) {
                    log = new FileWriter(logFile, true);
                } else {
                    System.out.println("Monitoring file is null please check the file path");
                    System.exit(5);
                }
                System.out.println("Enter password : ");

                /*Waiting password from the user*/
                Scanner readInputReg = new Scanner(System.in);
                String decPassword = readInputReg.nextLine();
                readInputReg.close();

                readPrivateKey readPrivateKey = new readPrivateKey(privateKeyFile);

                privateKey = readPrivateKey.keyReader();

                //System.out.println("Private Key = " + privateKey);

                hash hashDecPassword = new hash("MD5", decPassword);

                /*Hashed value of the password*/
                byte[] hashedDecPassword = hashDecPassword.digest();

                encDec dec = new encDec();

                /*Encrypt the private key by using the hashed password which was constructed by MD5 algorithm*/

                byte[] originalPrivateKeyBytes = Base64.getDecoder().decode(privateKey);

                try {
                    byte[] decryptedPrivateKey = dec.decrypt(originalPrivateKeyBytes, hashedDecPassword);

                    String originalKey = new String(decryptedPrivateKey);
                    String removedText = Base64.getEncoder().encodeToString("roses are red and violets are blue honey is sweet, but not as sweet as you".getBytes());

                    readFile readFile = new readFile();
                    String finalKey = originalKey.replace(removedText, "");

                    //System.out.println("Original Private Key" + finalKey);

                    String hashRegFile = null;

                    if (observingFile != null) {
                        hashRegFile = readFile.MonitoringFile(observingFile, regFile, log, hashMode);
                        //System.out.println(hashRegFile);
                    } else {
                        System.out.println("Monitoring file is null please check the file path");
                        System.exit(5);
                    }

                    //System.out.println(hashRegFile);

                    signature sign = new signature(hashRegFile, finalKey);
                    String signature = sign.CreateSignature();
                    FileWriter regWrite = new FileWriter(regFile, true);
                    regWrite.write("# " + signature + " #");
                    regWrite.flush();
                    regWrite.close();

                } catch (BadPaddingException wrongKey) {
                    log.write(sdf.format(new Date()) + " Wrong password attempt!\n");
                    log.flush();
                    log.close();
                    System.exit(3);
                }
                break;
            case "check":
/*check -r C:\Users\Asus\IdeaProjects\regFile\register.txt -p C:\Users\Asus\Desktop\deneme -l C:\Users\Asus\IdeaProjects\log\logfile.txt
-h MD5 -c C:\Users\Asus\IdeaProjects\public\cert\certificate*/

                readFile readFile = new readFile();
                String signature = readFile.takeSignature(regFile);
                String hashedData = null;

                if (observingFile != null) {
                    hashedData = readFile.detectRegFileAttack(regFile, hashMode);
                } else {
                    FileWriter fr = new FileWriter(logFile, true);
                    fr.write(sdf.format(new Date()) + "Monitoring file is null please check the file path");
                    fr.flush();
                    fr.close();
                    System.exit(5);
                }

                signature verify = new signature(signature);
                boolean check = verify.VerifySignature(certificateFile, hashedData);
                if (!check) {
                    FileWriter fr = new FileWriter(logFile, true);
                    fr.write(sdf.format(new Date()) + " Registry file verification failed!\n");
                    fr.flush();
                    fr.close();
                } else {
                    readFile.detectDifference(regFile, observingFile, logFile, hashMode);
                }
                break;
            default:
                FileWriter fr = new FileWriter(logFile, true);
                fr.write(sdf.format(new Date()) + " Undefined Command\n");
                fr.flush();
                fr.close();
                System.exit(1);
        }
    }

}
