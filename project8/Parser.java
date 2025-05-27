import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private BufferedReader reader;
    private String currentLine;

    public Parser(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
    }

    public boolean hasMoreLines() throws IOException {
        return currentLine != null;
    }

    public void advance() throws IOException {
        currentLine = reader.readLine();
        while (currentLine != null) {
            currentLine = currentLine.trim();
            if (!currentLine.isEmpty() && !currentLine.startsWith("//")) {
                break;
            }
            currentLine = reader.readLine();
            currentLine = currentLine.trim();

        }
    }

    public String commandType() throws IOException {
        if (currentLine.startsWith("push")) {
            return "C_PUSH";
        }
        if (currentLine.startsWith("pop")) {
            return "C_POP";
        }
        if (currentLine.startsWith("label")) {
            return "C_LABEL";
        }
        if (currentLine.startsWith("goto")) {
            return "C_GOTO";
        }
        if (currentLine.startsWith("if-goto")) {
            return "C_IF";
        }
        if (currentLine.startsWith("function")) {
            return "C_FUNCTION";
        }
        if (currentLine.startsWith("call")) {
            return "C_CALL";
        }
        if (currentLine.startsWith("return")) {
            return "C_RETURN";
        }
        return "C_ARITHMETIC"; // Default case for arithmetic commands
    }

    public String arg1() throws IOException{
        String currentCommand = commandType();
        if (currentCommand.equals("C_ARITHMETIC")) {
            return currentLine.split(" ")[0];
        }

        String[] parts = currentLine.split(" ");
        return parts[1];
    }

    public int arg2() throws IOException{
        String[] parts = currentLine.split("\\s+");
        if (parts.length > 2) {
            try {
                return Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format in command: " + currentLine, e);
            }
        }
        throw new IllegalArgumentException("Missing argument for command: " + currentLine);
    }
    public static String noComments(String str) {
        int pos = str.indexOf("//");
        if(pos != -1) {
            str = str.substring(0, pos);
        }

        return str;
    }
}



