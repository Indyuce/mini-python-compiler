package mini_python;

import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;

import java.util.HexFormat;

class Compile {

    static boolean debug = false;

    static X86_64 file(TFile f) {
        final X86_64 x86 = new X86_64();
        final TVisitor visitor = new VisitorImpl(x86);

        // Set entry point to label 'main'
        x86.globl(LABEL_MAIN);

        // TODO generalize to compiling all functions
        visitor.visit(f.l.getFirst());

        return x86;
    }

    // Labels reserved by the compiler TODO
    private static final String LABEL_MAIN = "main",
            LABEL_PRINT = "__print",
            LABEL_EQ = "__set_true",
            LABEL_GE = "__ge",
            LABEL_GT = "__gt",
            LABEL_LE = "__le",
            LABEL_LT = "__lt",
            LABEL_NEQ = "__neq";

   /* private static final List<TDef> stantardFunctions = Arrays.asList(
            new __eq()
    );*/

    private static class VisitorImpl implements TVisitor {
        final X86_64 x86;

        //@Nullable
        //String ret;

        VisitorImpl(X86_64 x86) {
            this.x86 = x86;
        }

        int stackPointerValue = 0;

        int dataLabelCounter = 0, textLabelCounter = 0;

        @NotNull
        private String newDataLabel() {
            return "c_" + dataLabelCounter++;
        }

        @NotNull
        private String newTextLabel() {
            return "t_" + textLabelCounter++;
        }

        @NotNull
        private String label(Function function) {
            return "f_" + function.name;
        }

        @Override
        public void visit(TDef tdef) {

            // Add def label (unique because of type)
            x86.label(tdef.f.name);

            // Compile function body
            tdef.body.accept(this);

            // If main, return code 0
            if (tdef.f.name.equals(LABEL_MAIN)) {
                x86.xorq("%rax", "%rax");
                x86.ret();
            }
        }

        @Override
        public void visit(Cnone c) {
            //file.movq("$0", "%rdi"); TODO
        }

        @Override
        public void visit(Cbool c) {
            if (c.b) x86.movq(1, "%rdi");
            else x86.xorq("%rdi", "%rdi");
        }

        @Override
        public void visit(Cstring c) {
            final String label = newDataLabel();
            x86.dlabel(label);

            // Write constant string to .data section at generated label
            x86.string(c.s);

            // Write label to return
            x86.movq(label, "%rdi");
        }

        @Override
        public void visit(Cint c) {
            x86.movq(HexFormat.of().toHexDigits(c.i), "%rdi");
        }

        @Override
        public void visit(TEcst e) {
            if (e.c instanceof Cnone) visit((Cnone) e.c);
            else if (e.c instanceof Cbool) visit((Cbool) e.c);
            else if (e.c instanceof Cstring) visit((Cstring) e.c);
            else if (e.c instanceof Cint) visit((Cint) e.c);
            else throw new CompileError("unsupported constant type");
        }

        @Override
        public void visit(TEbinop e) {

            e.e2.accept(this);      // %rdi = e2
            x86.pushq("%rdi");    // save e2 onto stack
            e.e1.accept(this);      // %rdi = e1
            x86.popq("%rsi");         // %rsi = e2
            x86.addq("%rsi", "%rdi"); // %rdi = %rdi * %rsi = e1 * e2

            // Compile operator
            final String _op1 = "%rdi", _op2 = "%rsi";
            switch (e.op) {
                case Badd:
                    x86.addq(_op2, _op1);
                    break;
                case Beq:
                    x86.cmpq(_op2, _op1);
                    final String skip = newTextLabel(), next = newTextLabel();
                    x86.je(skip);
                    x86.movq(0, "%rdi"); // negative case
                    x86.jmp(next);              //
                    x86.label(skip);            //
                    x86.movq(1, "%rdi"); // positive case
                    x86.label(next);            // exit
                    break;
                case Bge:
                    x86.testq(_op2, _op1);

                    // TODO
                    break;
                case Bgt:
                    // TODO
                    break;
                case Ble:
                    // TODO
                    break;
                case Blt:
                    // TODO
                    break;
                case Bor:
                    x86.orq(_op2, _op1);
                    break;
                case Band:
                    x86.andq(_op2, _op1);
                    break;
                case Bdiv:
                    // TODO
                    //x86.idivq(_op2, _op1);
                    break;
                case Bmod:
                    // TODO
                    //x86.orq(_op2, _op1);
                    break;
                case Bmul:
                    x86.imulq();
                    break;
                case Bneq:
                    // TODO
                    break;
                case Bsub:
                    x86.subq(_op2, _op1);
                    break;
                default:
                    throw new CompileError("unsupported operator " + e.op);
            }
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
            x86.call(e.f.name);
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
            x86.movq("$0", "%rax");
            x86.call("printf");
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
