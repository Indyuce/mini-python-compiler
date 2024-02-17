package mini_python;

import mini_python.annotation.NotNull;
import mini_python.annotation.Nullable;

public class Validate {

    public static void notNull(@Nullable Object obj, @Nullable String message) {
        if (obj == null) throw new IllegalArgumentException(String.valueOf(message));
    }

    public static <T> void isInstance(@NotNull Class<T> clazz, @NotNull Object obj, @Nullable String message) {
        Validate.notNull(clazz, "Class cannot be null");
        Validate.notNull(obj, "Object cannot be null");

        if (!clazz.isInstance(obj)) throw new IllegalArgumentException(String.valueOf(message));
    }

    @NotNull
    public static <T> T requireNonNull(@Nullable T obj, @Nullable String message) {
        if (obj == null) throw new IllegalArgumentException(String.valueOf(message));
        return obj;
    }
}
