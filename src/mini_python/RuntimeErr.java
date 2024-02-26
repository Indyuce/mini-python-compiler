package mini_python;

public class RuntimeErr {
    public static void registerConstants(TVisitor v) {
        for (ErrorEnum err : ErrorEnum.values()) {
            v.x86().dlabel(err.lbl);
            v.x86().string(err.message);
        }
    }

    public static void methodNotDefined(TVisitor v, Type callerType, String callerFunction) {
        v.x86().movq("$" + ErrorEnum.METHOD_NOT_DEFINED.lbl, "%rdi");
        v.x86().movq("$" + Type.methodNameLabel(callerFunction), "%rsi");
        v.x86().movq(callerType.classDesc(), "%rdx");
        v.x86().movq("8(%rdx)", "%rdx");
        compileThrow(v);
    }

    public static void invalidIndexType(TVisitor v, Type callerType, String callerFunction) {
        v.x86().movq("$" + ErrorEnum.INVALID_INDEX_TYPE.lbl, "%rdi");
        v.x86().movq("$" + Type.methodNameLabel(callerFunction), "%rsi");
        v.x86().movq(callerType.classDesc(), "%rdx");
        v.x86().movq("8(%rdx)", "%rdx");
        compileThrow(v);
    }

    private static void compileThrow(TVisitor v) {
        v.x86().call("__print__"); // print error msg
        v.x86().call("__er__"); // exit program
    }

    enum ErrorEnum {
        METHOD_NOT_DEFINED("method '%s' not defined for type '%s'"),
        INVALID_INDEX_TYPE("requires index of type int, given '%s'"),
        INVALID_ARG_TYPE("method '%s' of type '%s' does not accept arg of type '%s'"),

        ;

        final String lbl, message;

        ErrorEnum(String message) {
            this.lbl = "__errmsg__" + name().toLowerCase() + "__";
            this.message = message;
        }
    }
}
