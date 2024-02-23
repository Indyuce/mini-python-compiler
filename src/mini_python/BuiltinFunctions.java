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
    public static void main(TVisitor v) {

        v.malloc(8 * Compile.TYPES.size()); // Allocate memory for TDA
        v.x86().movq("%rax", Compile.TDA_REG);

        // Register types
        for (Type type : Compile.TYPES)
            type.compileInit(v);

        v.x86().jmp(Compile.LABEL_MAIN);
    }

    @Builtin
    public static void __len__(TVisitor v) {
        v.ofType("%rdi", Type.STRING, Type.LIST);

        v.saveRegisters(() -> v.newValue(Type.INT, 16), "%rdi"); // %rax = &[new int]
        v.x86().movq("8(%rdi)", "%r10"); // get length of string/list
        v.x86().movq("%r10", "8(%rax)"); // write length into new int

        v.x86().ret();
    }

    @Builtin
    public static void __err__(TVisitor v) {
        v.x86().movq(60, "%rax"); // syscall number
        v.x86().movq(1, "%rdi"); // error code in %rdi
        v.x86().emit("syscall");
    }
}
