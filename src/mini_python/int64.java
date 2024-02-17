package mini_python;

import mini_python.exception.NotImplementedError;

/**
 * int is not a valid class name but it really is an int
 */
public class int64 extends Type {

    @Override
    public int ofs() {
        return 2;
    }

    @Override
    public String name() {
        return "int";
    }

    @Override
    public void staticConstants(TVisitor v) {

    }

    @Override
    public void __add__(TVisitor v) {
        // &[e2]
        // &[e1]
        // ....
        v.x86().popq("%rdi"); // rdi = &[e1]
        v.x86().popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        v.x86().movq("8(%rdi)", "%rdi"); // rdi = [e1]
        v.x86().movq("8(%rsi)", "%rsi"); // rsi = [e2]
        v.x86().addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        v.x86().ret();
    }

    @Override
    public void __sub__(TVisitor v) {
        // &[e2]
        // &[e1]
        // ....
        v.x86().popq("%rdi"); // rdi = &[e1]
        v.x86().popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        v.x86().movq("8(%rdi)", "%rdi"); // rdi = [e1]
        v.x86().movq("8(%rsi)", "%rsi"); // rsi = [e2]
        v.x86().addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        v.x86().ret();
    }

    @Override
    public void __mul__(TVisitor v) {
        // TODO
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
        // TODO
    }

    @Override
    public void __not__(TVisitor v) {
        // TODO
    }
}
