package mini_python;

import mini_python.annotation.Builtin;
import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static final Type NONE = new none(), BOOL = new bool(), INT = new int64(), STRING = new string(), LIST = new list();


    public abstract int getOffset();

    public abstract String name();

    public abstract void staticConstants(TVisitor v);

    @Builtin
    public abstract void __add__(TVisitor v);

    @Builtin
    public abstract void __sub__(TVisitor v);

    @Builtin
    public abstract void __mul__(TVisitor v);

    @Builtin
    public abstract void __div__(TVisitor v);

    @Builtin
    public abstract void __mod__(TVisitor v);

    @Builtin
    public abstract void __eq__(TVisitor v);

    @Builtin
    public abstract void __neq__(TVisitor v);

    @Builtin
    public abstract void __lt__(TVisitor v);

    @Builtin
    public abstract void __le__(TVisitor v);

    @Builtin
    public abstract void __gt__(TVisitor v);

    @Builtin
    public abstract void __ge__(TVisitor v);

    @Builtin
    public abstract void __and__(TVisitor v);

    @Builtin
    public abstract void __or__(TVisitor v);

    @Builtin
    public abstract void __neg__(TVisitor v);

    @Builtin
    public abstract void __not__(TVisitor v);

    @Builtin
    public abstract void __int__(TVisitor v);

    @Builtin
    public abstract void __bool__(TVisitor v);

    /**
     * @implNote Completely arbitrary implementation. Offsets could be hard-coded, who cares
     */
    public static int getOffset(String functionName) {
        final Method[] arr = Type.class.getDeclaredMethods();

        for (int c = 0; c < arr.length; c++)
            if (arr[c].getName().equals(functionName)) return 8 * c;

        throw new CompileError("could not find offset of function '" + functionName + "'");
    }

    public static int getOffset(Enum element) {
        for (Method method : Type.class.getDeclaredMethods()) {
            final Builtin annot = method.getAnnotation(Builtin.class);
            if (annot != null && method.getName().substring(2, method.getName().length() - 2).equals(element.name().toLowerCase().substring(1)))
                return getOffset(method.getName());
        }
        throw new CompileError("could not find method corresponding to operator " + element.name());
    }

    private static final List<Method> METHODS = findMethods();

    @NotNull
    private static List<Method> findMethods() {
        List<Method> list = new ArrayList<>();

        for (Method method : Type.class.getDeclaredMethods())
            if (method.isAnnotationPresent(Builtin.class)) list.add(method);

        return list;
    }

    public void compileInit(TVisitor v) {

        // Static constants if needed
        staticConstants(v);

        v.malloc(METHODS.size() * 8); // Allocate memory for type descriptor
        int ofs = 0;
        for (Method function : METHODS)
            v.x86().movq(asmId(this, function), (8 * ofs++) + "(%rax)");

        // Write address to type descriptor in type descriptor array
        v.x86().movq("%rax", (getOffset() * 8) + "(" + Compile.TDA_REG + ")");
    }

    /**
     * When calling this method, address of type descriptor array
     * is located in %rdi
     */
    public void compileMethods(TVisitor v) {
        final Type nulled = REGISTERED.put(getOffset(), this);
        if (nulled != null) throw new CompileError("registered type with duplicate id " + getOffset());

        // Write methods
        for (Method parent : METHODS) {
            try {
                final Method method = this.getClass().getDeclaredMethod(parent.getName(), TVisitor.class);

                // Label method in .text
                v.x86().label(asmId(this, method));

                // Write method
                method.invoke(this, v);
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new CompileError("could not compile method " + parent.getName() + " from type " + name() + ": " + exception.getMessage());
            }
        }
    }

    @NotNull
    private static String asmId(Type type, Method function) {
        return "__" + type.name() + function.getName();
    }

    /**
     * Used to make sure that there are no types compiled
     * with same numerical identifier.
     */
    private static final Map<Integer, Type> REGISTERED = new HashMap<>();
}
