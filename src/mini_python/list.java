package mini_python;

import mini_python.annotation.Delegated;
import mini_python.annotation.Undefined;
import mini_python.annotation.Nullable;
import mini_python.exception.FunctionDelegatedError;
import mini_python.exception.MethodNotDefinedError;

public class list extends Type {

    public static final String
            BRACKET_OPEN_LABEL = "__list__print__bko__",
            BRACKET_OPEN_VALUE = "[",
            SEP_LABEL = "__list__print__sep__",
            SEP_VALUE = ", ",
            BRACKET_CLOSE_LABEL = "__list__print__bkc__",
            BRACKET_CLOSE_VALUE = "]";

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
        v.ofType("%rsi", Type.LIST, "__add__", Type.LIST); // %rsi = &l2

        v.x86().movq("%rdi", "%r8"); // save &l1 in %r8
        v.x86().movq("8(%rdi)", "%rdi");
        v.x86().addq("8(%rsi)", "%rdi");
        v.x86().movq("%rdi", "%r9"); // %r9 = len(l1 + l2)
        v.x86().leaq("16(, %rdi, 8)", "%rdi"); // %rdi = 16 + 2 * (len(l1) + len(l2))
        v.saveRegisters(() -> v.x86().call("__malloc__"), "%r8", "%rsi", "%r9"); // %rax = &(l1 + l2)
        v.x86().movq(Type.LIST.classDesc(), "0(%rax)"); // set type identifier
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

