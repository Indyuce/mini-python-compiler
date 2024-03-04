package mini_python;

import mini_python.annotation.Delegated;
import mini_python.annotation.Undefined;
import mini_python.exception.FunctionDelegatedError;
import mini_python.exception.MethodNotDefinedError;

public class bool extends Type {

    public static final String
            TRUE_LABEL = "__bool__True",
            FALSE_LABEL = "__bool__False",
            TRUE_PRINT_FORMAT_LABEL = "__bool__True__print__fmt__",
            TRUE_PRINT_FORMAT_VALUE = "True",
            FALSE_PRINT_FORMAT_LABEL = "__bool__False__print__fmt__",
            FALSE_PRINT_FORMAT_VALUE = "False";

    @Override
    public String name() {
        return "bool";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(TRUE_LABEL);
        v.x86().quadLabel(Type.BOOL.classDescLabel());
        v.x86().quad(1);

        v.x86().dlabel(FALSE_LABEL);
        v.x86().quadLabel(Type.BOOL.classDescLabel());
        v.x86().quad(0);

        v.x86().dlabel(TRUE_PRINT_FORMAT_LABEL);
        v.x86().string(TRUE_PRINT_FORMAT_VALUE);

        v.x86().dlabel(FALSE_PRINT_FORMAT_LABEL);
        v.x86().string(FALSE_PRINT_FORMAT_VALUE);
    }

    /**
     * %rdi = 1st arg, object caller
     * %rsi = 2nd arg
     *
     * @param v         Visitor I guess
     * @param operation Code corresponding to arithmetic operation where
     *                  [e1] is in %rdi and [e2] is in %rsi. Boolean result
     *                  of comparison/equality check must be placed in %cl
     */
    private void comp(TVisitor v, Runnable operation) {
        v.saveRegisters(() -> {
            v.x86().movq("%rsi", "%rdi");
            v.selfCall(Type.getOffset("__bool__")); // %rax = &int([e2])
            v.x86().movq("8(%rax)", "%rsi"); // %rsi = int([e2])
        }, "%rdi");
        v.x86().movq("8(%rdi)", "%rdi"); // %rdi = [e1]

        operation.run();
        v.x86().movzbq("%cl", "%r10");

        v.saveRegisters(() -> v.newValue(Type.BOOL, 16), "%r10");
        v.x86().movq("%r10", "8(%rax)"); // put value at %rax+8
        v.x86().ret();
    }

    // Future intention of this function : to_bool (or bool(...))
    @Override
    public void __bool__(TVisitor v) {
        v.x86().movq("%rdi", "%rax");
        v.x86().ret();
    }

    @Override
    @Undefined
    public void __add__(TVisitor v) {
        throw new MethodNotDefinedError();
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

    @Override
    @Delegated(id = "__int__eq__")
    public void __eq__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Delegated(id = "__int__neq__")
    public void __neq__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Delegated(id = "__int__lt__")
    public void __lt__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Delegated(id = "__int__le__")
    public void __le__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Delegated(id = "__int__gt__")
    public void __gt__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Delegated(id = "__int__ge__")
    public void __ge__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    @Undefined
    public void __neg__(TVisitor v) {
        throw new MethodNotDefinedError();
    }

    @Override
    public void __not__(TVisitor v) {
        v.saveRegisters(() -> v.newValue(Type.BOOL, 16), "%rdi");

        v.x86().cmpq(0, "8(%rdi)");
        v.x86().sete("%cl");
        v.x86().movzbq("%cl", "%r10");
        v.x86().movq("%r10", "8(%rax)");

        v.x86().ret();
    }

    @Override
    public void __print__(TVisitor v) {
        // %rdi = &[e]
        v.x86().cmpq(0, "8(%rdi)");
        v.x86().je("__bool__print__neg__");
        v.x86().movq("$" + TRUE_PRINT_FORMAT_LABEL, "%rdi"); // print True
        v.x86().jmp("__bool__print__nxt__");
        v.x86().label("__bool__print__neg__"); // print False
        v.x86().movq("$" + FALSE_PRINT_FORMAT_LABEL, "%rdi");
        v.x86().label("__bool__print__nxt__");
        v.x86().call("__printf__");
        v.x86().ret();
    }

    @Override
    public void __int__(TVisitor v) {
        v.saveRegisters(() -> v.newValue(Type.INT, 16), "%rdi");

        v.x86().movq("8(%rdi)", "%r10");
        v.x86().movq("%r10", "8(%rax)"); // Interpret byte-value as int

        v.x86().ret();
    }
}
