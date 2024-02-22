package mini_python.exception;

public class FunctionDelegatedError extends RuntimeException {
    public FunctionDelegatedError() {
        this("method delegated to other type");
    }

    public FunctionDelegatedError(String message) {
        super(message);
    }
}
