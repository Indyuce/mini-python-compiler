package mini_python;

import mini_python.annotation.NotNull;
import mini_python.annotation.Nullable;

public class Objects {

    @NotNull
    public static <T> T requireNonNull(@Nullable T obj, @Nullable String message) {
        if (obj == null) throw new IllegalArgumentException(String.valueOf(message));
        return obj;
    }
}
