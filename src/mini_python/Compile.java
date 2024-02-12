package mini_python;

import mini_python.annotation.Nullable;
import mini_python.exception.CompileError;

class Compile {

    static boolean debug = false;

    static X86_64 file(TFile f) {
        final X86_64 x86 = new X86_64();
        final TVisitor visitor = new VisitorImpl(x86);

        x86.globl("main");
        TDef mainDef = f.l.getFirst(); // TODO generalize to all functions
        x86.label("main");
        mainDef.body.accept(visitor);

        // Program return
        x86.xorq("%rax", "%rax");
        x86.ret();

        return x86; // TODO
    }

    static class VisitorImpl implements TVisitor {
        final X86_64 file;

        @Nullable
        String ret;

        VisitorImpl(X86_64 file) {
            this.file = file;
        }

        int stackPointerValue = 0;

        int labelCounter = 0;

        private String newLabel() {
            return "l" + labelCounter++;
        }

        @Override
        public void visit(Cnone c) {

        }

        @Override
        public void visit(Cbool c) {

        }

        @Override
        public void visit(Cstring c) {
            final String label = newLabel();
            file.dlabel(label);

            // Write constant string to .data section at generated label
            file.string(c.s);

            // Write label to return
            ret = label;
        }

        @Override
        public void visit(Cint c) {

        }

        @Override
        public void visit(TEcst e) {

            if (e.c instanceof Cstring)
                visit((Cstring) e.c);
            else
                throw new CompileError("unsupported constant type");
        }

        @Override
        public void visit(TEbinop e) {

        }

        @Override
        public void visit(TEunop e) {

        }

        @Override
        public void visit(TEident e) {

        }

        @Override
        public void visit(TEcall e) {
          // Use name of function for call
          file.call(e.f.name);
        }

        @Override
        public void visit(TEget e) {

        }

        @Override
        public void visit(TElist e) {

        }

        @Override
        public void visit(TErange e) {

        }

        @Override
        public void visit(TElen e) {

        }

        @Override
        public void visit(TSif s) {

        }

        @Override
        public void visit(TSreturn s) {

        }

        @Override
        public void visit(TSassign s) {

        }

        @Override
        public void visit(TSprint s) {
            s.e.accept(this);
            file.movq("$"+ret, "%rdi");
            file.movq("$0", "%rax");
            file.call("printf");
        }

        @Override
        public void visit(TSblock s) {
            for (TStmt stmt : s.l) {
                stmt.accept(this);
            }
        }

        @Override
        public void visit(TSfor s) {

        }

        @Override
        public void visit(TSeval s) {

        }

        @Override
        public void visit(TSset s) {

        }
    }
}
