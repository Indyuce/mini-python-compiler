package mini_python;

import mini_python.annotation.Builtin;
import mini_python.annotation.Delegated;
import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    public abstract String name();

    public String classNameLabel() {
        return "__classname__" + name().toLowerCase() + "__";
    }

    public String classDesc() {
        return classDescLabel(true);
    }

    public String classDescLabel(boolean dollar) {
        return (dollar ? "$" : "") + "__class__" + name();
    }

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
    public abstract void __neg__(TVisitor v);

    @Builtin
    public abstract void __not__(TVisitor v);

    @Builtin
    public abstract void __int__(TVisitor v);

    @Builtin
    public abstract void __bool__(TVisitor v);

    @Builtin
    public abstract void __print__(TVisitor v);

    /**
     * @implNote Completely arbitrary implementation. Offsets could be hard-coded, who cares
     */
    public static int getOffset(String functionName) {

        for (int c = 0; c < METHODS.size(); c++)
            if (METHODS.get(c).getName().equals(functionName)) return 8 * c;

        throw new CompileError("could not find offset of function '" + functionName + "'");
    }

    public static int getOffset(Enum element) {
        int c = 1;
        for (Method method : METHODS) {
            if (method.getName().substring(2, method.getName().length() - 2).equals(element.name().toLowerCase().substring(1)))
                return 8 * c;
            c++;
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

        // Compile class name string
        v.x86().dlabel(classNameLabel());
        v.x86().string(name());

        // Compile type descriptor in .data
        v.x86().dlabel(classDescLabel(false));
        v.x86().quad("$" + classNameLabel()); // address of class name string
        for (Method function : METHODS)
            v.x86().quad("$" + asmId(this, function));
    }

    public static String methodNameLabel(String functionName) {
        return "__fctnname" + functionName;
    }

    public void compileMethods(TVisitor v) {

        // Write method names as strings
        for (Method parent : METHODS) {
            v.x86().dlabel(methodNameLabel(parent.getName()));
            v.x86().string(parent.getName());
        }

        // Write methods
        for (Method parent : METHODS)
            try {
                final Method method = this.getClass().getDeclaredMethod(parent.getName(), TVisitor.class);
                if (method.isAnnotationPresent(Delegated.class)) continue; // Method delegated to other type

                // Label method in .text
                v.x86().label(asmId(this, method));

                // Write method
                method.invoke(this, v);
            } catch (Exception exception) {
                throw new CompileError("could not compile method " + parent.getName() + " from type " + name() + ": " + exception.getMessage());
            }
    }

    @NotNull
    private static String asmId(Type type, Method function) {

        // Method delegation
        try {
            final Method submethod = type.getClass().getDeclaredMethod(function.getName(), TVisitor.class);
            final Delegated delegate = submethod.getAnnotation(Delegated.class);
            if (delegate != null) return delegate.id();
        } catch (Exception exception) {
            throw new CompileError("could not find method " + function.getName() + " from type " + type.name() + " for method delegation: " + exception.getMessage());
        }

        return "__" + type.name() + function.getName();
    }
}
