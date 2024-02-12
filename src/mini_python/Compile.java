package mini_python;

class Compile {

    static boolean debug = false;

    static X86_64 file(TFile f) {
        final X86_64 file = new X86_64();




        return file; // TODO
    }

    class VisitorImpl implements TVisitor {

        @Override
        public void visit(Cnone c) {

        }

        @Override
        public void visit(Cbool c) {

        }

        @Override
        public void visit(Cstring c) {

        }

        @Override
        public void visit(Cint c) {

        }

        @Override
        public void visit(TEcst e) {

        }

        @Override
        public void visit(TEbinop e) {

        }

        @Override
        public void visit(TEunop e) {

        }

        @Override
        public void visit(TEident e) {

        }

        @Override
        public void visit(TEcall e) {

        }

        @Override
        public void visit(TEget e) {

        }

        @Override
        public void visit(TElist e) {

        }

        @Override
        public void visit(TErange e) {

        }

        @Override
        public void visit(TElen e) {

        }

        @Override
        public void visit(TSif s) {

        }

        @Override
        public void visit(TSreturn s) {

        }

        @Override
        public void visit(TSassign s) {

        }

        @Override
        public void visit(TSprint s) {

        }

        @Override
        public void visit(TSblock s) {

        }

        @Override
        public void visit(TSfor s) {

        }

        @Override
        public void visit(TSeval s) {

        }

        @Override
        public void visit(TSset s) {

        }
    }
}
