package mini_python;

import mini_python.annotation.Difference;
import mini_python.annotation.Kills;
import mini_python.annotation.NotNull;
import mini_python.annotation.Nullable;

import java.util.LinkedList;
import java.util.Objects;

/* Abstract Syntax of Mini-Python */

/* Parsed trees.
   This is the output of the parser and the input of the type checker. */

class Ident {
    final String id;
    final Location loc;

    Ident(String id) {
        this.id = id;
        this.loc = null;
    }

    Ident(String id, Location loc) {
        this.id = id;
        this.loc = loc;
    }
}

/* unary and binary operators */

enum Unop {Uneg, Unot}

enum Binop {
    Badd, Bsub, Bmul, Bdiv, Bmod, Beq, Bneq, Blt, Ble, Bgt, Bge, Band, Bor
}

/* constants */

abstract class Constant {
    abstract void accept(Visitor v);

    static final Cnone None = new Cnone();
}

class Cnone extends Constant {
    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Cbool extends Constant {
    final boolean b;

    Cbool(boolean b) {
        this.b = b;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Cstring extends Constant {
    final String s;

    Cstring(String s) {
        this.s = s;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Cint extends Constant {
    final long i; // Python has arbitrary-precision integers; we simplify here

    Cint(long i) {
        this.i = i;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

/* expressions */

abstract class Expr {
    abstract void accept(Visitor v);
}

class Ecst extends Expr {
    final Constant c;

    Ecst(Constant c) {
        this.c = c;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Ebinop extends Expr {
    final Binop op;
    final Expr e1, e2;

    Ebinop(Binop op, Expr e1, Expr e2) {
        super();
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Eunop extends Expr {
    final Unop op;
    final Expr e;

    Eunop(Unop op, Expr e) {
        super();
        this.op = op;
        this.e = e;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Eident extends Expr {
    final Ident x;

    Eident(Ident x) {
        super();
        this.x = x;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Eget extends Expr {
    final Expr e1, e2;

    Eget(Expr e1, Expr e2) {
        super();
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Ecall extends Expr {
    final Ident f;
    final LinkedList<Expr> l;

    Ecall(Ident f, LinkedList<Expr> l) {
        super();
        this.f = f;
        this.l = l;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Elist extends Expr {
    final LinkedList<Expr> l;

    Elist(LinkedList<Expr> l) {
        super();
        this.l = l;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

/* statements */

abstract class Stmt {
    abstract void accept(Visitor v);
}

class Sif extends Stmt {
    final Expr e;
    final Stmt s1, s2;

    Sif(Expr e, Stmt s1, Stmt s2) {
        super();
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sreturn extends Stmt {
    final Expr e;

    Sreturn(Expr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sassign extends Stmt {
    final Ident x;
    final Expr e;

    Sassign(Ident x, Expr e) {
        super();
        this.x = x;
        this.e = e;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sprint extends Stmt {
    final Expr e;

    Sprint(Expr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sblock extends Stmt {
    final LinkedList<Stmt> l;

    Sblock() {
        this.l = new LinkedList<Stmt>();
    }

    Sblock(LinkedList<Stmt> l) {
        super();
        this.l = l;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sfor extends Stmt {
    final Ident x;
    final Expr e;
    final Stmt s;

    Sfor(Ident x, Expr e, Stmt s) {
        super();
        this.x = x;
        this.e = e;
        this.s = s;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Seval extends Stmt {
    final Expr e;

    Seval(Expr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sset extends Stmt {
    final Expr e1, e2, e3;

    Sset(Expr e1, Expr e2, Expr e3) {
        super();
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    @Override
    void accept(Visitor v) {
        v.visit(this);
    }
}

/* function definition and file */

class Def {
    final Ident f;
    final LinkedList<Ident> l; // formal parameters
    final Stmt s;

    Def(Ident f, LinkedList<Ident> l, Stmt s) {
        super();
        this.f = f;
        this.l = l;
        this.s = s;
    }
}

class File {
    final LinkedList<Def> l;
    final Stmt s; // a block of global statements

    File(LinkedList<Def> l, Stmt s) {
        super();
        this.l = l;
        this.s = s;
    }
}

/* visitor for the parsed trees
   (feel free to modify it for your needs) */

interface Visitor {

    /**
     * Function scope is needed when dealing with recursive functions
     */
    @Difference
    void setFunctionScope(Function func);

    /**
     * Returns and flushes value contained in the returned value field.
     *
     * @param returnType Expected return type. Throws an exception on mismatch
     * @param <T>        Expected return type
     * @return Return value. Throws an error if null
     */
    @NotNull
    @Difference
    <T> T ret(Class<T> returnType);

    void visit(Cnone c);

    void visit(Cbool c);

    void visit(Cstring c);

    void visit(Cint c);

    void visit(Ecst e);

    void visit(Ebinop e);

    void visit(Eunop e);

    void visit(Eident e);

    void visit(Ecall e);

    void visit(Eget e);

    void visit(Elist e);

    void visit(Sif s);

    void visit(Sreturn s);

    void visit(Sassign s);

    void visit(Sprint s);

    void visit(Sblock s);

    void visit(Sfor s);

    void visit(Seval s);

    void visit(Sset s);
}

/* Typed trees.

   This is the output of the type checker and the input of the code
   generation.

   In the typed trees, identifiers (objects of class `Ident`) are
   now turned into objects of class `Variable` or `Function`.

   There is also a new class `TErange` for the Python expression
   `list(range(e))`.
*/

/* In the typed trees, all the occurrences of the same variable
   point to a single object of the following class. */
class Variable {
    final String name; // for debugging purposes
    int uid;           // unique id, for debugging purposes
    int ofs;           // position wrt %rbp

    private Variable(String name, int uid) {
        this.name = name;
        this.uid = uid;
        this.ofs = -1; // will be set later, during code generation
    }

    private static int id = 0;

    static Variable mkVariable(String name) {
        return new Variable(name, id++);
    }

    @Difference
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return uid == variable.uid;
    }

    @Difference
    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}

/* Similarly, all the occurrences of a given function all point
   to a single object of the following class. */
class Function {
    final String name;

    /**
     * Parameters of function
     */
    final LinkedList<Variable> params = new LinkedList<>();

    /**
     * Local variables of function
     */
    @Difference
    final LinkedList<Variable> local = new LinkedList<>();

    /**
     * Constructor for class Function
     * @param loc Location
     * @param name Name of the function
     */
    Function(Location loc, String name) {
        this.name = name;
    }
}

abstract class TExpr {
    abstract void accept(TVisitor v);
}

class TEcst extends TExpr {
    final Constant c;

    TEcst(Constant c) {
        this.c = c;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TEbinop extends TExpr {
    final Binop op;
    final TExpr e1, e2;

    TEbinop(Binop op, TExpr e1, TExpr e2) {
        super();
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TEunop extends TExpr {
    final Unop op;
    final TExpr e;

    TEunop(Unop op, TExpr e) {
        super();
        this.op = op;
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TEident extends TExpr {
    final Variable x;

    TEident(Variable x) {
        super();
        this.x = x;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TEget extends TExpr {
    final TExpr e1, e2;

    TEget(TExpr e1, TExpr e2) {
        super();
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "TEget<"+e1.toString()+","+e2.toString()+">";
    }
}

class TEcall extends TExpr {
    final Function f;
    final LinkedList<TExpr> l;

    TEcall(Function f, LinkedList<TExpr> l) {
        super();
        this.f = f;
        this.l = l;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TElist extends TExpr {
    final LinkedList<TExpr> l;

    TElist(LinkedList<TExpr> l) {
        super();
        this.l = l;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TElen extends TExpr {
    final TExpr e;

    TElen(TExpr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

@Difference
class TErange extends TExpr {
    final TExpr e;

    TErange(TExpr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

abstract class TStmt {
    abstract void accept(TVisitor v);
}

class TSif extends TStmt {
    final TExpr e;
    final TStmt s1, s2;

    TSif(TExpr e, TStmt s1, TStmt s2) {
        super();
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSreturn extends TStmt {
    final TExpr e;

    TSreturn(TExpr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSassign extends TStmt {
    final Variable x;
    final TExpr e;

    TSassign(Variable x, TExpr e) {
        super();
        this.x = x;
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSprint extends TStmt {
    final TExpr e;

    TSprint(TExpr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSblock extends TStmt {
    final LinkedList<TStmt> l;

    TSblock() {
        this.l = new LinkedList<TStmt>();
    }

    TSblock(LinkedList<TStmt> l) {
        super();
        this.l = l;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSfor extends TStmt {
    final Variable x;
    final TExpr e;
    final TStmt s;

    TSfor(Variable x, TExpr e, TStmt s) {
        super();
        this.x = x;
        this.e = e;
        this.s = s;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSeval extends TStmt {
    final TExpr e;

    TSeval(TExpr e) {
        super();
        this.e = e;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}

class TSset extends TStmt {
    final TExpr e1, e2, e3;

    TSset(TExpr e1, TExpr e2, TExpr e3) {
        super();
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    @Override
    void accept(TVisitor v) {
        v.visit(this);
    }
}


/* function definition */

class TDef {
    final Function f;
    final TStmt body;

    TDef(Function f, TStmt body) {
        super();
        this.f = f;
        this.body = body;
    }
}

class TFile {
    final LinkedList<TDef> l;
    // the block of global statements is now a `main` function

    TFile() {
        super();
        this.l = new LinkedList<>();
    }
}

/* visitor for the typed trees
   (feel free to modify it for your needs) */

interface TVisitor {

    @Difference
    X86_64 x86();

    /**
     * - Allocates memory in heap
     * - Sets type identifier of new value
     * - Puts address of new value in %rax
     */
    @Difference
    void newValue(Type type, String bytes);

    @Difference
    default void newValue(Type type, int bytes) {
        newValue(type, "$" + bytes);
    }

    @Difference
    void selfCall(int offset);

    @Difference
    String newTextLabel();

    @Difference
    String newDataLabel();

    @NotNull
    @Difference
    @Kills(reg = {"%r10"})
    String ofType(String reg, @Nullable Type caller, String callerFunction, Type... acceptedTypes);

    /**
     * Compiles a fragment of code that checks for given types.
     * If the value in provided register is not one of the
     * requested types, program will exit with an error message.
     *
     * @param reg           Register holding address to value to check
     * @param ifError       Compiled code executed when type does not match
     * @param acceptedTypes Accepted types
     */
    @NotNull
    @Difference
    String ofType(String reg, Runnable ifError, Type... acceptedTypes);

    /**
     * Saves frequently used caller-saved registers and wraps
     * code inside of it
     *
     * @param code Code to perform while caller-saved registers are saved
     * @param regs Registers to save
     */
    @Difference
    void saveRegisters(Runnable code, String... regs);

    @Difference
    void stackAligned(Runnable code);

    @Difference
    void visit(TDef tdef);

    void visit(Cnone c);

    void visit(Cbool c);

    void visit(Cstring c);

    void visit(Cint c);

    void visit(TEcst e);

    void visit(TEbinop e);

    void visit(TEunop e);

    void visit(TEident e);

    void visit(TEcall e);

    void visit(TEget e);

    void visit(TElist e);

    @Difference
    void visit(TErange e);

    void visit(TElen e);

    void visit(TSif s);

    void visit(TSreturn s);

    void visit(TSassign s);

    void visit(TSprint s);

    void visit(TSblock s);

    void visit(TSfor s);

    void visit(TSeval s);

    void visit(TSset s);
}
