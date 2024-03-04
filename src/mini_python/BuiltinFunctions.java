package mini_python;

import mini_python.annotation.Assembly;
import mini_python.annotation.Builtin;
import mini_python.annotation.Kills;

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
    public static void __len__(TVisitor v) {
        v.x86().dlabel(Type.methodNameLabel("__len__"));
        v.x86().string("len");

        v.ofType("%rdi", null, "__len__", Type.STRING, Type.LIST);

        v.saveRegisters(() -> v.newValue(Type.INT, 16), "%rdi"); // %rax = &[new int]
        v.x86().movq("8(%rdi)", "%r10"); // get length of string/list
        v.x86().movq("%r10", "8(%rax)"); // write length into new int

        v.x86().ret();
    }

    /**
     * %rdi <= &[list1]
     * %rsi <= &[list2]
     * %rax => &[list] with the lowest length
     * %rcx => &[list] with the highest length
     * %rdx => &bool(len(l1) < len(l2))
     */
    @Builtin
    @Kills(reg = {"%rax", "%rcx", "%rdx"})
    public static void __comp__length__list__(TVisitor v) {
        v.x86().movq("8(%rsi)", "%rcx");
        v.x86().cmpq("%rcx", "8(%rdi)");
        v.x86().js("__min__2__");
        v.x86().movq("%rsi", "%rax");
        v.x86().movq("%rdi", "%rcx");
        v.x86().movq("$" + bool.FALSE_LABEL, "%rdx");
        v.x86().ret();
        v.x86().label("__min__2__"); // len(l1) < len(l2)
        v.x86().movq("%rdi", "%rax");
        v.x86().movq("%rsi", "%rcx");
        v.x86().movq("$" + bool.TRUE_LABEL, "%rdx");
        v.x86().ret();
    }

    /**
     * Evaluates %rdi<=%rsi
     *
     * @param v Visitor
     */
    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __comp__lt__int__(TVisitor v) {
        // Perform %rdi <= %rsi
        // Return true
        v.x86().movq("8(%rdi)", "%r8");
        v.x86().movq("8(%rsi)", "%r9");

        v.x86().cmpq("%r8", "%r9");
        v.x86().jl("__comp__lt__int__if__");
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label("__comp__lt__int__if__");
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();
    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __comp__le__int__(TVisitor v) {
        // Perform %rdi <= %rsi
        // Return true
        v.x86().movq("8(%rdi)", "%r8");
        v.x86().movq("8(%rsi)", "%r9");

        v.x86().cmpq("%r8", "%r9");
        v.x86().jle("__comp__le__int__if__");
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label("__comp__le__int__if__");
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();
    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __comp__gt__int__(TVisitor v) {
        // Perform %rdi <= %rsi
        // Return true
        v.x86().movq("8(%rdi)", "%r8");
        v.x86().movq("8(%rsi)", "%r9");

        v.x86().cmpq("%r8", "%r9");
        v.x86().jg("__comp__gt__int__if__");
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label("__comp__gt__int__if__");
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();
    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __comp__ge__int__(TVisitor v) {
        // Perform %rdi <= %rsi
        // Return true
        v.x86().movq("8(%rdi)", "%r8");
        v.x86().movq("8(%rsi)", "%r9");

        v.x86().cmpq("%r8", "%r9");
        v.x86().jge("__comp__ge__int__if__");
        v.x86().movq("$" + bool.FALSE_LABEL, "%rax");
        v.x86().ret();

        v.x86().label("__comp__ge__int__if__");
        v.x86().movq("$" + bool.TRUE_LABEL, "%rax");
        v.x86().ret();
    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __lt__(TVisitor v) {

    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __le__(TVisitor v) {

    }

    @Builtin
    @Kills(reg = {"%rax", "%r8", "%r9"})
    public static void __gt__(TVisitor v) {

    }


    @Builtin
    public static void __err__(TVisitor v) {
        v.x86().movq(60, "%rax"); // syscall number
        v.x86().movq(1, "%rdi"); // error code in %rdi
        v.x86().emit("syscall");
    }
}
