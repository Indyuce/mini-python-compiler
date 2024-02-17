package mini_python;

import mini_python.exception.NotImplementedError;

public class bool extends Type {

    public static final String TRUE = "__bool__True", FALSE = "__bool__False";

    @Override
    public int ofs() {
        return 1;
    }

    @Override
    public String name() {
        return "bool";
    }

    @Override
    public void staticConstants(TVisitor v) {
        v.x86().dlabel(TRUE);
        v.x86().quad(ofs());
        v.x86().quad(1);
        v.x86().dlabel(FALSE);
        v.x86().quad(ofs());
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
        v.err();
    }

    @Override
    public void __not__(TVisitor v) {
        // TODO
    }
}
