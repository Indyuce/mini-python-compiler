package mini_python;

public class bool extends Type {

    public static final String TRUE = "__bool__True", FALSE = "__bool__False";

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public String name() {
        return "bool";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(TRUE);
        v.x86().quad(getOffset());
        v.x86().quad(1);
        v.x86().dlabel(FALSE);
        v.x86().quad(getOffset());
        v.x86().quad(0);
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
    public void __and__(TVisitor v) {
        // TODO
    }

    @Override
    public void __or__(TVisitor v) {
        // TODO
    }

    @Override
    public void __neg__(TVisitor v) {
        // Compilation trick
        v.x86().jmp("__int__neg__");
    }

    @Override
    public void __not__(TVisitor v) {
        v.newValue(Type.BOOL, 2);

        v.x86().movq("8(%rdi)", "%r10");
        v.x86().notq("%r10");
        v.x86().movq("%r10", "8(%rax)");

        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }

    @Override
    public void __bool__(TVisitor v) {
        v.x86().ret();
    }

    @Override
    public void __int__(TVisitor v) {
        v.newValue(Type.INT, 2);

        v.x86().movq("8(%rdi)", "%r10");
        v.x86().movq("%r10", "8(%rax)"); // Interpret byte-value as int

        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }
}
