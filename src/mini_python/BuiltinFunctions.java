package mini_python;

import mini_python.annotation.Builtin;

public class BuiltinFunctions {

    @Builtin
    public static void __malloc__(X86_64 x86) {
        x86.pushq("%rbp");
        x86.movq("%rsp", "%rbp");
        x86.andq("$-16", "%rsp");
        x86.call("malloc");
        x86.movq("%rbp", "%rsp");
        x86.popq("%rbp");
        x86.ret();
    }

    @Builtin
    public static void __init__(X86_64 x86) {

        // Initialize type descriptor array
        x86.malloc(8 * Compile.TYPES.size()); // address in %rax
        // TODO FOR NOW %r11 CONTAINS TYPE DESCRIPTOR ARRAY
        x86.movq("%rax", Compile.TDA_REG);

        // Register types
        for (Type type : Compile.TYPES)
            type.compileInit(x86);

        x86.jmp(Compile.LABEL_MAIN);
    }
}
