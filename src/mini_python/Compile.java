package mini_python;

import mini_python.annotation.Builtin;
import mini_python.annotation.NotNull;
import mini_python.annotation.Saves;
import mini_python.exception.CompileError;
import mini_python.exception.NotImplementedError;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Compile {

    //region global compilation
    static boolean debug = false;

    public static final List<Type> TYPES = Arrays.asList(Type.NONE, Type.BOOL, Type.INT, Type.STRING, Type.LIST);

    static X86_64 file(TFile f) {
        final X86_64 x86 = new X86_64();
        final TVisitor visitor = new TVisitorImpl(x86);

        // Set entry point
        x86.globl(LABEL_INIT);

        // Write misc builtins
        writeBuiltins(visitor);

        // Compile builtin type methods
        for (Type type : Compile.TYPES)
            type.compileMethods(visitor);

        // Compile all user functions
        for (TDef tdef : f.l)
            visitor.visit(tdef);

        return x86;
    }

    /**
     * Callee-saved register which points toward the type
     * descriptor array. Callee-saved so that NO C methods
     * or system calls later kills this register. It is safe
     * as long as we take the convention NEVER to use it.
     */
    public static final String TDA_REG = "%r15";

    // Labels reserved by the compiler
    public static final String LABEL_INIT = "main", LABEL_MAIN = "__main__";

    //endregion

    //region Built-in functions

    /**
     * Writes all builtin functions to the current code
     *
     * @param v Code where to write builtins
     */
    private static void writeBuiltins(TVisitor v) {

        // Write all functions to the code
        for (Method function : BuiltinFunctions.class.getDeclaredMethods())
            if (function.isAnnotationPresent(Builtin.class)) try {

                // Finally register builtin
                v.x86().label(function.getName());
                function.invoke(null, v);
            } catch (Exception exception) {
                throw new CompileError(exception);
            }
    }
    //endregion
}

class TVisitorImpl implements TVisitor {
    public final X86_64 x86;

    //@Nullable
    //String ret;

    TVisitorImpl(X86_64 x86) {
        this.x86 = x86;
    }

    int dataLabelCounter = 0, textLabelCounter = 0;

    // Set<Variable> assignedVariables = new HashSet<>();

    @NotNull
    private String newDataLabel() {
        return "c_" + dataLabelCounter++;
    }

    @NotNull
    private String newTextLabel() {
        return "t_" + textLabelCounter++;
    }

    @Override
    public X86_64 x86() {
        return x86;
    }

    @Override
    public void malloc(int bytes) {
        x86.movq(bytes, "%rdi");
        x86.call("__malloc__");
    }

    @Override
    public void newValue(Type type, int bytes) {
        malloc(bytes);
        x86.movq(type.getOffset(), "0(%rax)");
    }

    @Override
    public void selfCall(int offset) {
        // %rdi = &[e]
        x86.movq("0(%rdi)", "%r10"); // %r10 = type identifier
        x86.leaq("(" + Compile.TDA_REG + ", %r10, 8)", "%r10"); // %r10 = address of type descriptor
        x86.movq("(%r10)", "%r10"); // %r10 = type descriptor
        x86.callstar(offset + "(%r10)"); // finally call corresponding method
    }

    @Override
    public void saveRegisters(Runnable code, String... regs) {

        // Push
        for (int i = 0; i < regs.length; i++)
            x86.pushq(regs[i]);

        code.run();

        // Pop
        for (int i = 0; i < regs.length; i++)
            x86.popq(regs[regs.length - 1 - i]);
    }

    @Override
    public void err() {
        x86.jmp("__err__");
    }

    @NotNull
    @Override
    @Saves(reg = {"%rax", "%rbx", "%rcx", "%rdx", "%rsi", "%rdi", "%r8", "%r9", "%r11"})
    public String ofType(String reg, Type... acceptedTypes) {
        final String label = newTextLabel();

        // Get type and check
        x86.movq("0(" + reg + ")", "%r10");
        for (Type accepted : acceptedTypes) {
            x86.cmpq(accepted.getOffset(), "%r10");
            x86.je(label);
        }

        // Error, exit program
        err();

        x86.label(label);
        return label;
    }

    @Override
    public void stackAligned(Runnable code) {
        saveRegisters(() -> {
            x86.movq("%rsp", "%rbp");
            x86.andq("$-16", "%rsp");
            code.run();
            x86.movq("%rbp", "%rsp");
        }, "%rbp");
    }

    @Override
    public void visit(TDef tdef) {
        x86.label(tdef.f.name); // Add def label

        int i;
        if (!tdef.f.name.equals(Compile.LABEL_MAIN)) {
            x86.pushq("%rbp"); // Save base pointer
            x86.movq("%rsp", "%rbp"); // Set current base pointer

            if (!tdef.f.local.isEmpty()) { // Make space for local variables
                final int bytes = tdef.f.local.size() * 8;
                x86.addq("$" + bytes, "%rsp");
            }

            i = 0;
            for (Variable var : tdef.f.params) // Set offset of parameters
                var.ofs = 16 + 8 * i++;
        }

        i = 0;
        for (Variable var : tdef.f.local) // Set offset of local/global variables
            var.ofs = -8 - 8 * i++;

        tdef.body.accept(this); // Compile function body

        if (tdef.f.name.equals(Compile.LABEL_MAIN)) { // If main, return code 0
            x86.xorq("%rax", "%rax");
            x86.ret();
        } else visit(new TSreturn(new TEcst(new Cnone()))); // safe return if none
    }

