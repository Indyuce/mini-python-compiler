package mini_python;

/**
 * int is not a valid class name but it really is an int
 */
public class int64 extends Type {

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

    }

    /**
     * This wraps the provided code into the following instructions
     * - pop addresses to values from the stack
     * - check that second argument is of type int
     * - execute code
     * - allocate space on heap for new int value
     * - put new value address in %rdi
     *
     * @param v         Visitor
     * @param operation Code corresponding to operation where
     *                  [e1] is in %rdi and [e2] is in %rsi.
     *                  Result must be in %rdi
     */
    private void binop(TVisitor v, Runnable operation) {

        v.x86().popq("%rsi"); // pop &[e1]
        v.x86().popq("%rdi"); // pop &[e2]
        v.x86().pushq("%rsi"); // push &[e1] back for later

        v.objectFunctionCall(Type.getOffset("__int__")); // %rdi = &int([e2])
        v.x86().movq("8(%rdi)", "%rsi"); // %rsi = int([e2])
        v.x86().popq("%rdi"); // %rdi = &[e1]
        v.x86().movq("8(%rdi)", "%rdi"); // %rdi = [e1]

        operation.run(); // do operation, result in %rdi

        v.newValue(Type.INT, 2);
        v.x86().movq("%rdi", "8(%rax)"); // put value at %rax+8
        v.x86().movq("%rax", "%rdi"); // put address in %rdi
        v.x86().ret();
    }

    private void comp(TVisitor v, Runnable operation) {

        v.x86().popq("%rsi"); // pop &[e1]
        v.x86().popq("%rdi"); // pop &[e2]
        v.x86().pushq("%rsi"); // push &[e1] back for later

        v.objectFunctionCall(Type.getOffset("__int__")); // %rdi = &int([e2])
        v.x86().movq("8(%rdi)", "%rsi"); // %rsi = int([e2])
        v.x86().popq("%rdi"); // %rdi = &[e1]
        v.x86().movq("8(%rdi)", "%rdi"); // %rdi = [e1]

        // TODO

        v.newValue(Type.INT, 2);
        v.x86().movq("%rdi", "8(%rax)"); // put value at %rax+8
        v.x86().movq("%rax", "%rdi"); // put address in %rdi
        v.x86().ret();
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
        // TODO
    }

    @Override
    public void __mod__(TVisitor v) {
        // TODO
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
    public void __not__(TVisitor v) {
        v.x86().jmp("__bool__not__"); // not(.) = not(bool(.)) if . is int
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
        v.x86().notq("%r10"); // not(not(.)) = bool(.)
        v.x86().movq("%r10", "8(%rax)");

        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }
}
