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
    public void staticConstants(X86_64 x86) {
        x86.dlabel(TRUE);
        x86.quad(ofs());
        x86.quad(1);
        x86.dlabel(FALSE);
        x86.quad(ofs());
        x86.quad(0);
    }

    @Override
    public void __add__(X86_64 x86) {
        throw new NotImplementedError("method not implemented");
    }

    @Override
    public void __sub__(X86_64 x86) {
        x86.exit();
    }

    @Override
    public void __mul__(X86_64 x86) {
        x86.exit();
    }

    @Override
    public void __div__(X86_64 x86) {
        x86.exit();
    }

    @Override
    public void __mod__(X86_64 x86) {
        x86.exit();
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
