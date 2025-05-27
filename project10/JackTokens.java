import java.util.List;
//array with all the keywords
public interface JackTokens {
    List<String> keyWords = List.of(
            "class", "constructor", "function", "method",
            "field", "static", "var", "int", "char",
            "boolean", "void", "true", "false", "null",
            "this", "do", "if", "else", "while", "return", "let"
    );

    String operations = "+-/*&|<>=";
    String symbols = "{}()[].,;+-/*&|<>=-~";
}


