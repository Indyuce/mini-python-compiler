package mini_python;

public class Location {
    final int line;
    final int column;

    // TODO shouldn't be used, check usage
    @Deprecated
    public static final Location START = new Location(0, 0);

    Location(int line, int column) {
        this.line = line + 1;
        this.column = column;
    }

    @Override
    public String toString() {
        return this.line + ":" + this.column + ":";
    }
}
