package mini_python;

public enum RuntimeErr {
    METHOD_NOT_DEFINED("method '%s' not defined for type '%s'"),

    ;

    final String lbl, message;

    RuntimeErr(String message) {
        this.lbl = "__ermsg__" + name().toLowerCase() + "";
        this.message = message;
    }

    public static void registerConstants(TVisitor v) {
        for (RuntimeErr err : values()) {
            v.x86().dlabel(err.lbl);
            v.x86().string(err.message);
        }
    }

    public void compileThrow(TVisitor v) {
        v.x86().movq("$" + lbl, "%rdi");
        v.x86().call("__print__"); // print error msg
        v.x86().call("__er__"); // exit program
    }
}