    @Override
    public void visit(Cnone c) {
        x86.movq("$" + none.NONE_LABEL, "%rax");
    }

    @Override
    public void visit(Cbool c) {
        x86.movq("$" + (c.b ? bool.TRUE_LABEL : bool.FALSE_LABEL), "%rax");
    }

    @Override
    public void visit(Cstring c) {
        final String label = newDataLabel();
        x86.dlabel(label);
        x86.quad(Type.STRING.getOffset());
        x86.quad(c.s.length());
        x86.string(c.s);
        x86.movq("$" + label, "%rax");
    }

    @Override
    public void visit(Cint c) {
        final String label = newDataLabel();
        x86.dlabel(label);
        x86.quad(Type.INT.getOffset());
        x86.quad(c.i);
        x86.movq("$" + label, "%rax");
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
        e.e1.accept(this); // %rax = &[e1]
        x86.movq("%rax", "%rdi");

        switch (e.op) {
            case Bor -> {
                final String lblneg = newTextLabel(), lblnxt = newTextLabel();
                selfCall(Type.getOffset("__bool__")); // %rax = &bool([e1])
                x86.cmpq(0, "8(%rax)");
                x86.je(lblneg);
                x86.movq("$" + bool.TRUE_LABEL, "%rax"); // lazy or, early stop
                x86.jmp(lblnxt);

                x86.label(lblneg);
                e.e2.accept(this); // %rax = &[e2]
                x86.movq("%rax", "%rdi");
                selfCall(Type.getOffset("__bool__")); // %rax = &bool([e2])

                x86.label(lblnxt);
            }
            case Band -> {
                final String lblneg = newTextLabel(), lblnxt = newTextLabel();
                selfCall(Type.getOffset("__bool__")); // %rax = &bool([e1])
                x86.cmpq(0, "8(%rax)");
                x86.jne(lblneg);
                x86.movq("$" + bool.FALSE_LABEL, "%rax"); // lazy and, early stop
                x86.jmp(lblnxt);

                x86.label(lblneg);
                e.e2.accept(this); // %rax = &[e2]
                x86.movq("%rax", "%rdi");
                selfCall(Type.getOffset("__bool__")); // %rax = &bool([e2])

                x86.label(lblnxt);
            }
            default -> {
                saveRegisters(() -> {
                    e.e2.accept(this);
                    x86.movq("%rax", "%rsi");
                }, "%rdi");

                selfCall(Type.getOffset(e.op));
            }
        }
    }

    @Override
    public void visit(TEunop e) {
        e.e.accept(this); // %rax = &[e1]
        x86.movq("%rax", "%rdi");
        selfCall(Type.getOffset(e.op));
    }

    @Override
    public void visit(TEident e) {
        final int offset = e.x.ofs;
        if (offset == -1) throw new CompileError("could not identify variable " + e.x.name);
        x86.movq(offset + "(%rbp)", "%rax");
    }

    @Override
    public void visit(TEcall e) {
        for (int i = 0; i < e.l.size(); i++) {
            final int idx = e.l.size() - 1 - i;
            final TExpr arg = e.l.get(idx);
            arg.accept(this);
            x86.pushq("%rax"); // push argument on stack
        }
        x86.call(e.f.name);
    }

    @Override
    public void visit(TEget e) {
        throw new NotImplementedError();
    }

    @Override
    public void visit(TElist e) {
        saveRegisters(() -> {
            final int bytes = 2 + e.l.size();
            newValue(Type.LIST, bytes);
            x86.movq("%rax", "%r12");
            x86.movq(e.l.size(), "8(%r12)");

            int i = 2;
            for (TExpr element : e.l) {
                element.accept(this); // result in %rax
                x86.movq("%rax", 8 * i++ + "(%r12)");
            }
        }, "%r12");
        x86.movq("%r12", "%rax");
    }

    @Override
    public void visit(TErange e) {
        throw new NotImplementedError();
    }

    @Override
    public void visit(TElen e) {
        e.e.accept(this); // %rax = &[e]
        x86.movq("%rax", "%rdi");
        x86.call("__len__"); // %rax = len([e])
    }

    @Override
    public void visit(TSif s) {
        throw new NotImplementedError();
    }

    @Override
    public void visit(TSreturn s) {
        s.e.accept(this); // result in %rax
        x86.movq("%rbp", "%rsp");
        x86.popq("%rbp");
        x86.ret();
    }

    @Override
    public void visit(TSassign s) {
        s.e.accept(this); // %rax = &[e]
        x86.movq("%rax", s.x.ofs + "(%rbp)");
    }

    @Override
    public void visit(TSprint s) {
        s.e.accept(this); // %rax = &[e]
        x86.movq("%rax", "%rdi");
        selfCall(Type.getOffset("__print__"));
    }

    @Override
    public void visit(TSblock s) {
        for (TStmt stmt : s.l)
            stmt.accept(this);
    }

    @Override
    public void visit(TSfor s) {
        throw new NotImplementedError();
    }

    @Override
    public void visit(TSeval s) {
        s.e.accept(this);
    }

    @Override
    public void visit(TSset s) {
        throw new NotImplementedError();
    }
}
