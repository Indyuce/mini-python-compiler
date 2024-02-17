package mini_python.exception;

public class NotImplementedError extends Error {
    public NotImplementedError() {
        this("not implemented");
    }

    public NotImplementedError(String message) {
        super(message);
    }
}
