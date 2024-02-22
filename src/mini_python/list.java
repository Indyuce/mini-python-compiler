package mini_python;

public class list extends Type {

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
        // TODO convert to bool then not
    }

    @Override
    public void __int__(TVisitor v) {
        v.err();
    }

    @Override
    public void __bool__(TVisitor v) {
        // TODO [] = False, otherwise True
    }
}
