package mini_python;

public class none extends Type {

    public static final String NONE = "__none__None";

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
        v.x86().dlabel(NONE);
        v.x86().quad(getOffset());
        v.x86().quad(0);
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
        v.err();
    }

    @Override
    public void __int__(TVisitor v) {
        v.err();
    }

    @Override
    public void __bool__(TVisitor v) {
        v.err();
    }
}
