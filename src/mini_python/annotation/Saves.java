package mini_python.annotation;

/**
 * Indicates that a method call will not kill a
 * specific set of caller saved registers. It is
 * therefore useless to push them on the stack
 * before the call and pop them afterward.
 */
public @interface Saves {
    public String[] reg();
}
