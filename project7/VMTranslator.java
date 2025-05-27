import java.io.IOException;

public class VMTranslator {
    public VMTranslator(String inputFilePath, String outputFilePath) throws IOException {
        Parser parser=new Parser(inputFilePath);
        CodeWriter codeWriter = new CodeWriter(outputFilePath);
        parser.advance();
        while (parser.hasMoreLines()){
            String arg1 = parser.arg1();
            String command = parser.commandType();
            if (command.equals("C_PUSH") || command.equals("C_POP")){
                int index = parser.arg2();
                codeWriter.writePushPop(command, arg1, index);
            }
            else if (command.equals("C_ARITHMETIC")){
                codeWriter.writeArithmetic(arg1);
            }
            parser.advance();
        }
        codeWriter.close();
    }

}

