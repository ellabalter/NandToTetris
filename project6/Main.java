import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
      String inputFilePath = args[0];
        String outputFilePath = inputFilePath.replace(".asm", ".hack");
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        System.out.println("output file path: " + outputFilePath);
        HackAssembler assembler = new HackAssembler();
        assembler.assemble(inputFilePath, outputFilePath);

    }

}
