import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        String inputPath = args[0];
        File inputFile = new File(inputPath);
        if (inputFile.isFile()) {
            if (inputPath.endsWith(".vm")) {
                String outputFile = inputPath.replace("vm", "asm");
                VMTranslator translator = new VMTranslator(inputPath, outputFile);
            } else {
                throw new FileNotFoundException();
            }
        }
        else if (inputFile.isDirectory()) {
            File[] files = inputFile.listFiles((dir, name) -> name.endsWith(".vm"));
            if (files == null || files.length == 0) {
                throw new FileNotFoundException();
            }
           File firstFile= files[0];
            String outputFile = firstFile.getPath().replace("vm", "asm");
            VMTranslator translator = new VMTranslator(firstFile.getPath(), outputFile);

        }

    }

}

