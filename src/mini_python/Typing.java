package mini_python;

import mini_python.annotation.NotNull;
import mini_python.annotation.Nullable;
import mini_python.exception.TypeError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that takes as input parsed trees and outputs typed trees.
 *
 * @see #file(File)
 */
class Typing {
    public static final String USER_DEFINED_FUNCTION_PREFIX = "f_"; // To be appended to all user defined functions names
    private static final List<String> RESERVED_FUNCTION_NAMES = Arrays.asList("list", "len", "range", "print");
    static boolean debug = false;

    static TFile file(File f) {

        // Typed file
        final TFile tf = new TFile();
        final Function mainFunction = new Function(Location.NONE, Compile.LABEL_MAIN);
        final Visitor visitor = new VisitorImpl(tf, mainFunction);

        // All functions
        for (Def def : f.l) {

            // Reserved function name
            if (Typing.RESERVED_FUNCTION_NAMES.contains(def.f.id))
                throw new TypeError(def.f.loc, "tried defining function with reserved identifier '" + def.f.id + "'");

            // Define new typed function
            final Function defFunction = new Function(def.f.loc, USER_DEFINED_FUNCTION_PREFIX + def.f.id);

            // Pairwise distinct function identifiers
            for (TDef sofar : tf.l)
                if (sofar.f.name.equals(defFunction.name))
                    throw new TypeError(def.f.loc, "duplicate function definition '" + defFunction.name + "'");

            // Iterate over parameters
            for (Ident paramIdent : def.l) {

                // Pairwise distinct function param identifiers
                for (Variable var : defFunction.params)
                    if (var.name.equals(paramIdent.id))
                        throw new TypeError(paramIdent.loc, "duplicate function argument identifier '" + paramIdent.id + "'");

                // Register parameter
                defFunction.params.add(Variable.mkVariable(paramIdent.id));
            }

            // Visit statement
            visitor.setFunctionScope(defFunction);
            def.s.accept(visitor);
            final TStmt tstmt = visitor.ret(TStmt.class);

            // Register typed statement
            final TDef tdef = new TDef(defFunction, tstmt);
            tf.l.add(tdef);
        }

        // Create main function and put body
        visitor.setFunctionScope(mainFunction);
        f.s.accept(visitor);
        final TStmt tstmt = visitor.ret(TStmt.class);
        final TDef mainTDef = new TDef(mainFunction, tstmt);
        tf.l.add(mainTDef);

        return tf;
    }
}

class VisitorImpl implements Visitor {
    final TFile tf;
    final Function mfunc;

    @Nullable
    Function functionScope;

    @Nullable
    Object ret;

    public VisitorImpl(TFile tf, Function mfunc) {
        this.tf = tf;
        this.mfunc = mfunc;
    }

    @Override
    public void setFunctionScope(Function functionScope) {
        this.functionScope = functionScope;
    }

    private TExpr expr(Expr e) {
        e.accept(this);
        return ret(TExpr.class);
    }

    private TStmt stmt(Stmt s) {
        s.accept(this);
        return ret(TStmt.class);
    }

    private LinkedList<TExpr> exprs(LinkedList<Expr> exprs) {
        final LinkedList<TExpr> texprs = new LinkedList<>();
        for (Expr expr : exprs)
            texprs.add(expr(expr));
        return texprs;
    }

    private LinkedList<TStmt> stmts(LinkedList<Stmt> stmts) {
        final LinkedList<TStmt> tstmts = new LinkedList<>();
        for (Stmt stmt : stmts)
            tstmts.add(stmt(stmt));
        return tstmts;
    }

    @Override
    public <T> T ret(Class<T> returnType) {
        if (this.ret == null) throw new RuntimeException("return value is null");
        // System.out.prinln("Return type 1")
        if (!returnType.isInstance(this.ret))
            throw new RuntimeException("invalid return type, expected " + returnType.getSimpleName() + " got " + this.ret.getClass().getSimpleName());
        final Object ret = this.ret;
        this.ret = null;
        return (T) ret;
    }

    @Override
    public void visit(Cnone c) {
        ret = new TEcst(c);
    }

    @Override
    public void visit(Cbool c) {
        ret = new TEcst(c);
    }

    @Override
    public void visit(Cstring c) {
        ret = new TEcst(c);
    }

    @Override
    public void visit(Cint c) {
        ret = new TEcst(c);
    }

    @Override
    public void visit(Ecst e) {
        ret = new TEcst(e.c);
    }

