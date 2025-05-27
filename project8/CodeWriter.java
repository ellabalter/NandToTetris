import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    private final BufferedWriter writer;
    private String fileName;
    private int labelCounter = 0;
    public boolean bootsrtrap;

    public CodeWriter(String outputFileName, boolean bootsrtrap) throws IOException {
        // Open the file in OVERWRITE mode, not append mode
        writer = new BufferedWriter(new FileWriter(outputFileName));
        this.bootsrtrap = bootsrtrap;

        // Just extract the filename prefix (for static variables)
        String[] parts = outputFileName.split("\\.");
        fileName = parts[0];
    }

        public void setFileName(String fileName) {
            this.fileName = fileName.substring(0, fileName.indexOf("."));
        }

        public void writeArithmetic(String command) throws IOException {
            switch (command) {
                case "add":
                    writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=D+M\n");
                    break;
                case "sub":
                    writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n");
                    break;
                case "neg":
                    writer.write("@SP\nA=M-1\nM=-M\n");
                    break;
                case "eq":
                case "gt":
                case "lt":
                    String jump = command.equals("eq") ? "JEQ" : command.equals("gt") ? "JGT" : "JLT";
                    String labelTrue = "TRUE_" + labelCounter;
                    String labelEnd = "END_" + labelCounter;
                    labelCounter++;
                    writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n@" + labelTrue + "\nD;" + jump + "\n@SP\nA=M-1\nM=0\n@" + labelEnd + "\n0;JMP\n(" + labelTrue + ")\n@SP\nA=M-1\nM=-1\n(" + labelEnd + ")\n");
                    break;
                case "and":
                    writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=D&M\n");
                    break;
                case "or":
                    writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=D|M\n");
                    break;
                case "not":
                    writer.write("@SP\nA=M-1\nM=!M\n");
                    break;
            }
            //writer.flush();
        }

        public void writePushPop(String command, String segment, int index) throws IOException {
            if (command.equals("C_PUSH")) {
                //sp++;
                //System.out.println(sp);
                switch (segment) {
                    case "static":
                        writer.write("@" + fileName + "." + index + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "local":
                        writer.write("@LCL\nD=M\n@" + index + "\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "argument":
                        writer.write("@ARG\nD=M\n@" + index + "\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "this":
                        writer.write("@THIS\nD=M\n@" + index + "\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "that":
                        writer.write("@THAT\nD=M\n@" + index + "\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "constant":
                        writer.write("@" + index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "temp":
                        writer.write("@" + (5 + index) + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "pointer":
                        if (index == 0) {
                            writer.write("@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        } else if (index == 1) {
                            writer.write("@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        }
                        break;

                }

            } else if (command.equals("C_POP")) {
                //sp--;
                //System.out.println(sp);
                switch (segment) {
                    case "static":
                        writer.write("@SP\nAM=M-1\nD=M\n@" + fileName + "." + index + "\nM=D\n");
                        break;
                    case "local":
                        writer.write("@LCL\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n");
                        break;
                    case "argument":
                        writer.write("@ARG\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n");
                        break;
                    case "this":
                        writer.write("@THIS\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n");
                        break;
                    case "that":
                        writer.write("@THAT\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n");
                        break;
                    case "temp":
                        writer.write("@R" + (5 + index) + "\nD=A\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n");
                        break;
                    case "pointer":
                        if (index == 0) {
                            writer.write("@SP\nAM=M-1\nD=M\n@THIS\nM=D\n");
                        } else if (index == 1) {
                            writer.write("@SP\nAM=M-1\nD=M\n@THAT\nM=D\n");

                        }
                        break;
                }


            }
        }

        public void writeLabel(String label) throws IOException {
            writer.write("(" + label + ")\n");

        }

        public void writeGoto(String label) throws IOException {
           writer.write("@" + label + "\n" + "0;JMP\n");
        }

        public void writeIf(String label) throws IOException {
            writer.write("@SP\n");
            writer.write("AM=M-1\n");
            writer.write("D=M\n");
            writer.write("@" + label + "\n");
            writer.write("D;JNE\n");
            //sp--;
            //System.out.println(sp);
        }

        public void writeFunction(String functionName, int nVars) throws IOException {
            writeLabel(functionName);
            for (int i = 0; i < nVars; i++) {
                writePushPop("push", "constant", 0);

            }
          }

        public void writeCall(String functionName, int nArgs) throws IOException {
            String returnLabel = functionName + "$ret." + labelCounter++;
            writer.write("@" + returnLabel + "\n");
            writer.write("D=A\n");
            writer.write("@SP\n");
            writer.write("A=M\n");
            writer.write("M=D\n");
            writer.write("@SP\n");
            writer.write("M=M+1\n");

            // Push LCL, ARG, THIS, THAT
            for (String seg : new String[]{"LCL", "ARG", "THIS", "THAT"}) {
                writer.write("@" + seg + "\n");
                writer.write("D=M\n");
                writer.write("@SP\n");
                writer.write("A=M\n");
                writer.write("M=D\n");
                writer.write("@SP\n");
                writer.write("M=M+1\n");
            }

            // Reposition ARG (SP - 5 - nArgs)
            writer.write("@SP\n");
            writer.write("D=M\n");
            writer.write("@5\n");
            writer.write("D=D-A\n");
            writer.write("@" + nArgs + "\n");
            writer.write("D=D-A\n");
            writer.write("@ARG\n");
            writer.write("M=D\n");

            // Reposition LCL (SP)
            writer.write("@SP\n");
            writer.write("D=M\n");
            writer.write("@LCL\n");
            writer.write("M=D\n");

            // Goto function
            writeGoto(functionName);

            // Declare return label
            writeLabel(returnLabel);

        }

        public void writeReturn() throws IOException {
            writer.write("@LCL\n" +
                    "D=M\n" +
                    "@R13\n" +
                    "M=D\n" +

                    "@5\n" +         //get return address
                    "A=D-A\n" +
                    "D=M\n" +
                    "@R14\n" +
                    "M=D\n" +

                    "@SP\n" +      //put return value for caller
                    "AM=M-1\n" +
                    "D=M\n" +
                    "@ARG\n" +
                    "A=M\n" +
                    "M=D\n" +

                    "@ARG\n" +        // SP = ARG + 1
                    "D=M+1\n" +
                    "@SP\n" +
                    "M=D\n" +

                    "@R13\n" +    // Restore THAT,THIS..
                    "A=M-1\n" +
                    "D=M\n" +
                    "@THAT\n" +
                    "M=D\n" +

                    "@R13\n" +
                    "D=M\n" +
                    "@2\n" +
                    "A=D-A\n" +
                    "D=M\n" +
                    "@THIS\n" +
                    "M=D\n" +

                    "@R13\n" +
                    "D=M\n" +
                    "@3\n" +
                    "A=D-A\n" +
                    "D=M\n" +
                    "@ARG\n" +
                    "M=D\n" +

                    "@R13\n" +
                    "D=M\n" +
                    "@4\n" +
                    "A=D-A\n" +
                    "D=M\n" +
                    "@LCL\n" +
                    "M=D\n" +

                    "@R14\n" +     // Jump to the return address
                    "A=M\n" +
                    "0;JMP\n");

        }

        public void close() throws IOException {
            writer.close();
        }

        public void writeInit(boolean bootstrap) throws IOException {
            if (bootstrap) {
                // SP=256
                writer.write("@256\nD=A\n@SP\nM=D\n");
                // Call Sys.init
                writeCall("Sys.init", 0);
            }
        }

    public void flush() throws IOException {
            writer.flush();
        }
    }

