package mini_python.exception;

import mini_python.Location;

public class TypeError extends CompileError {
    public TypeError(Location loc, String message) {
        super(loc, message);
    }
}
