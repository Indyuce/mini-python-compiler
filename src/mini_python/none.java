package mini_python;

public class none extends Type {

    public static final String
            NONE_LABEL = "__none__None__",
            NONE_STR_LABEL = "__none__print__fmt__",
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
        v.x86().quad(Type.NONE.getOffset());
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
    public void __neg__(TVisitor v) {
        v.err();
    }

    @Override
    public void __not__(TVisitor v) {
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax"); // Hardcoded write True
        v.x86().ret();
    }

    @Override
    public void __int__(TVisitor v) {
        v.newValue(Type.INT, 16);
        v.x86().movq(0, "8(%rax)");  // Hardcoded write 0
        v.x86().ret();
    }

    @Override
    public void __bool__(TVisitor v) {
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax"); // Hardcoded write False
        v.x86().ret();
    }

    @Override
    public void __print__(TVisitor v) {
        // Hardcoded print None
        v.x86().movq("$" + NONE_STR_LABEL, "%rdi");
        v.x86().call("__printf__");
        v.x86().ret();
    }
}
