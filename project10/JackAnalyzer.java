import java.io.File;
import java.util.ArrayList;

public class JackAnalyzer {
    //go through the files in the directory and use only jack files
    public static ArrayList<File> getJackFiles(File directory){
        File[] files = directory.listFiles();
        ArrayList<File> result = new ArrayList<File>();
        if (files == null) return result;
        for (File f:files){
            if (f.getName().endsWith(".jack")){
                result.add(f);
            }
        }
        return result;
    }
}

