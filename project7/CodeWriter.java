    import java.io.BufferedWriter;
    import java.io.FileWriter;
    import java.io.IOException;

    public class CodeWriter {
            BufferedWriter writer;
            private String fileName;
            private int labelCounter = 0;

            public CodeWriter(String outputFileName) throws IOException {
                writer = new BufferedWriter(new FileWriter(outputFileName));
                String[] parts = outputFileName.split("\\.");
                fileName = parts[0];
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
            }

            public void writePushPop(String command, String segment, int index) throws IOException {
                if (command.equals("C_PUSH")) {
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
                            if(index == 0){
                                writer.write("@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        }
                            else if (index == 1){
                                writer.write("@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                            }
                            break;
                    }
                } else if (command.equals("C_POP")) {
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
                            if(index ==0 ) {
                                writer.write("@SP\nAM=M-1\nD=M\n@THIS\nM=D\n");
                            }
                            else if (index == 1) {
                                writer.write("@SP\nAM=M-1\nD=M\n@THAT\nM=D\n");

                            }
                            break;
                    }
                }
            }

            public void close() throws IOException {
                writer.close();
            }
        }

