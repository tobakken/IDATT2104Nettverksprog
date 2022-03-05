package ntnu.stud.tobakken.coderunner.service;

import java.io.*;

public class FileService {
    public void writeToFile(File file, String code) {
        String[] lineSep = code.split("\"\\r?\\n|\\r");

        for (int i = 0; i < lineSep.length; i++) {
            System.out.println(lineSep[i]);
        }

        try (OutputStream os = new FileOutputStream(file);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(os))) {
            for (int i = 0; i < lineSep.length; i++) {
                pw.write(lineSep[i] + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
