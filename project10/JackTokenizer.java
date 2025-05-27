import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JackTokenizer {
    private BufferedReader reader;
    private ArrayList<String> tokens;
    private String TokenType;
    private int curIndex;
    private boolean inFirstToken;
    private String curToken;


     // Opens the input jack file and gets ready to tokenize it.

    public JackTokenizer(File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        String jackCode = "";
        String line;

        while ((line = reader.readLine()) != null) {
            // Skip empty lines and comments
            // Handle // comments
            if (line.contains("//")) {
                line = line.substring(0, line.indexOf("//"));
            }

// Handle /* */ comments
            if (line.contains("/*")) {
                line = line.substring(0, line.indexOf("/*"));
            }

// Skip if line is empty after removing comments
            if (line.trim().isEmpty() || line.trim().endsWith("*/") || line.startsWith("*") || line.startsWith(" *")) {
                continue;
            }

            // Append non-empty, non-comment lines
            jackCode += (line.trim());
        }


        // add all the tokens.
        tokens = new ArrayList<String>();
        while (jackCode.length() > 0) {
            while (jackCode.charAt(0) == ' ') {
                jackCode = jackCode.substring(1);
            }


            for (int i = 0; i < JackTokens.keyWords.size(); i++) {
                //keyword token
                if (jackCode.startsWith(JackTokens.keyWords.get(i).toString())) {
                    String keyword = JackTokens.keyWords.get(i).toString();
                    tokens.add(keyword);
                    jackCode = jackCode.substring(keyword.length());
                }

            }
            // symbol token
            if (JackTokens.symbols.contains(jackCode.substring(0, 1))) {
                char symbol = jackCode.charAt(0);
                tokens.add(Character.toString(symbol));
                jackCode = jackCode.substring(1);

            }
            // int token
            else if (Character.isDigit(jackCode.charAt(0))) {
                String value = jackCode.substring(0, 1);
                jackCode = jackCode.substring(1);
                while (Character.isDigit(jackCode.charAt(0))) {
                    value += jackCode.substring(0, 1);
                    jackCode = jackCode.substring(1);
                }
                tokens.add(value);

            }

            // string token
            else if (jackCode.substring(0, 1).equals("\"")) {
                jackCode = jackCode.substring(1);
                String str = "\"";
                while ((jackCode.charAt(0) != '\"')) {
                    str += jackCode.charAt(0);
                    jackCode = jackCode.substring(1);
                }
                str = str + "\"";
                tokens.add(str);
                jackCode = jackCode.substring(1);

            }

            // identifier token
            else if (Character.isLetter(jackCode.charAt(0)) || (jackCode.substring(0, 1).equals("_"))) {
                String thisIdentifier = jackCode.substring(0, 1);
                jackCode = jackCode.substring(1);
                while ((Character.isLetter(jackCode.charAt(0))) || (jackCode.substring(0, 1).equals("_"))) {
                    thisIdentifier += jackCode.substring(0, 1);
                    jackCode = jackCode.substring(1);
                }
                tokens.add(thisIdentifier);
            }

            inFirstToken = true;
           curIndex = 0;
        }
    }

    // check if there are more tokens in the input
    public boolean hasMoreTokens() {
        if (curIndex < tokens.size()) {
            return true;
        }
        return false;
    }

    // moves to the next token
    public void advance() {
        if (hasMoreTokens()) {
            if (!inFirstToken) {
                curIndex++;
            } else {
                inFirstToken = false;
            }
            curToken = tokens.get(curIndex);
            TokenType = tokenType();
        }

    }


    // returns token type
    public String tokenType() {
        if (JackTokens.keyWords.contains(curToken)) {
            TokenType = "KEYWORD";
        } else if (JackTokens.symbols.contains(curToken)) {
            TokenType = "SYMBOL";
        } else if (Character.isDigit(curToken.charAt(0))) {
            TokenType = "INT_CONST";
        } else if (curToken.substring(0, 1).equals("\"")) {
            TokenType = "STRING_CONST";
        } else {
            TokenType = "IDENTIFIER";
        }
        return TokenType;
    }


    // keyword
    public String keyWord() {
        return curToken;
    }

    //returns symbol
    public char symbol() {
        return curToken.charAt(0);
    }

    //returns identifier
    public String identifier() {
        return curToken;
    }

    // returns int value
    public int intVal() {
        return Integer.parseInt(curToken);
    }

    // returns string value
    public String stringVal() {
        return curToken.substring(1, curToken.length() - 1);
    }


     public void decrementIndex() {
         if (curIndex > 0) {
             curIndex--;
         }
     }

    public boolean isOperation() {
        for (int i = 0; i < JackTokens.operations.length(); i++) {
            if (JackTokens.operations.charAt(i) == symbol()) {
                return true;
            }
        }
        return false;
    }
}

