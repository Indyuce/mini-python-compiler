package mini_python;

import mini_python.annotation.Builtin;
import mini_python.annotation.Kills;
import mini_python.annotation.NotNull;
import mini_python.annotation.Nullable;
import mini_python.exception.CompileError;
import mini_python.exception.CompileException;

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

        x86.section(".section  .note.GNU-stack, \"\", @progbits");
        // Set entry point
        x86.globl(LABEL_MAIN);

        // Register types and their functions
        Type.registerTypes(visitor);

        RuntimeErr.registerConstants(visitor); // Register constants for errors
        writeBuiltinConstants(visitor); // Write static builtin function constants
        writeBuiltins(visitor); // Write static builtin functions

        // Compile all user functions
        for (TDef tdef : f.l)
            visitor.visit(tdef);

        return x86;
    }

    // Labels reserved by the compiler
    public static final String LABEL_MAIN = "main"; // Entry point of the program assembly_wise.

    public static final String FUNCTION_PREFIX = "f_"; // Prefix for every use defined function.

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

    private static void writeBuiltinConstants(TVisitor v) {

        v.x86().dlabel(Type.methodNameLabel("__get__"));
        v.x86().string("__get__");

        v.x86().dlabel(Type.methodNameLabel("__set__"));
        v.x86().string("__set__");
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
    @Override
    public String newDataLabel() {
        return "DL" + dataLabelCounter++;
    }

    @NotNull
    @Override
    public String newTextLabel() {
        return "TL" + textLabelCounter++;
    }

    @Override
    public X86_64 x86() {
        return x86;
    }

    @Override
    public void newValue(Type type, String bytes) {
        x86.movq(bytes, "%rdi");
        x86.call("__malloc__");
        x86.movq(type.classDesc(), "0(%rax)");
    }

    @Override
    public void selfCall(int offset) {
        // %rdi = &[e]
        x86.movq("0(%rdi)", "%r10"); // %r10 = address of class descriptor
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
    public String ofType(String reg, @Nullable Type caller, String callerFunction, Type... acceptedTypes) {
        return ofType(reg, () -> RuntimeErr.invalidArgType(this, caller, callerFunction, reg), acceptedTypes);
    }

    @NotNull
    @Override
    @Kills(reg = {"%r10"})
    public String ofType(String reg, Runnable ifError, Type... acceptedTypes) {
        final String label = newTextLabel();

        // Get type and check
        x86.movq("0(" + reg + ")", "%r10");
        for (Type accepted : acceptedTypes) {
            x86.cmpq(accepted.classDesc(), "%r10");
            x86.je(label);
        }

        // Error, exit program
        ifError.run();

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

    // Same as stackAligned, but returns
    public void stackAlignedReturn(Runnable code) {
        saveRegisters(() -> {
            x86.movq("%rsp", "%rbp");
            x86.andq("$-16", "%rsp");
            code.run();
            x86.movq("%rbp", "%rsp");
        }, "%rbp");
        x86.ret();
    }

    @Override
    public void visit(TDef tdef) {

        if (tdef.f.name.equals(Compile.LABEL_MAIN)) {
            // All function calls and compilations are prefixed with f_
            // main being special, it is not.
            x86.label(Compile.LABEL_MAIN);
        } else {
            // the prefix is added per typing. The compiler needs not to know.
            x86.label(tdef.f.name);
        }

        int i;
        if (!tdef.f.name.equals(Compile.LABEL_MAIN)) {
            x86.pushq("%rbp"); // Save base pointer
            x86.movq("%rsp", "%rbp"); // Set current base pointer

            if (!tdef.f.local.isEmpty()) { // Make space for local variables
                final int bytes = tdef.f.local.size() * 8;
                x86.subq("$" + bytes, "%rsp");
            }

            i = 0;
            for (Variable var : tdef.f.params) // Set offset of parameters
                var.ofs = 16 + 8 * i++;
        } else {

            int total_nums_of_bytes = 0;
            if (!tdef.f.local.isEmpty()) { // Make space for local variables
                final int bytes = tdef.f.local.size() * 8;
                total_nums_of_bytes += bytes;
            }
            x86.pushq("%rbp"); // Save base pointer
            x86.movq("%rsp", "%rbp"); // Set current base pointer
            x86.subq("$" + total_nums_of_bytes, "%rsp");

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
            x86.movq("%rbp", "%rsp");
            x86.popq("%rbp");
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
        x86.quadLabel(Type.STRING.classDescLabel());
        x86.quad(c.s.length()+1);
        x86.string(c.s+"\\0");
        x86.movq("$" + label, "%rax");
    }

    @Override
    public void visit(Cint c) {
        final String label = newDataLabel();
        x86.dlabel(label);
        x86.quadLabel(Type.INT.classDescLabel());
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
        // There is a special case where e calls __main__
        // Behavior is undefined in that case.

        for (int i = 0; i < e.l.size(); i++) {
            final int idx = e.l.size() - 1 - i;
            final TExpr arg = e.l.get(idx);
            arg.accept(this);
            x86.pushq("%rax"); // push argument on stack
        }

        x86.call(e.f.name);

        // We did many pushes we need to undo here
        for (int i = 0; i < e.l.size(); i++) {
            x86.addq("$8", "%rsp"); // Pop without storing basically
        }
    }

    @Override
    public void visit(TEget e) {
        e.e1.accept(this);
        ofType("%rax", Type.LIST, "__get__", Type.LIST); // %rax = &[list]

        saveRegisters(() -> {
            e.e2.accept(this);
            ofType("%rax", () -> RuntimeErr.invalidIndexType(this, "%rax"), Type.INT);
            x86.movq("8(%rax)", "%rsi"); // %rsi = int value
        }, "%rax");

        x86.leaq("16(%rax, %rsi, 8)", "%rax");
        x86.movq("(%rax)", "%rax");
    }

    @Override
    public void visit(TElist e) {
        saveRegisters(() -> {
            final int bytes = 16 + 8 * e.l.size();
            newValue(Type.LIST, bytes);
            x86.movq("%rax", "%r12");
            x86.movq(e.l.size(), "8(%r12)");

            int i = 2;
            for (TExpr element : e.l) {
                element.accept(this); // result in %rax
                x86.movq("%rax", 8 * i++ + "(%r12)");
            }
            x86.movq("%r12", "%rax");
        }, "%r12");
    }

    @Override
    public void visit(TErange e) {
        e.e.accept(this);
        x86.movq("%rax", "%rdi");
        x86.call("range");
    }

    @Override
    public void visit(TElen e) {
        e.e.accept(this); // %rax = &[e]
        x86.movq("%rax", "%rdi");
        x86.call("len"); // %rax = len([e])
    }

    @Override
    public void visit(TSif s) {
        final String neg = newTextLabel(), nxt = newTextLabel();
        s.e.accept(this); // %rax = &[bool]
        x86.cmpb(0, "8(%rax)");
        x86.je(neg);
        s.s1.accept(this); // positive statement
        x86.jmp(nxt);
        x86.label(neg); // negative statement
        s.s2.accept(this);
        x86.label(nxt);
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
        x86.movq("$" + string.LINE_BREAK_LABEL, "%rdi"); // print line break right after
        x86.call("__printf__");
    }

    @Override
    public void visit(TSblock s) {
        for (TStmt stmt : s.l)
            stmt.accept(this);
    }

    @Override
    public void visit(TSfor s) {
        s.e.accept(this);
        ofType("%rax", () -> RuntimeErr.forRequiresList(this), Type.LIST);
        saveRegisters(() -> list.iter(this, "%rax", newTextLabel(), "%r12", "%r13", () -> {
            x86.movq("(%r12)", "%r10");
            x86.movq("%r10", s.x.ofs + "(%rbp)"); // load value of list at given index into variable
            s.s.accept(this);
        }), "%r12", "%r13");
    }

    @Override
    public void visit(TSeval s) {
        s.e.accept(this);
    }

    @Override
    public void visit(TSset s) {
        s.e1.accept(this);
        ofType("%rax", Type.LIST, "__set__", Type.LIST); // %rax = &[list]

        saveRegisters(() -> {
            s.e2.accept(this);
            // ofType("%rax", () -> RuntimeErr.invalidIndexType(this, "%rax"), Type.LIST);
            // The above does not make sense : we can affect both integers and lists
            x86.movq("8(%rax)", "%rcx"); // %rcx = [int]
            saveRegisters(() -> {
                s.e3.accept(this);
                x86.movq("%rax", "%rdx"); // %rdx = &[e]
            }, "%rcx");
        }, "%rax");

        x86.leaq("16(%rax, %rcx, 8)", "%rsi"); // compute address
        x86.movq("%rdx", "(%rsi)"); // set element
    }
}
