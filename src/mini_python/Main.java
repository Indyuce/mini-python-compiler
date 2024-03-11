package mini_python;

import mini_python.exception.CompileError;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    static boolean parse_only = false;
    static boolean stack_traces = false;
    static boolean type_only = false;
    static boolean debug = false;
    public static String file = null;
    public static List<String> lines = null;

    static void usage() {
        System.err.println("minipython [--parse-only] [--type-only] file.py");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        for (String arg : args)
            if (arg.equals("--parse-only")) parse_only = true;
            else if (arg.equals("--traces")) stack_traces = true;
            else if (arg.equals("--type-only")) type_only = true;
            else if (arg.equals("--debug")) {
                debug = true;
                Typing.debug = true;
                Compile.debug = true;
            } else {
                if (file != null) usage();
                if (!arg.endsWith(".py")) usage();
                file = arg;
            }
        if (file == null) file = "test.py";

        java.io.Reader reader = new java.io.FileReader(file);
        lines = Files.readAllLines(Paths.get(file)); // save lines for debug
        Lexer lexer = new MyLexer(reader);
        MyParser parser = new MyParser(lexer);
        try {
            File f = (File) parser.parse().value;
            if (parse_only) System.exit(0);
            TFile tf = Typing.file(f);
            if (type_only) System.exit(0);
            X86_64 asm = Compile.file(tf);
            String file_s = file.substring(0, file.length() - 3) + ".s";
            asm.printToFile(file_s);
        }

        // Nicely display
        catch (CompileError error) {
            if (stack_traces) error.printStackTrace();
            else error.printError();
            System.exit(1);
        }

        // Any other error is not expected
        catch (Throwable throwable) {
            System.out.println("!! Internal error !!");
            throwable.printStackTrace();
            System.exit(1);
        }
    }
}
