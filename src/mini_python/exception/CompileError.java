package mini_python.exception;

public class CompileError extends Error {
    public CompileError(String message) {
        super(message);
    }

    public CompileError(Exception exception) {
        super(exception);
    }
}
