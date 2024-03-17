package mini_python;

import mini_python.exception.CompileException;
import mini_python.exception.TypeException;

public class Utils {
    public static CompileException typeException(Location loc, String message) {
        return new TypeException(loc, message, Main.lines, Main.file);
    }
}
