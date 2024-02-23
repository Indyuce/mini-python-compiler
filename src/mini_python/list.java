package mini_python;

import mini_python.annotation.Delegated;
import mini_python.exception.FunctionDelegatedError;

public class list extends Type {

    public static final String
            BRACKET_OPEN_LABEL = "__list__print__bko__",
            BRACKET_OPEN_VALUE = "[",
            SEP_LABEL = "__list__print__sep__",
            SEP_VALUE = ", ",
            BRACKET_CLOSE_LABEL = "__list__print__bkc__",
            BRACKET_CLOSE_VALUE = "]";

    @Override
    public int getOffset() {
        return 4;
    }

    @Override
    public String name() {
        return "list";
    }


    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(BRACKET_OPEN_LABEL);
        v.x86().string(BRACKET_OPEN_VALUE);
        v.x86().dlabel(SEP_LABEL);
        v.x86().string(SEP_VALUE);
        v.x86().dlabel(BRACKET_CLOSE_LABEL);
        v.x86().string(BRACKET_CLOSE_VALUE);
    }

    @Override
    public void __add__(TVisitor v) {
        // TODO
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
        v.saveRegisters(() -> {
            v.x86().xorq("%r12", "%r12"); // %r12 = counter
            v.x86().movq("8(%rdi)", "%r13"); // %r13 = list length
            v.x86().movq("%rdi", "%r14"); // %r14 = &list

            v.x86().movq("$" + BRACKET_OPEN_LABEL, "%rdi"); // bracket open
            v.x86().call("__printf__");

            final String loop = v.newTextLabel(), skipSep = v.newTextLabel(), end = v.newTextLabel();
            v.x86().label(loop); // Main loop label
            v.x86().cmpq("%r12", "%r13");
            v.x86().je(end); // Quit loop
            v.x86().cmpq(0, "%r12");
            v.x86().je(skipSep);
            v.x86().movq("$"+ SEP_LABEL, "%rdi"); // display separator
            v.x86().call("__printf__");
            v.x86().label(skipSep);
            v.x86().leaq("16(%r14, %r12, 8)", "%rdi"); // compute object address
            v.x86().movq("(%rdi)", "%rdi"); // access object
            v.selfCall(Type.getOffset("__print__")); // print object
            v.x86().incq("%r12");
            v.x86().jmp(loop);

            v.x86().label(end); // End of loop
            v.x86().movq("$" + BRACKET_CLOSE_LABEL, "%rdi"); // bracket close
            v.x86().call("__printf__");
        }, "%r12", "%r13", "%r14");
        v.x86().ret();
    }
}
