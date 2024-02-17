package mini_python;

import mini_python.annotation.BuiltinMethod;
import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * There are no object-specific built-in functions or methods.
 * All built-in functions or methods are either static or inherited
 * from the 'object' type, which means we only need ONE descriptor
 * for all types!
 * <p>
 * For this reason, all descriptors have the same structure. This
 * does simplify a lot of things
 */
public abstract class Type {
    public static final Type
            NONE = new none(),
            BOOL = new bool(),
            INT = new int64(),
            STRING = new string(),
            LIST = new list();


    public abstract int ofs();

    public abstract String name();

    public abstract void staticConstants(X86_64 x86);

    @BuiltinMethod(ofs = 0)
    public abstract void __add__(X86_64 x86);

    @BuiltinMethod(ofs = 1)
    public abstract void __sub__(X86_64 x86);

    @BuiltinMethod(ofs = 2)
    public abstract void __mul__(X86_64 x86);

    @BuiltinMethod(ofs = 3)
    public abstract void __div__(X86_64 x86);

    @BuiltinMethod(ofs = 4)
    public abstract void __mod__(X86_64 x86);

    @BuiltinMethod(ofs = 5)
    public abstract void __eq__(X86_64 x86);

    @BuiltinMethod(ofs = 6)
    public abstract void __neq__(X86_64 x86);

    @BuiltinMethod(ofs = 7)
    public abstract void __lt__(X86_64 x86);

    @BuiltinMethod(ofs = 8)
    public abstract void __le__(X86_64 x86);

    @BuiltinMethod(ofs = 9)
    public abstract void __gt__(X86_64 x86);

    @BuiltinMethod(ofs = 10)
    public abstract void __ge__(X86_64 x86);

    @BuiltinMethod(ofs = 11)
    public abstract void __and__(X86_64 x86);

    @BuiltinMethod(ofs = 12)
    public abstract void __or__(X86_64 x86);

    @BuiltinMethod(ofs = 13)
    public abstract void __neg__(X86_64 x86);

    @BuiltinMethod(ofs = 14)
    public abstract void __not__(X86_64 x86);

    /**
     * When calling this method, address of type descriptor array
     * is located in %rdi
     */
    public void register(X86_64 x86) {
        final Type found = REGISTERED.put(ofs(), this);
        if (found != null) throw new CompileError("registered type with duplicate id " + ofs());

        // Write methods
        for (Method method : this.getClass().getDeclaredMethods()) {
            final BuiltinMethod annot2 = method.getAnnotation(BuiltinMethod.class);
            if (annot2 == null) continue;

            // Label method in .text
            x86.label(asmId(this, annot2, method));
            try {
                method.invoke(x86);
            } catch (Exception exception) {
                throw new CompileError("could not compile method " + method.getName() + " from type " + name() + ": " + exception.getMessage());
            }
        }

        // Static constants if needed
        staticConstants(x86);

        // Write type descriptor in heap


        // Write address to type descriptor in type descriptor array

    }

    public static BuiltinMethod fromName(Enum element) {
        for (Method method : Type.class.getDeclaredMethods()) {
            final BuiltinMethod annot = method.getAnnotation(BuiltinMethod.class);
            if (annot != null && method.getName().substring(2, method.getName().length() - 2).equals(element.name().toLowerCase().substring(1)))
                return annot;
        }
        throw new CompileError("could not find method corresponding to operator " + element.name());
    }

    @NotNull
    private static String asmId(Type type, BuiltinMethod functionAnnot, Method function) {
        return type.name() + function.getName();
    }

    /**
     * Used to make sure that there are no types compiled
     * with same numerical identifier.
     */
    private static final Map<Integer, Type> REGISTERED = new HashMap<>();
}
