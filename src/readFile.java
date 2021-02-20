import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class readFile {

    public String MonitoringFile(File pathMonitor, File regFile, FileWriter log, String hashMode) throws IOException, NoSuchAlgorithmException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        FileWriter writeReg = new FileWriter(regFile);
        log.write(sdf.format(new Date()) + " Registry file is created at " + regFile.getAbsolutePath() + "\n");

        String directory = pathMonitor.getAbsolutePath();
        ArrayList<String> contents = new ArrayList<>();

        File[] files = new File(directory).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    contents.add(file.getAbsolutePath());
                }
            }
        }

        if (contents.size() == 0) {
            log.write(sdf.format(new Date()) + " You are trying to observe an empty directory\n");
            System.exit(4);
        }

        for (String content : contents) {
            MessageDigest md = MessageDigest.getInstance(hashMode);
            try (DigestInputStream dis = new DigestInputStream(new FileInputStream(content), md)) {
                while (dis.read() != -1) ; //empty loop to clear the data
                md = dis.getMessageDigest();

                StringBuilder result = new StringBuilder(content + " ");
                for (byte b : md.digest()) {
                    result.append(String.format("%02x", b));
                }
                writeReg.write(String.valueOf(result));
                writeReg.write("\n");
                writeReg.flush();
                log.write(sdf.format(new Date()) + " " + content + " is added to registry.\n");
            }
        }
        log.write(sdf.format(new Date()) + " " + contents.size() + " files are added to the registry and registry " +
                "creation is finished!\n");

        StringBuilder resultSignature = new StringBuilder();
        FileReader fr = new FileReader(regFile);

        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();

        while (line != null) {
            resultSignature.append(line);
            line = br.readLine();
        }
        fr.close();
        br.close();
        hash h = new hash(hashMode, resultSignature.toString());
        byte[] eq = h.digest();

        StringBuilder result = new StringBuilder();
        for (byte b : eq) {
            result.append(String.format("%02x", b));
        }
        writeReg.close();
        log.close();
        return resultSignature.toString();
    }

    public String takeSignature(File regFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(regFile));
        String lastLine = "", sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            lastLine = sCurrentLine;
            lastLine = lastLine.replaceAll("#", "");
            lastLine = lastLine.replaceAll(" ", "");
        }
        return lastLine;
    }

    public String detectRegFileAttack(File regFile, String hashMode) throws NoSuchAlgorithmException, IOException {
        StringBuilder resultSignature = new StringBuilder();
        FileReader fr = new FileReader(regFile);

        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();

        while (line != null) {
            if (line.charAt(0) == '#') {
            } else {
                resultSignature.append(line);
            }
            line = br.readLine();
        }
        fr.close();
        br.close();
        hash h = new hash(hashMode, resultSignature.toString());
        byte[] eq = h.digest();

        /*StringBuilder result = new StringBuilder();
        for (byte b : eq) {
            result.append(String.format("%02x", b));
        }
        System.out.println(result);*/
        return resultSignature.toString();
    }

    public void detectDifference(File regFile, File observingFile, File log, String hashMode) throws IOException, NoSuchAlgorithmException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        FileWriter logWrite = new FileWriter(log, true);

        Map<String, String> fileNamesAndHashesRegFile = new HashMap<>();
        Map<String, String> fileNamesAndHashesObservingFile = new HashMap<>();

        FileReader fr = new FileReader(regFile);

        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();

        while (line != null) {
            if (line.charAt(0) == '#') {
            } else {
                String[] split = line.split(" ");
                fileNamesAndHashesRegFile.put(split[0], split[1]);
            }
            line = br.readLine();
        }
        fr.close();
        br.close();

        File[] files = new File(observingFile.getAbsolutePath()).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    MessageDigest md = MessageDigest.getInstance(hashMode);
                    StringBuilder result = new StringBuilder();
                    try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file.getAbsolutePath()), md)) {
                        while (dis.read() != -1) ; //empty loop to clear the data
                        md = dis.getMessageDigest();


                        for (byte b : md.digest()) {
                            result.append(String.format("%02x", b));
                        }
                        fileNamesAndHashesObservingFile.put(file.getAbsolutePath(), result.toString());
                    }
                }
            }
        }

        boolean check = true;
        for (String fileName : fileNamesAndHashesObservingFile.keySet()) {
            /*If both reg file and observing file contains the same file*/
            if (fileNamesAndHashesRegFile.containsKey(fileName)) {
                /*If the given file does not change*/
                if (!fileNamesAndHashesObservingFile.get(fileName).equals(fileNamesAndHashesRegFile.get(fileName))) {
                    logWrite.write(sdf.format(new Date()) + " " + fileName + " altered\n");
                    logWrite.flush();
                    check = false;
                }
                fileNamesAndHashesRegFile.remove(fileName);
            } else {
                logWrite.write(sdf.format(new Date()) + " " + fileName + " created\n");
                logWrite.flush();
                check = false;
            }
        }
        for (String fileName : fileNamesAndHashesRegFile.keySet()) {
            logWrite.write(sdf.format(new Date()) + " " + fileName + " deleted\n");
            logWrite.flush();
            check = false;
        }
        if (check) {
            logWrite.write(sdf.format(new Date()) + " The directory is checked and no change is detected!\n");
            logWrite.flush();
            logWrite.close();
            return;
        }

        logWrite.close();
    }
}
