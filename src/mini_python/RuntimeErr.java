package mini_python;

import mini_python.annotation.Nullable;

import java.util.Objects;

public class RuntimeErr {
    public static void registerConstants(TVisitor v) {
        for (ErrorEnum err : ErrorEnum.values()) {
            v.x86().dlabel(err.lbl);
            v.x86().string(err.message);
        }
    }

    public static void methodNotDefined(TVisitor v, Type callerType, String callerFunction) {
        v.x86().movq("$" + ErrorEnum.METHOD_NOT_DEFINED.lbl, "%rdi");
        v.x86().movq("$" + Type.methodNameLabel(callerFunction), "%rsi"); // address to method name string
        v.x86().movq(callerType.classDesc(), "%rdx");
        v.x86().movq("0(%rdx)", "%rdx"); // address to class name string
        compileThrow(v);
    }

    public static void forRequiresList(TVisitor v) {
        v.x86().movq("$" + ErrorEnum.FOR_REQUIRES_LIST.lbl, "%rdi");
        compileThrow(v);
    }

    public static void invalidArgType(TVisitor v, @Nullable Type callerType, String callerFunction, String paramReg) {
        v.x86().movq("(" + paramReg + ")", "%rcx");
        v.x86().movq("(%rcx)", "%rcx"); // address to param class name string
        v.x86().movq("$" + ErrorEnum.INVALID_ARG_TYPE.lbl, "%rdi");
        v.x86().movq("$" + Type.methodNameLabel(callerFunction), "%rsi"); // address to method name string
        v.x86().movq(Objects.requireNonNullElse(callerType, Type.NONE).classDesc(), "%rdx");
        v.x86().movq("0(%rdx)", "%rdx");  // address to object caller class name string
        compileThrow(v);
    }

    public static void invalidIndexType(TVisitor v) {
        v.x86().movq("$" + ErrorEnum.INVALID_INDEX_TYPE.lbl, "%rdi");
        //   v.x86().movq(callerType.classDesc(), "%rsi");
        // TODO
        v.x86().movq("0(%rdx)", "%rdx");
        compileThrow(v);
    }

    private static void compileThrow(TVisitor v) {
        v.x86().call("__printf__"); // print error msg
        v.x86().call("__err__"); // exit program
    }

    enum ErrorEnum {
        @Deprecated
        TEST("this is a test error message!!!!!!!!!!!!!!!!!!!!!"),
        METHOD_NOT_DEFINED("method '%s' not defined for type '%s'"),
        INVALID_INDEX_TYPE("requires index of type int, given '%s'"),
        INVALID_ARG_TYPE("method '%s' of type '%s' does not accept arg of type '%s'"),
        FOR_REQUIRES_LIST("for loop requires a list to iterate through");

        final String lbl, message;

        ErrorEnum(String message) {
            this.lbl = "__errmsg__" + name().toLowerCase() + "__";
            this.message = message + "\n";
        }
    }
}
