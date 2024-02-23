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
        iter(v, "%r8", "add__1", () -> {
            v.x86().movq("(%rcx)", "%r10");
            v.x86().movq("%r10", "(%r9)");
            v.x86().addq("$8", "%r9");
        });

        iter(v, "%rsi", "add__2", () -> {
            v.x86().movq("(%rcx)", "%r10");
            v.x86().movq("%r10", "(%r9)");
            v.x86().addq("$8", "%r9");
        });

        v.x86().ret();
    }

    private void iter(TVisitor v, String listReg, String label, Runnable code) {
        iter(v, listReg, label, "%rcx", "%rdx", code);
    }

    /**
     * Iterates through a list in a conventional fashion.
     *
     * @param listReg Register containing the list to iterate through.
     *                It is only used by the three first instructions and
     *                can therefore be an unsafe caller-saved register.
     * @param code    Code of while loop
     *                %rcx = increasing pointer
     *                %rdx = end pointer
     */
    private void iter(TVisitor v, String listReg, String label, String counterReg, String destReg, Runnable code) {
        final String loopLabel = "__list__" + label + "__loop__";
        final String endLabel = "__list__" + label + "__end__";

        v.x86().leaq("16(" + listReg + ")", counterReg); // %rcx = incrementing pointer
        v.x86().movq("8(" + listReg + ")", destReg);
        v.x86().leaq("16(" + listReg + ", " + destReg + ", 8)", destReg); // %rdx = destination of rcx

        v.x86().label(loopLabel);
        v.x86().cmpq(counterReg, destReg); // quit loop?
        v.x86().je(endLabel);

        code.run();

        v.x86().addq("$8", counterReg); // increment pointer
        v.x86().jmp(loopLabel); // jump back
        v.x86().label(endLabel); // end of loop
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

    private static final String
            __EQ__NEG__ = "__list__eq__neg__",
            __EQ__BRK__ = "__list__eq__brk__",
            __EQ__CTN__ = "__list__eq__ctn__";

    @Override
    public void __eq__(TVisitor v) {
        v.x86().movq("0(%rsi)", "%r10");
        v.x86().cmpq(Type.LIST.getOffset(), "%r10"); // check type
        v.x86().jne(__EQ__NEG__);

        v.x86().movq("8(%rsi)", "%r10"); // check length
        v.x86().cmpq("%r10", "8(%rdi)");
        v.x86().jne(__EQ__NEG__);

        v.saveRegisters(() -> {
            v.x86().leaq("16(%rsi)", "%r13"); // pointer within l2
            v.x86().movq("$" + bool.TRUE_LABEL, "%rbx"); // true by default

            // %r12 = pointer within l1
            // %r13 = pointer within l2
            // %r14 = max value of pointer %r12
            // %rbx = if lists are equal
            iter(v, "%rdi", "eq", "%r12", "%r14", () -> {
                v.x86().movq("(%r12)", "%rdi"); // 1st arg, caller
                v.x86().movq("(%r13)", "%rsi"); // 2nd arg
                v.selfCall(Type.getOffset("__eq__")); // bool in %rax
                v.x86().cmpq(1, "8(%rax)");
                v.x86().je(__EQ__CTN__);
                v.x86().movq("$" + bool.FALSE_LABEL, "%rbx");
                v.x86().jmp(__EQ__BRK__); // break out of loop

                v.x86().label(__EQ__CTN__);
                v.x86().addq("$8", "%r13"); // increment l2 pointer
            });

            v.x86().label(__EQ__BRK__);
            v.x86().movq("%rbx", "%rax");
        }, "%rbx", "%r12", "%r13", "%r14");

        v.x86().ret(); // return, %rax already populated.

        v.x86().label(__EQ__NEG__); // Check element per element
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();
    }

    private static final String __NEQ__SKIP__ = "__list__neq__pos";

    @Override
    public void __neq__(TVisitor v) {
        v.x86().call("__list__eq__");
        v.x86().cmpq(0, "8(%rax)");
        v.x86().je(__NEQ__SKIP__);
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();
        v.x86().label(__NEQ__SKIP__);
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();
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
        // TODO rewrite using #iter(..)
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
