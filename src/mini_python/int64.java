package mini_python;

import mini_python.annotation.Delegated;
import mini_python.exception.FunctionDelegatedError;

/**
 * int is not a valid class name but it really is an int
 */
public class int64 extends Type {

    public static final String
            PRINT_FORMAT_LABEL = "__int__print__fmt__",
            PRINT_FORMAT_VALUE = "%d";

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    public String name() {
        return "int";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(PRINT_FORMAT_LABEL);
        v.x86().string(PRINT_FORMAT_VALUE);
    }

    /**
     * %rdi = 1st arg, object caller
     * %rsi = 2nd arg
     *
     * @param v         Visitor I guess
     * @param operation Code corresponding to arithmetic operation where
     *                  [e1] is in %rdi and [e2] is in %rsi. Result must
     *                  be placed inside %rdi
     */
    private void binop(TVisitor v, Runnable operation) {
        v.saveRegisters(x86 -> {
            v.x86().movq("%rsi", "%rdi");
            v.objectFunctionCall(Type.getOffset("__int__")); // %rdi = &int([e2])
            v.x86().movq("8(%rdi)", "%rsi"); // %rsi = int([e2])
        }, "%rdi");
        v.x86().movq("8(%rdi)", "%rdi"); // %rdi = [e1]

        operation.run();

        v.newValue(Type.INT, 2);
        v.x86().movq("%rdi", "8(%rax)"); // put value at %rax+8
        v.x86().movq("%rax", "%rdi"); // put address in %rdi
        v.x86().ret();
    }

    private void comp(TVisitor v, Runnable operation) {
        // TODO
    }

    @Override
    public void __add__(TVisitor v) {
        binop(v, () -> v.x86().addq("%rsi", "%rdi"));
    }

    @Override
    public void __sub__(TVisitor v) {
        binop(v, () -> v.x86().subq("%rsi", "%rdi"));
    }

    @Override
    public void __mul__(TVisitor v) {
        binop(v, () -> v.x86().imulq("%rsi", "%rdi"));
    }

    @Override
    public void __div__(TVisitor v) {
        binop(v, () -> {
            v.x86().movq("%rdi", "%rax"); // dividend goes in %rax
            v.x86().idivq("%rsi"); // divider goes in %rsi
            v.x86().movq("%rax", "%rdi"); // quotient is placed in %rax
        });
    }

    @Override
    public void __mod__(TVisitor v) {
        binop(v, () -> {
            v.x86().movq("%rdi", "%rax");
            v.x86().idivq("%rsi");
            v.x86().movq("%rdx", "%rdi"); // remainder is placed in %rax
        });
    }

    @Override
    public void __eq__(TVisitor v) {
        // TODO
    }

    @Override
    public void __neq__(TVisitor v) {
        // TODO
    }

    @Override
    public void __lt__(TVisitor v) {
        // TODO
    }

    @Override
    public void __le__(TVisitor v) {
        // TODO
    }

    @Override
    public void __gt__(TVisitor v) {
        // TODO
    }

    @Override
    public void __ge__(TVisitor v) {
        // TODO
    }

    @Override
    public void __and__(TVisitor v) {
        // TODO
    }

    @Override
    public void __or__(TVisitor v) {
        // TODO
    }

    @Override
    public void __neg__(TVisitor v) {
        v.newValue(Type.INT, 2);

        v.x86().movq("8(%rdi)", "%r10");
        v.x86().negq("%r10");
        v.x86().movq("%r10", "8(%rax)");

        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }

    @Override
    @Delegated(id = "__bool__not__")
    public void __not__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    public void __int__(TVisitor v) {
        v.x86().ret();
    }

    @Override
    public void __bool__(TVisitor v) {
        v.newValue(Type.BOOL, 2);

        v.x86().movq("8(%rdi)", "%r10");
        v.x86().notq("%r10");
        v.x86().notq("%r10");
        v.x86().movq("%r10", "8(%rax)");

        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }

    @Override
    public void __print__(TVisitor v) {
        v.x86().movq("8(%rdi)", "%rsi"); // extract byte value in %rsi
        v.x86().movq("$" + PRINT_FORMAT_LABEL, "%rdi");
        v.x86().xorq("%rax", "%rax");
        v.x86().call("printf");
        v.x86().ret();
    }
}
