import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utility {
    private static final String tokenFile = ".token";// file path to git access token

    /**
     * clearDirectory attempts to clear all files in a given directory.
     * If the program will attempt to delete the file 10 times with a interval on 0.5 seconds.
     *
     * @param directoryName the name of the directory containing the files that should be deleted.
     */
    public static void clearDirectory(String directoryName) {
        File dir = new File(directoryName);
        if (!dir.exists())
            return;
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File f : files) {
            for (int i = 0; i < 10; i++) {
                boolean status = f.delete();
                if (status) {
                    break;
                } else {
                    System.out.println("Attempting to delete file: " + f.getName() + ", attempt: " + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * Reads git access token from file
     *
     * @return The git access token
     */
    public static String readToken() throws IOException {
        File file = new File(tokenFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String token = reader.readLine();
        reader.close();
        return token;
    }
}
