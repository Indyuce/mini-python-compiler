package mini_python.annotation;

/**
 * Indicates that a method call will kill a
 * specific set of caller saved registers. It is
 * therefore useless to push the rest of the caller-saved registers on the stack
 * before the call and pop them afterward.
 */
public @interface Kills {
    public String[] reg();
}
