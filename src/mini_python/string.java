package mini_python;

import mini_python.annotation.Delegated;
import mini_python.annotation.Undefined;
import mini_python.exception.FunctionDelegatedError;
import mini_python.exception.MethodNotDefinedError;

public class string extends Type {

    public static final String
            LINE_BREAK_LABEL = "__print__lnbrk__",
            LINE_BREAK_VALUE = "\n";

    @Override
    public String name() {
        return "string";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(LINE_BREAK_LABEL);
        v.x86().string(LINE_BREAK_VALUE);
    }

    @Override
    public void __add__(TVisitor v) {
        v.ofType("%rsi", Type.STRING, "__add__", Type.STRING);

        v.saveRegisters(() -> {
            v.x86().movq("%rdi", "%r12"); // %r12 = &s1
            v.x86().movq("%rsi", "%r13"); // %r13 = &s2

            // allocate string result memory
            v.x86().movq("8(%r12)", "%rdi");
            v.x86().addq("8(%r13)", "%rdi");
            v.x86().movq("%rdi", "%r14"); // %r14 = len(s1 + s2)
            v.x86().addq("$17", "%rdi"); // %rdi = 16 + len(s1 + s2) + 1
            v.x86().call("__malloc__"); // allocate memory
            v.x86().movq(Type.STRING.classDesc(), "0(%rax)"); // initialize type identifier
            v.x86().movq("%r14", "8(%rax)"); // initialize string length
            v.x86().movq("%rax", "%r14"); // %r14 = &res

            // copy s1 to res
            v.x86().leaq("16(%r14)", "%rdi");
            v.x86().leaq("16(%r12)", "%rsi");
            v.x86().call("__strcpy__");

            // copy s2 to res
            v.x86().leaq("16(%r14)", "%rdi");
            v.x86().leaq("16(%r13)", "%rsi");
            v.x86().call("__strcat__");
            v.x86().movq("%r14", "%rax");
        }, "%r12", "%r13", "%r14");

        v.x86().ret();
    }

    @Override
    @Undefined
    public void __sub__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    @Undefined
    public void __mul__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    @Undefined
    public void __div__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    @Undefined
    public void __mod__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    private static final String __EQ__POS__ = "__string__eq__pos__";

    @Override
    public void __eq__(TVisitor v) {
        v.x86().movq("0(%rsi)", "%r10");
        v.x86().cmpq(Type.STRING.classDesc(), "%r10"); // Check type of arg
        v.x86().je(__EQ__POS__);
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label(__EQ__POS__); // Check value
        v.x86().addq("$16", "%rdi");
        v.x86().addq("$16", "%rsi");
        v.x86().call("__strcmp__");
        v.x86().setz("%cl");
        v.saveRegisters(() -> v.newValue(Type.BOOL, 16), "%rcx");
        v.x86().movzbq("%cl", "%r10");
        v.x86().movq("%r10", "8(%rax)");
        v.x86().ret();
    }

    private static final String __NEQ__POS__ = "__string__neq__pos__";

    @Override
    public void __neq__(TVisitor v) {
        v.x86().movq("0(%rsi)", "%r10");
        v.x86().cmpq(Type.STRING.classDesc(), "%r10"); // Check type of arg
        v.x86().je(__NEQ__POS__);
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label(__NEQ__POS__); // Check value
        v.x86().addq("$16", "%rdi");
        v.x86().addq("$16", "%rsi");
        v.x86().call("__strcmp__");
        v.x86().setnz("%cl");
        v.saveRegisters(() -> v.newValue(Type.BOOL, 16), "%rcx");
        v.x86().movzbq("%cl", "%r10");
        v.x86().movq("%r10", "8(%rax)");
        v.x86().ret();
    }

    /**
     * %rdi = 1st arg, object caller
     * %rsi = 2nd arg
     * %rax = result
     *
     * @param code Check to perform; all flags are set after calling
     *             method strcmp. Result must be put in register %cl
     */
    private void comp(TVisitor v, String callerFunction, Runnable code) {
        v.ofType("%rsi", Type.STRING, callerFunction, Type.STRING);
        v.x86().addq("$16", "%rdi");
        v.x86().addq("$16", "%rsi");
        v.x86().call("__strcmp__");
        code.run();
        v.saveRegisters(() -> v.newValue(Type.BOOL, 16), "%rcx");
        v.x86().movzbq("%cl", "%r10");
        v.x86().movq("%r10", "8(%rax)");
        v.x86().ret();
    }

    @Override
    public void __lt__(TVisitor v) {
        comp(v, "__lt__", () -> v.x86().setl("%cl"));
    }

    @Override
    public void __le__(TVisitor v) {
        comp(v, "__le__",() -> v.x86().setle("%cl"));
    }

    @Override
    public void __gt__(TVisitor v) {
        comp(v, "__gt__",() -> v.x86().setg("%cl"));
    }

    @Override
    public void __ge__(TVisitor v) {
        comp(v, "__ge__",() -> v.x86().setge("%cl"));
    }

    @Override
    @Undefined
    public void __neg__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    @Delegated(id = "__bool__not__")
    public void __not__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Undefined
    public void __int__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    @Delegated(id = "__int__bool__")
    public void __bool__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    public void __print__(TVisitor v) {
        v.x86().addq("$16", "%rdi");
        v.x86().call("__printf__");
        v.x86().ret();
    }
}
