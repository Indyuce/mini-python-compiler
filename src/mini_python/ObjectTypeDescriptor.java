package mini_python;

import mini_python.annotation.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to generate offsets for the functions of a given type.
 *
 * @implNote There are no object-specific built-in functions or methods.
 *         All built-in functions or methods are either static or inherited
 *         from the 'object' type, which means we only need ONE descriptor
 *         for all types!
 *         <p>
 *         For this reason, all descriptors have the same structure. This
 *         does simplify a lot of things
 */
public class ObjectTypeDescriptor {

    /**
     * Methods are registered hard, as we don't allow method creation.
     * Custom structs would require additional abstraction
     */
    enum Method {
        __add__,
        __sub__,
        __mul__,
        __div__,
        __mod__,
        __eq__,
        __neq__,
        __lt__,
        __le__,
        __gt__,
        __ge__,
        __and__,
        __or__,
        __neg__,
        __not__,
        __print__;

        final static Map<Object, Method> BY_ID = new HashMap<>();
        final static Map<Object, Method> BY_ORDINAL = new HashMap<>();

        static {
            for (Method method : values()) {
                BY_ID.put(method.name(), method);
                BY_ORDINAL.put(method.ordinal(), method);
            }
        }

        public int ofs() {
            return ordinal() * 8;
        }

        @NotNull
        public static Method fromName(@NotNull String name) {
            return Validate.requireNonNull(BY_ID.get(name), "Could not find method from object type with id '" + name + "'");
        }

        @NotNull
        public static Method from(@NotNull Enum op) {
            return fromName(toId(op));
        }

        @NotNull
        private static String toId(@NotNull Enum op) {
            return "__" + op.name().substring(1) + "__";
        }
    }
}
