package mini_python;

import mini_python.annotation.BuiltinMethod;
import mini_python.annotation.NotNull;
import mini_python.exception.CompileError;
import mini_python.exception.NotImplementedError;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class Compile {

    //region global compilation
    static boolean debug = false;

    private static final List<Type> TYPES = Arrays.asList(Type.NONE, Type.BOOL, Type.INT, Type.STRING, Type.LIST);

    static X86_64 file(TFile f) {
        final X86_64 x86 = new X86_64();
        final TVisitor visitor = new VisitorImpl(x86);

        // Write standard types and functions
        for (Type type : TYPES)
            type.register(x86);

        // Write misc builtins
        writeBuiltins(x86);

        // Set entry point
        x86.globl(LABEL_MAIN);

        // Compile all functions
        for (TDef tdef : f.l)
            visitor.visit(tdef);

        return x86;
    }

    public static final String TDA_REG = "%r11";

    // Labels reserved by the compiler
    public static final String LABEL_MAIN = "__main__";

    //endregion

    //region visitor implementation

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

            x86.movq(none.NONE, "%rdi");
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

            x86.movq(c.b ? bool.TRUE : bool.FALSE, "%rdi");
        }

        @Override
        public void visit(Cstring c) {
            final String label = newDataLabel();
            x86.dlabel(label);
            x86.quad(Type.STRING.ofs());
            x86.quad(c.s.length());
            x86.string(c.s);
            x86.movq(label, "%rdi");
        }

        @Override
        public void visit(Cint c) {
            final String label = newDataLabel();
            x86.dlabel(label);
            x86.quad(Type.INT.ofs());
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

            final BuiltinMethod method = Type.fromName(e.op);
            final int offset = method.ofs();

            x86.movq("0(%rdi)", "%r10"); // %r10 = type descriptor of type([e1])
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

        /**
         * @param acceptedTypes Types accepted.
         * @return Code for checking type of
         */
        @NotNull
        private String checkOfType(int... acceptedTypes) {
            final String label = newTextLabel();

            // Get type, then check
            x86.movq("0(%rdi)", "%r10");
            for (int accepted : acceptedTypes) {
                x86.cmpq(accepted, "%r10");
                x86.je(label);
            }

            // Error, exit program
            //x86

            x86.label(label);
            return label;
        }
    }

    //endregion

    //region Built-in functions

    /**
     * Writes all builtin functions to the current code
     *
     * @param x86 Code where to write builtins
     */
    private static void writeBuiltins(X86_64 x86) {
        try {

            // Write all functions to the code
            for (Field field : Functions.class.getDeclaredFields())
                if (field.isAnnotationPresent(BuiltinMethod.class)) {
                    final Consumer<X86_64> code = (Consumer<X86_64>) field.get(null);
                    final String label = field.getName();

                    // Finally register builtin
                    x86.label("__" + label);
                    code.accept(x86);
                }


        } catch (Throwable throwable) {
            throw new Error("could not write builtin function: " + throwable.getMessage());
        }
    }
    //endregion
}
