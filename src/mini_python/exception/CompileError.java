package mini_python.exception;

import mini_python.Location;
import mini_python.Main;

import java.util.ArrayList;
import java.util.List;

public class CompileError extends Error {
    final Location loc;

    @Deprecated
    public CompileError(String message) {
        this(Location.NONE, message);
    }

    @Deprecated
    public CompileError(Exception exception) {
        this(Location.NONE, exception);
    }

    public CompileError(Location loc, String message) {
        super(message);

        this.loc = loc;
    }

    public CompileError(Location loc, Exception exception) {
        super(exception);

        this.loc = loc;
    }


    public Location getLocation() {
        return loc;
    }

    private static final int DISPLAY_PREVIOUS_LINES = 5;
    private static final boolean DISPLAY_EMPTY_LINES = true;
    public static final String ANSI_RED = "\u001B[31m";

    public void printError() {
        final List<String> out = new ArrayList<>();
        final int i = loc.line;

        if (loc != Location.NONE) {
            final String tgtline = Main.lines.get(i); // Target line

            out.add("Compilation error at line " + i + ", column " + loc.column + " of file " + Main.file + ":");
            out.add("");

            // Display lines
            out.addAll(fetchPreviousLines());

            // Display column
            final StringBuilder colDisp = new StringBuilder();
            int j = 0;
            while (j++ < loc.column) colDisp.append('.');
            colDisp.append('^');
            while (j++ < tgtline.length()) colDisp.append('.');
            out.add(colDisp.toString());
            out.add("");
        }

        out.add(getClass().getSimpleName() + ": " + getMessage());

        // Print
        for (String lineout : out) System.out.println(ANSI_RED + lineout);
    }

    /**
     * This method allows to skip empty lines so that the dev understands
     * more easily where the error is
     */
    private List<String> fetchPreviousLines() {
        final List<String> lines = new ArrayList<>();
        int disp = 0;
        int i = this.loc.line;
        while (i >= 0 && disp < DISPLAY_PREVIOUS_LINES) {
            final String nextLine = Main.lines.get(i--);
            if (!DISPLAY_EMPTY_LINES && isEmpty(nextLine)) continue;
            lines.add(0, nextLine);
            disp++;
        }
        if (i >= 0) lines.add(0, "...");
        return lines;
    }

    private boolean isEmpty(String line) {
        for (char c : line.toCharArray())
            if (c != ' ' && c != '\t' && c != '\n') return false;
        return true;
    }
}
