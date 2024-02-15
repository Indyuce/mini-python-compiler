package mini_python;

import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;
import mini_python.exception.NotImplementedError;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class Compile {

    //region global complation

    static boolean debug = false;

    static X86_64 file(TFile f) {
        final X86_64 x86 = new X86_64();
        final TVisitor visitor = new VisitorImpl(x86);

        registerStaticConstants(x86);

        // Set entry point
        x86.globl(LABEL_MAIN);

        // Compile all functions
        for (TDef tdef : f.l)
            visitor.visit(tdef);

        return x86;
    }

    private static void registerStaticConstants(X86_64 x86) {
        x86.dlabel(LABEL_BOOL_TRUE);
        x86.quad(0); // Change to bool type descriptor addess
        x86.quad(1);
        x86.dlabel(LABEL_BOOL_FALSE);
        x86.quad(0);
        x86.quad(0);
        x86.dlabel(LABEL_NONE_NONE);
        x86.quad(0);
        x86.quad(0);
    }

    // Labels reserved by the compiler
    public static final String

            // Other reserved labels
            LABEL_MAIN = "__main__",

    // Static constant labels
    LABEL_BOOL_TRUE = "__bool__True", LABEL_BOOL_FALSE = "__bool__False", LABEL_NONE_NONE = "__none__None",

    // Type labels (addresses of type descriptors)
    LABEL_NONE = "__none__", LABEL_BOOL = "__bool__", LABEL_INT = "__int__", LABEL_STRING = "__string__", LABEL_LIST = "__list__";

    //endregion

    //region visitor implementation

    private static class VisitorImpl implements TVisitor {
        final X86_64 x86;

        /**
         * Type descriptor of 'object'
         */
        final ObjectTypeDescriptor descriptor = new ObjectTypeDescriptor();

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
            /*malloc(2);
            x86.movq(LABEL_NONE, "0(%rax)");
            x86.movq("0", "8(%rax)");
            x86.movq("%rax", "%rdi");*/

            x86.movq(LABEL_NONE_NONE, "%rdi");
        }

        /**
         * Compiles allocating memory. At the end of these
         * instructions, the address of allocated memory
         * can be found in the %rax register.
         *
         * @param bytes Bytes being allocated
         */
        private void malloc(int bytes) {
            x86.movq(bytes, "%rdi");
            x86.call("malloc");
        }

        @Override
        public void visit(Cbool c) {
            /*malloc(2);
            x86.data();
            x86.movq(1, "8(%rax)");
            x86.movq("%rax", "%rdi");*/

            x86.movq(c.b ? LABEL_BOOL_TRUE : LABEL_BOOL_FALSE, "%rdi");
        }

        @Override
        public void visit(Cstring c) {
            final String label = newDataLabel();
            x86.dlabel(label);
            x86.string(c.s);
            x86.movq(label, "%rdi");
        }

        @Override
        public void visit(Cint c) {
            final String label = newDataLabel();
            x86.dlabel(label);
            x86.quad(c.i);
            x86.movq(label, "%rdi");
        }

        @Override
        public void visit(TEcst e) {
            if (e.c instanceof Cnone) visit((Cnone) e.c);
            else if (e.c instanceof Cbool) visit((Cbool) e.c);
            else if (e.c instanceof Cstring) visit((Cstring) e.c);
            else if (e.c instanceof Cint) visit((Cint) e.c);
            else throw new CompileError("unsupported constant type");
        }

      /*  private boolean isLazy(Binop binop) {
            return binop == Binop.Bor || binop == Binop.Band;
        }*/

        /**
         * Binary operators are compiled this way:
         * - Address of value of first argument is put in %rdi
         * - Address of value of second argument is put in %rsi
         * - Method corresponding to binop retrieved from type descriptor of *%rdi
         * - Method is called placing result in %rdi
         */
        @Override
        public void visit(TEbinop e) {

            e.e1.accept(this);
            x86.pushq("%rdi"); // put &[e1] on stack

            // TODO check for laziness before compiling [e2]

            final ObjectTypeDescriptor.Method method = ObjectTypeDescriptor.Method.from(e.op);
            final int offset = method.ofs();

            x86.movq("(%rdi)", "%r10"); // %r10 = type descriptor of type([e1])
            x86.callstar(offset + "(%r10)");
        }

        @Override
        public void visit(TEunop e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TEident e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TEcall e) {
            // Use name of function for call
            x86.call(e.f.name);
        }

        @Override
        public void visit(TEget e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TElist e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TErange e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TElen e) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSif s) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSreturn s) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSassign s) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSprint s) {
            s.e.accept(this);
            x86.movq("$0", "%rax");
            x86.call("printf");
        }

        @Override
        public void visit(TSblock s) {
            for (TStmt stmt : s.l)
                stmt.accept(this);
        }

        @Override
        public void visit(TSfor s) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSeval s) {
            throw new NotImplementedError("not implemented");
        }

        @Override
        public void visit(TSset s) {
            throw new NotImplementedError("not implemented");
        }
    }

    //endregion

    //region Built-in functions

    /**
     * Writes all builtin functions to the current code
     *
     * @param x86 Code where to write builtins
     */
    private void writeBuiltins(X86_64 x86) {

        // Write all functions to the code
        builtins.forEach((id, code) -> {
            x86.label("__" + id);
            code.accept(x86);
        });
    }

    private static final Map<String, Consumer<X86_64>> builtins = new HashMap<>();

    static {
        builtins.put("int_add", x86 -> {
            // &[e2]
            // &[e1]
            // ....
            x86.popq("%rdi"); // rdi = &[e1]
            x86.popq("%rsi"); // rsi = &[e2]

            // Make sure %rsi is

            x86.movq("8(%rdi)", "%rdi"); // rdi = [e1]
            x86.movq("8(%rsi)", "%rsi"); // rsi = [e2]
            x86.addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
            x86.ret();
        });
    }

    //endregion
}
