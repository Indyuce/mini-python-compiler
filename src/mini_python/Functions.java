package mini_python;

import java.util.function.Consumer;

@Deprecated
public class Functions {

    @Deprecated
    private static final Consumer<X86_64>

            mem_alloc = x86 -> {
        x86.pushq("%rbp");
        x86.movq("%rsp", "%rbp");
        x86.andq("$-16", "%rsp");
        x86.call("malloc");
        x86.movq("%rbp", "%rsp");
        x86.popq("%rbp");
        x86.ret();
    };
}
