package mini_python;

import mini_python.annotation.Delegated;
import mini_python.exception.FunctionDelegatedError;

public class string extends Type {

    public static final String
            PRINT_FORMAT_LABEL = "__string__print__fmt__",
            PRINT_FORMAT_VALUE = "%s\n";

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    public String name() {
        return "string";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(PRINT_FORMAT_LABEL);
        v.x86().string(PRINT_FORMAT_VALUE);
    }

    @Override
    public void __add__(TVisitor v) {

        v.ofType("%rsi", Type.STRING);

        v.saveRegisters(x86 -> {
            x86.movq("%rdi", "%r12"); // %r12 = &s1
            x86.movq("%rsi", "%r13"); // %r13 = &s2

            // allocate string result memory
            x86.movq("8(%r12)", "%rdi");
            x86.addq("8(%r13)", "%rdi");
            x86.movq("%rdi", "%r14"); // %r14 = len(s1 + s2)
            x86.addq("$17", "%rdi"); // %rdi = 16 + len(s1 + s2) + 1
            x86.call("__malloc__"); // allocate memory
            x86.movq("$3", "0(%rax)"); // initialize type identifier
            x86.movq("%r14", "8(%rax)"); // initialize string length
            x86.movq("%rax", "%r14"); // %r14 = &res

            // copy s1 to res
            x86.leaq("16(%r14)", "%rdi");
            x86.leaq("16(%r12)", "%rsi");
            x86.call("__strcpy__");

            // copy s2 to res
            x86.leaq("16(%r14)", "%rdi");
            x86.leaq("16(%r13)", "%rsi");
            x86.call("__strcat__");
            x86.movq("%r14", "%rdi");
        }, "%r12", "%r13", "%r14");

        v.x86().ret();
    }

    @Override
    public void __sub__(TVisitor v) {
        v.err();
    }

    @Override
    public void __mul__(TVisitor v) {
        v.err();
    }

    @Override
    public void __div__(TVisitor v) {
        v.err();
    }

    @Override
    public void __mod__(TVisitor v) {
        v.err();
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
        v.err();
    }

    @Override
    @Delegated(id = "__bool__not__")
    public void __not__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    public void __int__(TVisitor v) {
        v.err();
    }

    @Override
    @Delegated(id = "__int__bool__")
    public void __bool__(TVisitor v) {
        throw new FunctionDelegatedError();
    }

    @Override
    public void __print__(TVisitor v) {
        v.x86().addq("$16", "%rdi");
        v.x86().movq("%rdi", "%rsi"); // 1st arg, string, in %rsi
        v.x86().movq("$" + PRINT_FORMAT_LABEL, "%rdi"); // format in %rdi
        v.x86().xorq("%rax", "%rax");
        v.x86().call("__printf__");
        v.x86().ret();
    }
}