    public static void iter(TVisitor v, @Nullable String listReg, String label, Runnable code) {
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
    public static void iter(TVisitor v, @Nullable String listReg, String label, String counterReg, String destReg, Runnable code) {
        final String loopLabel = "__list__" + label + "__loop__";
        final String endLabel = "__list__" + label + "__end__";

        if (listReg != null) {
            v.x86().leaq("16(" + listReg + ")", counterReg); // %rcx = incrementing pointer
            v.x86().movq("8(" + listReg + ")", destReg);
            v.x86().leaq("16(" + listReg + ", " + destReg + ", 8)", destReg); // %rdx = destination of rcx
        }

        v.x86().label(loopLabel);
        v.x86().cmpq(counterReg, destReg); // quit loop?
        v.x86().je(endLabel);

        code.run();

        v.x86().addq("$8", counterReg); // increment pointer
        v.x86().jmp(loopLabel); // jump back
        v.x86().label(endLabel); // end of loop
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

    private static final String
            __EQ__NEG__ = "__list__eq__neg__",
            __EQ__BRK__ = "__list__eq__brk__",
            __EQ__CTN__ = "__list__eq__ctn__";

    /**
     * %rdi : first list
     * %rsi : second list
     */
    @Override
    public void __eq__(TVisitor v) {
        v.x86().movq("0(%rsi)", "%r10");
        v.x86().cmpq(Type.LIST.classDesc(), "%r10"); // check type
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

        v.x86().label(__EQ__NEG__);
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
        comp(v, "lt", true, false);
    }

    @Override
    public void __le__(TVisitor v) {
        comp(v, "le", true, true);
    }

    @Override
    public void __gt__(TVisitor v) {
        comp(v, "gt", false, false);
    }

    @Override
    public void __ge__(TVisitor v) {
        comp(v, "ge", false, true);
    }

    /**
     * %rdi : first list
     * %rsi : second list
     * @param v x86 visitor
     * @param functionName function nominator
     * @param lower If it's < or >
     * @param equal If it's <= (resp =>) or not.
     */
    private void comp(TVisitor v, String functionName, boolean lower, boolean equal) {
        /*

        Two cases : should equal be true (<= or >=) :

        We keep comparing element by element. If (comparison and not equal), early stop on result! Else, continue comparing next elements.
        If went through all the elements without returning, of the smallest list: compare len(list_first) to len(list_second)
        - Equal : return true
        - Not equal : return depends on lower.

        Should equal be false (< or >):

        I only need to test the first element!
        Should one of the lists be empty, compare the lengths.
        Should the two lists be NOT empty, compare just the first elements.

         */
        final String negLabel = "__list__" + functionName + "__neg__";
        final String ctnLabel = "__list__" + functionName + "__ctn__";
        final String brkLabel = "__list__" + functionName + "__brk__";
        final String compFunction = "__" + functionName + "__";

        v.ofType("%rsi", Type.LIST, compFunction, Type.LIST);

        // Check type - in fact, this shouldn't _compile_. But in this case it should throw a panic error - TODO
        v.x86().movq("0(%rsi)", "%r10");
        v.x86().cmpq(Type.LIST.classDesc(), "%r10"); // check type
        v.x86().jne(__EQ__NEG__);


        // Switch list 1 and 2 such that list 1 is the shorter one, list 2 is the longer one
        {
            String label_name = "__min__"+functionName+"__2__";
            String label_name_fin = "__min__"+functionName+"__fin__";
            v.x86().movq("8(%rsi)", "%rcx");
            v.x86().cmpq("%rcx", "8(%rdi)");
            v.x86().js(label_name);
            v.x86().movq("%rsi", "%rax");
            v.x86().movq("%rdi", "%rcx");
            v.x86().jmp(label_name_fin);
            v.x86().label(label_name); // len(l1) < len(l2)
            v.x86().movq("%rdi", "%rax");
            v.x86().movq("%rsi", "%rcx");
            v.x86().label(label_name_fin);
        }

        // %rax (lowest)
        // %rcx (highest)
        // %rdi (first)
        // %rsi (second)
        // Meaning 2 variables pointing at the same lists

        if (equal) {

            v.saveRegisters(() -> {
                v.x86().leaq("16(%rcx)", "%r13"); // pointer within HLL
                v.x86().movq("$" + bool.TRUE_LABEL, "%rbx"); // true by default TODO

                // %r12 = pointer within LLL
                // %r13 = pointer within HLL
                // %r14 = max value of pointer %r12
                // %rbx = if lists are equal

                iter(v, "%rax", "cmp_" + functionName + "_", "%r12", "%r14", () -> {
                    v.saveRegisters(() -> {
                        v.x86().movq("(%r12)", "%rdi"); // 1st arg (from caller parser)
                        v.x86().movq("(%r13)", "%rsi"); // 2nd arg
                        v.selfCall(Type.getOffset(compFunction));
                    }, "%rsi", "%rdi");

                    v.x86().cmpq(1, "8(%rax)");

                    v.x86().label("__list__cmp_" + functionName + "__typeif_");

                    v.x86().je(ctnLabel);
                    v.x86().movq("$" + bool.FALSE_LABEL, "%rbx");
                    v.x86().jmp(brkLabel); // break out of loop

                    v.x86().label(ctnLabel);
                    v.x86().addq("$8", "%r13"); // increment list_2 pointer
                });

                String sadBrkLabel = "__list__" + functionName + "__falseret_brk__";

                // The loop is finished, meaning smallest is fully parsed, but no result : compare lengths
                v.x86().movq("8(%rdi)", "%r10");
                v.x86().cmpq("%r10", "8(%rsi)");

                switch (functionName) {
                    case "le":
                        // True already in rbx
                        v.x86().jl(sadBrkLabel);
                        v.x86().jmp(brkLabel);
                        break;
                    case "ge":
                        // Same comment
                        v.x86().jg(sadBrkLabel);
                        v.x86().jmp(brkLabel);
                        break;
                    default:
                        v.x86().jmp(brkLabel);
                }


                v.x86().label(sadBrkLabel);
                v.x86().movq("$" + bool.FALSE_LABEL, "%rbx");

                v.x86().label(brkLabel);
                v.x86().movq("%rbx", "%rax");
            }, "%rbx", "%r12", "%r13", "%r14", "%rdx");

            v.x86().ret(); // return, %rax already populated. TODO

            v.x86().label(negLabel);
            v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
            v.x86().ret();
        } else {

            String non_empty_lists_label = "__non_empty_strict__"+functionName+"__";
            String compare_gives_true = "__result__"+functionName+"_true_";
            String compare_gives_false = "__result__"+functionName+"_false_";

            v.x86().cmpq(0, "8(%rax)");
            v.x86().jne(non_empty_lists_label);

            v.x86().movq("8(%rdi)", "%r10");
            v.x86().cmpq("%r10", "8(%rsi)");

            // Unfortunately this is hard coded because we use a quad int representation for the length of the list
            // (Not an object that is apart)
            switch (functionName) {
                case "lt":
                    v.x86().jg(compare_gives_true);
                    v.x86().jmp(compare_gives_false);
                    break;
                case "gt":
                    v.x86().jl(compare_gives_true);
                    v.x86().jmp(compare_gives_false);
                    break;
                default:
                    v.x86().jmp(compare_gives_false);
            }

            // If both are longer than 1 element : compare first elements of each list
            v.x86().label(non_empty_lists_label);
            v.x86().movq("16(%rdi)", "%rdi"); // 1st arg (from caller parser)
            v.x86().movq("16(%rsi)", "%rsi"); // 2nd arg

            // Call the associated function
            v.selfCall(Type.getOffset(compFunction));

            // If comparison successful : return the correct value
            v.x86().cmpq(1, "8(%rax)");
            v.x86().je(compare_gives_true);
            v.x86().jmp(compare_gives_false);


            // return true label
            v.x86().label(compare_gives_true);
            v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
            v.x86().ret();

            // return false label
            v.x86().label(compare_gives_false);
            v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
            v.x86().ret();
        }
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
