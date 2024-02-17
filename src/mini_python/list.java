package mini_python;

import mini_python.exception.NotImplementedError;

public class list extends Type {

    @Override
    public int ofs() {
        return 4;
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public void staticConstants(X86_64 x86) {

    }

    @Override
    public void __add__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __sub__(X86_64 x86) {
        x86.err();
    }

    @Override
    public void __mul__(X86_64 x86) {
        x86.err();
    }

    @Override
    public void __div__(X86_64 x86) {
        x86.err();
    }

    @Override
    public void __mod__(X86_64 x86) {
        x86.err();
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
        x86.err();
    }

    @Override
    public void __not__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }
}
