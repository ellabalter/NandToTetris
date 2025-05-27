import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private BufferedReader reader;
    private String currentLine;
    public Parser(String filePath)  throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
    }
        public boolean hasMoreLines() throws IOException {
            return currentLine != null;

        }
        public void advance() throws IOException {
        currentLine = reader.readLine();
            while (currentLine!=null) {
                currentLine = currentLine.trim();
                if (!currentLine.isEmpty() && !currentLine.startsWith("//")) {
                    break;
                }
                currentLine = reader.readLine();
            }


        }
        public String commandType() throws IOException {
            if (currentLine.startsWith("push")) {
                return "C_PUSH";
            }
            if (currentLine.startsWith("pop")) {
                return "C_POP";
            }
                return "C_ARITHMETIC";
        }
        public String arg1() throws IOException {
        String currentCommand = commandType();
        if (currentCommand.equals("C_ARITHMETIC")) {
            return currentLine;
        }
            String[] parts = currentLine.split(" ");
            return parts[1];
        }
        public int arg2(){
            String[] parts = currentLine.split(" ");
            int num = Integer.parseInt(parts[2]);
            return num;
        }





}
