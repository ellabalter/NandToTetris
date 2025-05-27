import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    public BufferedReader reader;
    private String currentLine;

    public Parser(String filepath) throws IOException {
        reader = new BufferedReader(new FileReader(filepath));
        currentLine = reader.readLine();
    }

    public boolean hasMoreLines() throws IOException {
     return currentLine != null;
}

    public String advance() throws IOException {
        while ((currentLine = reader.readLine()) != null) {
            currentLine = currentLine.trim(); // Remove leading and trailing whitespace

            // Skip blank lines and comments
            if (!currentLine.isEmpty() && !currentLine.startsWith("//")) {
                return currentLine; // Return the valid line
            }
        }

        return null;
    }

    public InstructionType.Type instructionType() {
        currentLine = currentLine.trim();
        if (currentLine.startsWith("@")) {
            return InstructionType.Type.A_INSTRUCTION;
        }
        if (currentLine.contains("=") || currentLine.contains(";")) {
            return InstructionType.Type.C_INSTRUCTION;
        }
        if (currentLine.startsWith("(") && currentLine.endsWith(")")) {
            return InstructionType.Type.L_INSTRUCTION;
        }
        throw new RuntimeException("Unknown instruction type: " + currentLine);
    }

    public  String symbol() {
        if(instructionType() == InstructionType.Type.L_INSTRUCTION ){
            return currentLine.substring(1, currentLine.length()-1);
        }
        if(instructionType() == InstructionType.Type.A_INSTRUCTION){
            return currentLine.substring(1, currentLine.length());
        }
        throw new IllegalArgumentException("Unknown instruction type: " + currentLine);
    }
    public String dest() {
        System.out.println(currentLine);
        if(currentLine.contains("=")){
            return currentLine.split("=")[0];
        }
        return null;
    }

    public String comp() {
        if(currentLine.contains("=")){
            return currentLine.split("=")[1];
        }
        if(currentLine.contains(";")){
            return currentLine.split(";")[0];
        }
        return null;

    }

    public String jump() {
        if(currentLine.contains(";")){
            return currentLine.split(";")[1];
        }
        return null;

    }

}
