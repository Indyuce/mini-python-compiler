package mini_python;

import mini_python.annotation.Builtin;

public class BuiltinFunctions {

    @Builtin
    public static void __malloc__(TVisitor v) {
        v.stackAligned(() -> v.x86().call("malloc"));
        v.x86().ret();
    }

    @Builtin
    public static void __printf__(TVisitor v) {
        v.stackAligned(() -> {
            v.x86().xorq("%rax", "%rax");
            v.x86().call("printf");
        });
        v.x86().ret();
    }

    @Builtin
    public static void __strcpy__(TVisitor v) {
        v.stackAligned(() -> v.x86().call("strcpy"));
        v.x86().ret();
    }

    @Builtin
    public static void __strcat__(TVisitor v) {
        v.stackAligned(() -> v.x86().call("strcat"));
        v.x86().ret();
    }

    @Builtin
    public static void __strcmp__(TVisitor v) {
        v.stackAligned(() -> v.x86().call("strcmp"));
        v.x86().ret();
    }

    @Builtin
    public static void len(TVisitor v) {
        v.x86().dlabel(Type.methodNameLabel("len"));
        v.x86().string("len");

        v.ofType("%rdi", null, "len", Type.STRING, Type.LIST);

        v.saveRegisters(() -> v.newValue(Type.INT, 16), "%rdi"); // %rax = &[new int]
        v.x86().movq("8(%rdi)", "%r10"); // get length of string/list
        v.x86().movq("%r10", "8(%rax)"); // write length into new int

        v.x86().ret();
    }

    @Builtin
    public static void range(TVisitor v) {
        v.x86().dlabel(Type.methodNameLabel("range"));
        v.x86().string("range");

        v.saveRegisters(() -> {
            v.ofType("%rdi", null, "range", Type.INT);
            v.x86().movq("8(%rdi)", "%r13"); // %r13 = max counter
            v.x86().xorq("%r12", "%r12"); // %r12 = current counter

            v.x86().leaq("16(, %r13, 8)", "%rdi"); // Allocate memory for list
            v.x86().call("__malloc__");
            v.x86().movq("%rax", "%r14"); // %r14 = &[list]
            v.x86().movq(Type.LIST.classDesc(), "0(%r14)"); // write type identifier
            v.x86().movq("%r13", "8(%r14)"); // write length

            final String loop = v.newTextLabel(), end = v.newTextLabel();
            v.x86().label(loop); // Main loop label
            v.x86().cmpq("%r12", "%r13");
            v.x86().je(end);
            v.newValue(Type.INT, 16); // new value to be written to list
            v.x86().movq("%r12", "8(%rax)"); // write value
            v.x86().leaq("16(%r14, %r12, 8)", "%r10"); // target address of new value, i LOVE leaq
            v.x86().movq("%rax", "(%r10)");
            v.x86().incq("%r12");
            v.x86().jmp(loop);

            v.x86().label(end); // End of loop
            v.x86().movq("%r14", "%rax");
        }, "%r12", "%r13", "%r14");
        v.x86().ret();
    }

    @Builtin
    public static void __err__(TVisitor v) {
        v.x86().movq(60, "%rax"); // syscall number
        v.x86().movq(1, "%rdi"); // error code in %rdi
        v.x86().emit("syscall");
    }
}
