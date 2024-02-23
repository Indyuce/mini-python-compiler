package mini_python.exception;

public class FunctionDelegatedError extends Error {
    public FunctionDelegatedError() {
        this("method delegated to other type");
    }

    public FunctionDelegatedError(String message) {
        super(message);
    }
}
