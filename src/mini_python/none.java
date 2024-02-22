package mini_python;

public class none extends Type {

    public static final String
            NONE_LABEL = "__none__None__",
            NONE_STR_LABEL = "__none__None__str__",
            NONE_STR_VALUE = "None";

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public String name() {
        return "none";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(NONE_LABEL);
        v.x86().quad(getOffset());
        v.x86().quad(0);

        v.x86().dlabel(NONE_STR_LABEL);
        v.x86().string(NONE_STR_VALUE);
    }

    @Override
    public void __add__(TVisitor v) {
        v.err();
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
        v.err();
    }

    @Override
    public void __neq__(TVisitor v) {
        v.err();
    }

    @Override
    public void __lt__(TVisitor v) {
        v.err();
    }

    @Override
    public void __le__(TVisitor v) {
        v.err();
    }

    @Override
    public void __gt__(TVisitor v) {
        v.err();
    }

    @Override
    public void __ge__(TVisitor v) {
        v.err();
    }

    @Override
    public void __and__(TVisitor v) {
        v.err();
    }

    @Override
    public void __or__(TVisitor v) {
        v.err();
    }

    @Override
    public void __neg__(TVisitor v) {
        v.err();
    }

    @Override
    public void __not__(TVisitor v) {
        v.x86().jmp("__bool__not__"); // not(.) = not(bool(.))
    }

    @Override
    public void __int__(TVisitor v) {
        v.newValue(Type.INT, 2);
        v.x86().movq(0, "8(%rax)");
        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }

    @Override
    public void __bool__(TVisitor v) {
        v.newValue(Type.BOOL, 2);
        v.x86().movq(0, "8(%rax)");
        v.x86().movq("%rax", "%rdi");
        v.x86().ret();
    }

    @Override
    public void __print__(TVisitor v) {
        v.x86().movq("$" + NONE_STR_LABEL, "%rdi");
        v.x86().movq("$0", "%rax");
        v.x86().call("printf");
        v.x86().ret();
    }
}
