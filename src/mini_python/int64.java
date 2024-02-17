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
    public void staticConstants(X86_64 x86) {

    }

    @Override
    public void __add__(X86_64 x86) {
        // &[e2]
        // &[e1]
        // ....
        x86.popq("%rdi"); // rdi = &[e1]
        x86.popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        x86.movq("8(%rdi)", "%rdi"); // rdi = [e1]
        x86.movq("8(%rsi)", "%rsi"); // rsi = [e2]
        x86.addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        x86.ret();
    }

    @Override
    public void __sub__(X86_64 x86) {
        // &[e2]
        // &[e1]
        // ....
        x86.popq("%rdi"); // rdi = &[e1]
        x86.popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        x86.movq("8(%rdi)", "%rdi"); // rdi = [e1]
        x86.movq("8(%rsi)", "%rsi"); // rsi = [e2]
        x86.addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        x86.ret();
    }

    @Override
    public void __mul__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __div__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __mod__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __eq__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __neq__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __lt__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __le__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __gt__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __ge__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __and__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __or__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __neg__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __not__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }
}
