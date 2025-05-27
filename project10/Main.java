import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
           throw new RuntimeException("Invalid number of arguments");
        } else {
            String inputFileName = args[0];
            File input = new File(inputFileName);
            String outputFilePath = "";
            File outputFile;
            ArrayList<File> jackFiles = new ArrayList<File>();
            if (input.isFile()) {
                String path = input.getAbsolutePath();
                if (!path.endsWith(".jack")) {
                    throw new IllegalArgumentException("Input file is not a Jack file");
                }
                jackFiles.add(input);
            } else if (input.isDirectory()) {
                jackFiles = JackAnalyzer.getJackFiles(input);
                if (jackFiles.size() == 0) {
                    throw new IllegalArgumentException("No jack files found");
                }
            }
            for (File file : jackFiles) {
                outputFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".xml";
                outputFile = new File(outputFilePath);
                CompilationEngine compilationEngine = new CompilationEngine(file, outputFile);
                compilationEngine.compileClass();
            }
        }

    }
}