    @Override
    public void visit(Ebinop e) {
        ret = new TEbinop(e.op, expr(e.e1), expr(e.e2));
    }

    @Override
    public void visit(Eunop e) {
        ret = new TEunop(e.op, expr(e.e));
    }

    @Override
    public void visit(Eident e) {
        ret = new TEident(matchVariable(e.x, false));
    }

    /**
     * Finds function definition from ident in given scope, user defined.
     * Predefined are not considered in score and are separately treated.
     */
    private Function matchUserDefinedFunction(Ident ident) {

        // Recover name of function. Predefined functions are checked elsewhere

        String name_to_find = Typing.USER_DEFINED_FUNCTION_PREFIX + ident.id;

        // Check function scope (allows recursive call)
        if (functionScope != null && functionScope.name.equals(name_to_find)) return functionScope;

        // Check methods defined so far
        for (TDef tdef : tf.l)
            if (tdef.f.name.equals(name_to_find)) return tdef.f;

        // Throw type error
        throw new TypeError(ident.loc, "could not match function to identifier '" + ident + "'");
    }

    @Override
    public void visit(Ecall e) {
        switch (e.f.id) {

            // Wrong syntax
            case "range":
                throw new TypeError(e.f.loc, "range(n) can only be used inside of list(.)");

            // Implementation of list(range(.))
            case "list":
                if (e.l.size() != 1) throw new TypeError(e.f.loc, "list(.) takes 1 argument but got " + e.l.size());
                final Expr expr = e.l.get(0);
                if (!(expr instanceof Ecall call)) throw new TypeError(e.f.loc, "list(.) takes a range as argument");
                if (!call.f.id.equals("range")) throw new TypeError(call.f.loc, "list(.) takes a range as argument");
                if (call.l.size() != 1)
                    throw new TypeError(call.f.loc, "range(.) takes 1 argument but got " + call.l.size());
                ret = new TErange(expr(call.l.get(0)));
                break;

            // Implementation of len(.)
            case "len":
                if (e.l.size() != 1) throw new TypeError(e.f.loc, "len(.) takes 1 argument but got " + e.l.size());
                ret = new TElen(expr(e.l.get(0)));
                break;

            // Normal function call
            default:
                final Function func = matchUserDefinedFunction(e.f);

                // Check arity
                if (e.l.size() != func.params.size())
                    throw new TypeError(e.f.loc, "wrong function arity, expected " + func.params.size() + " arguments, got " + e.l.size());

                ret = new TEcall(func, exprs(e.l));
        }
    }

    @Override
    public void visit(Eget e) {
        ret = new TEget(expr(e.e1), expr(e.e2));
    }

    @Override
    public void visit(Elist e) {
        ret = new TElist(exprs(e.l));
    }

    @Override
    public void visit(Sif s) {
        ret = new TSif(expr(s.e), stmt(s.s1), stmt(s.s2));
    }

    @Override
    public void visit(Sreturn s) {
        ret = new TSreturn(expr(s.e));
    }

    @NotNull
    private Variable matchVariable(Ident ident, boolean allowCreate) {

        // Function parameters
        for (Variable var : functionScope.params)
            if (var.name.equals(ident.id)) return var;

        // Local variables
        for (Variable var : functionScope.local)
            if (var.name.equals(ident.id)) return var;

        // Global variables (local variables of main function)
        for (Variable var : mfunc.params)
            if (var.name.equals(ident.id)) return var;

        if (!allowCreate) throw new TypeError(ident.loc, "Right variable is not identified, with id " + ident.id);

        // Create new variable, local to visited function
        final Variable newVar = Variable.mkVariable(ident.id);
        functionScope.local.add(newVar);
        return newVar;
    }

    @Override
    public void visit(Sassign s) {
        ret = new TSassign(matchVariable(s.x, true), expr(s.e));
    }

    @Override
    public void visit(Sprint s) {
        ret = new TSprint(expr(s.e));
    }

    @Override
    public void visit(Sblock s) {
        ret = new TSblock(stmts(s.l));
    }

    @Override
    public void visit(Sfor s) {
        final Variable forCounter = Variable.mkVariable(s.x.id);
        functionScope.local.add(forCounter);
        ret = new TSfor(forCounter, expr(s.e), stmt(s.s));
    }

    @Override
    public void visit(Seval s) {
        ret = new TSeval(expr(s.e));
    }

    @Override
    public void visit(Sset s) {
        ret = new TSset(expr(s.e1), expr(s.e2), expr(s.e3));
    }
}