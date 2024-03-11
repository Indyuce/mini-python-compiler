package mini_python;

public class Location {
    public final int line;
    public final int column;

    public static final Location NONE = new Location(0, 0);

    Location(int line, int column) {
        this.line = line + 1;
        this.column = column;
    }

    @Override
    public String toString() {
        return this.line + ":" + this.column + ":";
    }
}
