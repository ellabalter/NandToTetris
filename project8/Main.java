import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: java Main <input file/directory>");
        }
        String inputPath = args[0];
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            throw new IllegalArgumentException("File or directory does not exist: " + inputPath);
        }

        if (inputFile.isFile()) {
            // SINGLE FILE CASE
            if (!inputPath.endsWith(".vm")) {
                throw new IllegalArgumentException("Input file must be a .vm file.");
            }
            // Output file will be same name but .asm
            String outputFile = inputPath.replace(".vm", ".asm");
            // Single-file => typically no bootstrap needed, unless your spec demands it
            boolean bootstrap = false;

            // Create ONE code writer
            CodeWriter codeWriter = new CodeWriter(outputFile, bootstrap);
            // Set the file name for static references, e.g. fileName.index
            codeWriter.setFileName(inputFile.getName());

            // Create a single translator for this .vm file
            VMTranslator translator = new VMTranslator(inputPath, codeWriter);
            translator.translate();

            // Finally close the code writer
            codeWriter.close();

        } else {
            // DIRECTORY CASE
            File[] vmFiles = inputFile.listFiles((dir, name) -> name.endsWith(".vm"));
            if (vmFiles == null || vmFiles.length == 0) {
                throw new IllegalArgumentException("No .vm files found in directory: " + inputPath);
            }
            // Typically if multiple .vm files, we do want bootstrap
            // (some teachers let you always do bootstrap for directory input)
             boolean bootstrap = (vmFiles.length > 1);
            // Could also do: boolean bootstrap = true; // always bootstrap in directories

            // Name the output file <directoryName>.asm
            String outputFile = new File(inputFile, inputFile.getName() + ".asm").getPath();

            // Create ONE code writer for the entire directory
            CodeWriter codeWriter = new CodeWriter(outputFile, bootstrap);

            // If bootstrap is true, write Sys.init once at the top
            codeWriter.writeInit(bootstrap);

            // Translate each .vm file
            for (File vmFile : vmFiles) {
                codeWriter.setFileName(vmFile.getName());
                VMTranslator translator = new VMTranslator(vmFile.getAbsolutePath(), codeWriter);
                translator.translate();
            }

            // Close
            codeWriter.close();
        }
    }
}
