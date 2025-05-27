import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CompilationEngine {

    private FileWriter writer; // the output file
    private JackTokenizer token; // the input tokens
    private boolean isFirst; // are we in the first routine


    public CompilationEngine(File in, File outFile) throws IOException {
            writer = new FileWriter(outFile);
            token = new JackTokenizer(in);
            isFirst = true;
        if (in == null || !in.exists()) {
            throw new IllegalArgumentException("Input file must exist");
        }
        if (outFile == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }
    }

    // compiles a class
    public void compileClass() throws IOException {
            token.advance();
            writer.write("<class>\n");
            writer.write("<keyword> class </keyword>\n"); //class
            token.advance();
            writer.write("<identifier> " + token.identifier() + " </identifier>\n"); //name of class
            token.advance();
            writer.write("<symbol> { </symbol>\n"); // start of class
            compileClassVarDec(); //continue to compile fields and static variables
            compileSubRoutine(); //compiles the constructors, functions, methods
            writer.write("<symbol> } </symbol>\n"); // end of class
            writer.write("</class>\n");
            writer.close(); //end compilation

    }

    /*
     * Compiles a static varible declaration, or a field declration.
     */
    public void compileClassVarDec() throws IOException {
        token.advance();
            while (token.keyWord().equals("static") || token.keyWord().equals("field")) {
                writer.write("<classVarDec>\n");
                //field or static
                writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                token.advance();
                //either a variable type or and identifier from another class
                if (token.tokenType().equals("IDENTIFIER")) {
                    writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                }
                else {
                    writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                }
                token.advance();
                //name of static variable or field
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
                //checks if there are more variables
                if (token.symbol() == ',') {
                    writer.write("<symbol> , </symbol>\n");
                    token.advance();
                    writer.write(("<identifier> " + token.identifier() + " </identifier>\n"));
                    token.advance();
                }
                //end of field or static line
                writer.write("<symbol> ; </symbol>\n");
                token.advance();
                writer.write("</classVarDec>\n");
            }
            //checks if compile subRputine needs to be called and decrement index
            if (token.keyWord().equals("function") || token.keyWord().equals("method") || token.keyWord().equals("constructor")) {
               token.decrementIndex();

            }
        }

    /*
     * Compiles a complete method, function or constructor.
     */
    public void compileSubRoutine() throws IOException {
        boolean subRoutines = false;
        token.advance();
            if (token.symbol() == '}' && token.tokenType().equals("SYMBOL")) {
                return;
            }
            //checks if we should compile subRoutine, if it's the first routine, write <subroutineDec>

            if (token.keyWord().equals("function") || token.keyWord().equals("method") || token.keyWord().equals("constructor")) {
                subRoutines = true;
                if (isFirst){
                    isFirst = false;
                    writer.write("<subroutineDec>\n");
                }
                writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                token.advance();
            }
            //name of function/method/constructor
            if (token.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
            }
            // keyword
            else if (token.tokenType().equals("KEYWORD")) {
                writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                token.advance();
            }

            if (token.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
            }
            //( parameters )
            if (token.symbol() == '(') {
                writer.write("<symbol> ( </symbol>\n");
                writer.write("<parameterList>\n");
                compileParameterList();
                writer.write("</parameterList>\n");
                writer.write("<symbol> ) </symbol>\n");

            }
            compileSubroutineBody();
            //if there was a subRoutine close it
            if (subRoutines) {
                writer.write("</subroutineBody>\n");
                writer.write("</subroutineDec>\n");
                isFirst = true;
            }
            //recursively call the function
            compileSubRoutine();

        }


 //compiles paramater list not including ( and )
    public void compileParameterList()  throws IOException {
        token.advance();
        //paramater name
            while (!(token.tokenType().equals("SYMBOL") && token.symbol() == ')')) {
                if (token.tokenType().equals("IDENTIFIER")) {
                    writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                    token.advance();
                } else if (token.tokenType().equals("KEYWORD")) {
                    writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                    token.advance();
                }
                else if ((token.tokenType().equals("SYMBOL")) && (token.symbol() == ',')) {
                    writer.write("<symbol> , </symbol>\n");
                    token.advance();

                }
            }
    }

    //Compiles a subroutine's body.
    public void compileSubroutineBody () throws IOException {
        token.advance();
            if (token.symbol() == '{') {
                writer.write("<subroutineBody>\n");
                writer.write("<symbol> { </symbol>\n");
                token.advance();
            }
            while (token.keyWord().equals("var") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<varDec>\n");
                token.decrementIndex();
                compileVarDec();
                writer.write("</varDec>\n");
            }
            writer.write("<statements>\n");
            compileStatements();
            writer.write("</statements>\n");
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");

    }

    // compiles var declerations
    public void compileVarDec() throws IOException {
        token.advance();
            if (token.keyWord().equals("var") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<keyword> var </keyword>\n");
                token.advance();
            }
            if (token.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
            }
            else if (token.tokenType().equals("KEYWORD")) {
                writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                token.advance();
            }
            if (token.tokenType().equals("IDENTIFIER")) {
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
            }
            if ((token.tokenType().equals("SYMBOL")) && (token.symbol() == ',')) {
                writer.write("<symbol> , </symbol>\n");
                token.advance();
                writer.write(("<identifier> " + token.identifier() + " </identifier>\n"));
                token.advance();
            }
            if ((token.tokenType().equals("SYMBOL")) && (token.symbol() == ';')) {
                writer.write("<symbol> ; </symbol>\n");
                token.advance();
            }

    }

    //Compiles a sequence of statements.

    public void compileStatements() throws IOException {
            if (token.symbol() == '}' && (token.tokenType().equals("SYMBOL"))) {
                return;
            }
            else if (token.keyWord().equals("do") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<doStatement>\n");
                compileDo();
                writer.write(("</doStatement>\n"));
            }
            else if (token.keyWord().equals("let") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<letStatement>\n");
                compileLet();
                writer.write(("</letStatement>\n"));
            }
            else if (token.keyWord().equals("if") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<ifStatement>\n");
                compileIf();
                writer.write(("</ifStatement>\n"));
            }
            else if (token.keyWord().equals("while") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<whileStatement>\n");
                compileWhile();
                writer.write(("</whileStatement>\n"));
            }
            else if (token.keyWord().equals("return") && (token.tokenType().equals("KEYWORD"))) {
                writer.write("<returnStatement>\n");
                compileReturn();
                writer.write(("</returnStatement>\n"));
            }
            token.advance();
            compileStatements();
    }

    // compile do statement
    public void compileDo() throws IOException {

            if (token.keyWord().equals("do")) {
                writer.write("<keyword> do </keyword>\n");
            }
            compileCall();
            token.advance();
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");

    }

    // Compiles a call function statement.
    private void compileCall()  throws IOException {
        token.advance();
            writer.write("<identifier> " + token.identifier() + " </identifier>\n");
            token.advance();
            if ((token.tokenType().equals("SYMBOL")) && (token.symbol() == '.')) {
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                token.advance();
                writer.write("<identifier> " + token.identifier() + " </identifier>\n");
                token.advance();
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                writer.write("<expressionList>\n");
                compileExpressionList();
                writer.write("</expressionList>\n");
                token.advance();
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            }
            else if (token.tokenType().equals("SYMBOL") && token.symbol() == '(') {
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                writer.write("<expressionList>\n");
                compileExpressionList();
                writer.write("</expressionList>\n");
                token.advance();
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            }
    }

    /*
     * Compiles a let statement.
     */
    public void compileLet() throws IOException {
            writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
            token.advance();
            writer.write("<identifier> " + token.identifier() + " </identifier>\n");
            token.advance();
            if ((token.tokenType().equals("SYMBOL")) && (token.symbol() == '[')) {
                writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                compileExpression();
                token.advance();
                if ((token.tokenType().equals("SYMBOL")) && ((token.symbol() == ']'))) {
                    writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                }
                token.advance();
            }
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            compileExpression();
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            token.advance();

    }

    /*
     * Compiles a while statement.
     */
    public void compileWhile() throws IOException {

            writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
            token.advance();
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            compileExpression();
            token.advance();
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            token.advance();
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
            writer.write("<statements>\n");
            compileStatements();
            writer.write("</statements>\n");
            writer.write("<symbol> " + token.symbol() + " </symbol>\n");
    }

    /*
     * Compiles a return statement.
     */
    public void compileReturn() throws IOException {

            writer.write("<keyword> return </keyword>\n");
            token.advance();
            if (!((token.tokenType().equals("SYMBOL") && token.symbol() == ';'))) {
                token.decrementIndex();
                compileExpression();
            }
            if (token.tokenType().equals("SYMBOL") && token.symbol() == ';') {
                writer.write("<symbol> ; </symbol>\n");
            }

    }

    /*
     * Compiles an if statement, possibly with a trailing else clause.
     */
    public void compileIf() throws IOException {
            writer.write("<keyword> if </keyword>\n");
            token.advance();
            writer.write("<symbol> ( </symbol>\n");
            compileExpression();
            writer.write("<symbol> ) </symbol>\n");
            token.advance();
            writer.write("<symbol> { </symbol>\n");
            token.advance();
            writer.write("<statements>\n");
            compileStatements();
            writer.write("</statements>\n");
            writer.write("<symbol> } </symbol>\n");
            token.advance();
            if (token.tokenType().equals("KEYWORD") && token.keyWord().equals("else")) {
                writer.write("<keyword> else </keyword>\n");
                token.advance();
                writer.write("<symbol> { </symbol>\n");
                token.advance();
                writer.write("<statements>\n");
                compileStatements();
                writer.write("</statements>\n");
                writer.write("<symbol> } </symbol>\n");
            }
            else {
                token.decrementIndex();
            }
    }

    // Compiles an expression.
    public void compileExpression() throws IOException{
            writer.write("<expression>\n");
            compileTerm();
            while (true) {
                token.advance();
                if (token.tokenType().equals("SYMBOL") && token.isOperation()) {
                    if (token.symbol() == '<') {
                        writer.write("<symbol> &lt; </symbol>\n");
                    }
                    else if (token.symbol() == '>') {
                        writer.write("<symbol> &gt; </symbol>\n");
                    }
                    else if (token.symbol() == '&') {
                        writer.write("<symbol> &amp; </symbol>\n");
                    }
                    else {
                        writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                    }
                    compileTerm();
                }
                else {
                    token.decrementIndex();
                    break;
                }
            }
            writer.write("</expression>\n");
    }

    // Compiles a term.

    public void compileTerm() throws IOException {
            writer.write("<term>\n");
            token.advance();
            if (token.tokenType().equals("IDENTIFIER")) {
                String prev = token.identifier();
                token.advance();
                if (token.tokenType().equals("SYMBOL") && token.symbol() == '[') {
                    writer.write("<identifier> " + prev + " </identifier>\n");
                    writer.write("<symbol> [ </symbol>\n");
                    compileExpression();
                    token.advance();
                    writer.write("<symbol> ] </symbol>\n");
                }
                else if (token.tokenType().equals("SYMBOL") && (token.symbol() == '(' || token.symbol() == '.')) {
                    token.decrementIndex();
                    token.decrementIndex();
                    compileCall();
                }
                else {
                    writer.write("<identifier> " + prev + " </identifier>\n");
                    token.decrementIndex();
                }
            }
            else {
                if (token.tokenType().equals("INT_CONST")) {
                  writer.write("<integerConstant> " + token.intVal() + " </integerConstant>\n");
                }
                else if (token.tokenType().equals("STRING_CONST")) {
                    writer.write("<stringConstant> " + token.stringVal() + " </stringConstant>\n");
                }
                else if (token.tokenType().equals("KEYWORD") && (token.keyWord().equals("this") || token.keyWord().equals("null")
                        || token.keyWord().equals("false") || token.keyWord().equals("true"))) {
                    writer.write("<keyword> " + token.keyWord() + " </keyword>\n");
                }
                else if (token.tokenType().equals("SYMBOL") && token.symbol() == '(') {
                    writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                    compileExpression();
                    token.advance();
                    writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                }
                else if (token.tokenType().equals("SYMBOL") && (token.symbol() == '-' || token.symbol() == '~')) {
                    writer.write("<symbol> " + token.symbol() + " </symbol>\n");
                    compileTerm();
                }
            }
            writer.write("</term>\n");

    }

    /*
     * Compiles a list of expressions.
     */
    public void compileExpressionList() throws IOException {
        token.advance();
        if (token.symbol() == ')' && token.tokenType().equals("SYMBOL")) {
            token.decrementIndex();
        }
        else {
            token.decrementIndex();
            compileExpression();
        }
        while (true) {
            token.advance();
            if (token.tokenType().equals("SYMBOL") && token.symbol() == ',') {

                    writer.write("<symbol> , </symbol>\n");

                compileExpression();
            }
            else {
                token.decrementIndex();
                break;
            }
        }
    }

}
