import java.io.IOException;

public class VMTranslator {
    private final Parser parser;
    private final CodeWriter codeWriter;

    public VMTranslator(String inputFilePath, CodeWriter codeWriter) throws IOException {
        // For each file, create a fresh Parser
        this.parser = new Parser(inputFilePath);
        // Re-use the single CodeWriter passed from Main
        this.codeWriter = codeWriter;
    }

    // Translate the entire .vm file
    public void translate() throws IOException {
        // Advance to the first command
        if (parser.hasMoreLines()) {
            parser.advance();
        }

        // Keep going until we run out of lines
        while (parser.hasMoreLines()) {
            String commandType = parser.commandType();
            switch (commandType) {
                case "C_PUSH":
                case "C_POP":
                    String arg1 = parser.arg1();  // segment
                    int index = parser.arg2();    // index
                    codeWriter.writePushPop(commandType, arg1, index);
                    break;

                case "C_ARITHMETIC":
                    codeWriter.writeArithmetic(parser.arg1());
                    break;

                case "C_LABEL":
                    codeWriter.writeLabel(parser.arg1());
                    break;

                case "C_GOTO":
                    codeWriter.writeGoto(parser.arg1());
                    break;

                case "C_IF":
                    codeWriter.writeIf(parser.arg1());
                    break;

                case "C_FUNCTION":
                    codeWriter.writeFunction(parser.arg1(), parser.arg2());
                    break;

                case "C_CALL":
                    codeWriter.writeCall(parser.arg1(), parser.arg2());
                    break;

                case "C_RETURN":
                    codeWriter.writeReturn();
                    break;
            }
            parser.advance();
        }
    }
}
