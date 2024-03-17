package mini_python.exception;

import mini_python.Location;

public class TypeException extends CompileException {
    public TypeException(Location loc, String message) {
        super(loc, message);
    }
}
