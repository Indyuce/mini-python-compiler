package mini_python.exception;

import mini_python.Location;

import java.util.List;

public class TypeException extends CompileException {
    public TypeException(Location loc, String message, List<String> lines, String file) {
        super(loc, message, lines, file);
    }
}
