import java.io.File;

public class Utility {
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
}
