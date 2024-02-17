package mini_python;

import mini_python.annotation.Builtin;

import java.util.function.Consumer;

public class Functions {

    @Builtin
    private static final Consumer<X86_64>

            mem_alloc = x86 -> {
        x86.pushq("%rbp");
        x86.movq("%rsp", "%rbp");
        x86.andq("$-16", "%rsp");
        x86.call("malloc");
        x86.movq("%rbp", "%rsp");
        x86.popq("%rbp");
        x86.ret();
    },

    //region bool methods
    //endregion

    //region int methods

    int_add = x86 -> {
        // &[e2]
        // &[e1]
        // ....
        x86.popq("%rdi"); // rdi = &[e1]
        x86.popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        x86.movq("8(%rdi)", "%rdi"); // rdi = [e1]
        x86.movq("8(%rsi)", "%rsi"); // rsi = [e2]
        x86.addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        x86.ret();
    },

    int_sub = x86 -> {
        // &[e2]
        // &[e1]
        // ....
        x86.popq("%rdi"); // rdi = &[e1]
        x86.popq("%rsi"); // rsi = &[e2]

        // TODO Make sure %rsi is of type int?

        x86.movq("8(%rdi)", "%rdi"); // rdi = [e1]
        x86.movq("8(%rsi)", "%rsi"); // rsi = [e2]
        x86.addq("%rsi", "%rdi"); // rdi = [e1] + [e2]
        x86.ret();
    };
    //endregion

    //region string methods
    //endregion

    //region list methods
    //endregion
}
