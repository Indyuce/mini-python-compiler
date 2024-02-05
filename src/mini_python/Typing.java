package mini_python;

import java.util.LinkedList;

/**
 * Class that takes as input parsed trees and output typed trees.
 *
 * @see #file(File)
 */
class Typing {
    static boolean debug = false;

    /**
     * Signals a typing error
     */
    static void error(Location loc, String msg) {
        throw new Error(loc + "\nerror: " + msg);
    }

    static TFile file(File f) {

        // Typed file
        final TFile tf = new TFile();
        final Visitor visitor = new VisitorImpl(tf);

        // All functions
        for (Def def : f.l) {
            // Define new typed function
            final Function defFunction = new Function(def.f.id, new LinkedList<>());

            // Iterate over parameters
            for (Ident paramIdent : def.l)
                defFunction.params.add(Variable.mkVariable(paramIdent.id));

            // Visit statement
            def.s.accept(visitor);
            final TStmt tstmt = visitor.ret(TStmt.class);

            // Register typed statement
            final TDef tdef = new TDef(defFunction, tstmt);
            tf.l.add(tdef);
        }

        // Create main function and put body
        final Function mainFunction = new Function("__main__", new LinkedList<>());
        f.s.accept(visitor);
        final TStmt tstmt = visitor.ret(TStmt.class);
        final TDef mainTDef = new TDef(mainFunction, tstmt);
        tf.l.add(mainTDef);

        return tf;
    }
}
