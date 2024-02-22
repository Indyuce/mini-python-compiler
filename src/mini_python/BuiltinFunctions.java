package mini_python;

import mini_python.annotation.Builtin;

public class BuiltinFunctions {

    @Builtin
    public static void __malloc__(TVisitor v) {
        v.x86().pushq("%rbp");
        v.x86().movq("%rsp", "%rbp");
        v.x86().andq("$-16", "%rsp");
        v.x86().call("malloc");
        v.x86().movq("%rbp", "%rsp");
        v.x86().popq("%rbp");
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
        // TODO
    }

    @Builtin
    public static void __err__(TVisitor v) {
        v.x86().movq(60, "%rax"); // Syscall number
        v.x86().movq(1, "%rdi"); // Error code in %rdi
        v.x86().emit("syscall");
    }
}
