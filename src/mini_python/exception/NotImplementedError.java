package mini_python.exception;

@Deprecated
public class NotImplementedError extends Error {
    public NotImplementedError() {
        this("not implemented");
    }

    public NotImplementedError(String message) {
        super(message);
    }
}
