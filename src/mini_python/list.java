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

    private static final String
            __ADD__LOOP1__ = "__list__add__l1__",
            __ADD__END1__ = "__list__add__e1__",
            __ADD__LOOP2__ = "__list__add__l2__",
            __ADD__END2__ = "__list__add__e2__";

    @Override
    public void __add__(TVisitor v) {
        v.ofType("%rsi", Type.LIST); // %rsi = &l2

        v.x86().movq("%rdi", "%r8"); // save &l1 in %r8
        v.x86().movq("8(%rdi)", "%rdi");
        v.x86().addq("8(%rsi)", "%rdi");
        v.x86().movq("%rdi", "%r9"); // %r9 = len(l1 + l2)
        v.x86().leaq("16(, %rdi, 8)", "%rdi"); // %rdi = 16 + 2 * (len(l1) + len(l2))
        v.saveRegisters(() -> v.x86().call("__malloc__"), "%r8", "%rsi", "%r9"); // %rax = &(l1 + l2)
        v.x86().movq(Type.LIST.getOffset(), "0(%rax)"); // set type identifier
        v.x86().movq("%r9", "8(%rax)"); // set list size

        // %rcx / %rdx in list1/list2
        // %r9  / ---- in new list
        v.x86().leaq("16(%rax)", "%r9");

        // %r8 = &l1
        // %rsi = &l2
        iter(v, "%r8", __ADD__LOOP1__, __ADD__END1__, () -> {
            v.x86().movq("(%rcx)", "%r10");
            v.x86().movq("%r10", "(%r9)");
            v.x86().addq("$8", "%r9");
        });

        iter(v, "%rsi", __ADD__LOOP2__, __ADD__END2__, () -> {
            v.x86().movq("(%rcx)", "%r10");
            v.x86().movq("%r10", "(%r9)");
            v.x86().addq("$8", "%r9");
        });

        v.x86().ret();
    }

    /**
     * Iterates through a list in a conventional fashion.
     *
     * @param listReg containing the list to iterate through
     * @param code    Code of while loop
     *                %rcx = increasing pointer
     *                %rdx = end pointer
     */
    private void iter(TVisitor v, String listReg, String loopLabel, String endLabel, Runnable code) {
        v.x86().leaq("16(" + listReg + ")", "%rcx"); // %rcx = incrementing pointer
        v.x86().movq("8(" + listReg + ")", "%rdx");
        v.x86().leaq("16(" + listReg + ", %rdx, 8)", "%rdx"); // %rdx = destination of rcx

        v.x86().label(loopLabel);
        v.x86().cmpq("%rcx", "%rdx"); // quit loop?
        v.x86().je(endLabel);

        code.run();

        v.x86().addq("$8", "%rcx"); // increment pointer
        v.x86().jmp(loopLabel); // jump back
        v.x86().label(endLabel); // End of loop
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

    private static final String
            __PRINT__LOOP__ = "__list__print__lop__",
            __PRINT__SKSP__ = "__list__print__sks__",
            __PRINT__END__ = "__list__print__end__";

    @Override
    public void __print__(TVisitor v) {
        v.saveRegisters(() -> {
            v.x86().xorq("%r12", "%r12"); // %r12 = counter
            v.x86().movq("8(%rdi)", "%r13"); // %r13 = list length
            v.x86().movq("%rdi", "%r14"); // %r14 = &list

            v.x86().movq("$" + BRACKET_OPEN_LABEL, "%rdi"); // bracket open
            v.x86().call("__printf__");

            v.x86().label(__PRINT__LOOP__); // Main loop label
            v.x86().cmpq("%r12", "%r13");
            v.x86().je(__PRINT__END__); // Quit loop
            v.x86().cmpq(0, "%r12");
            v.x86().je(__PRINT__SKSP__);
            v.x86().movq("$" + SEP_LABEL, "%rdi"); // display separator
            v.x86().call("__printf__");
            v.x86().label(__PRINT__SKSP__);
            v.x86().leaq("16(%r14, %r12, 8)", "%rdi"); // compute object address
            v.x86().movq("(%rdi)", "%rdi"); // access object
            v.selfCall(Type.getOffset("__print__")); // print object
            v.x86().incq("%r12");
            v.x86().jmp(__PRINT__LOOP__);

            v.x86().label(__PRINT__END__); // End of loop
            v.x86().movq("$" + BRACKET_CLOSE_LABEL, "%rdi"); // bracket close
            v.x86().call("__printf__");
        }, "%r12", "%r13", "%r14");
        v.x86().ret();
    }
}
