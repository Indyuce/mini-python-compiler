package mini_python.exception;

public class MethodNotDefinedError extends Error {
    public MethodNotDefinedError() {
        this("method not defined");
    }

    public MethodNotDefinedError(String message) {
        super(message);
    }
}
