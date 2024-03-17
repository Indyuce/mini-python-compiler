package mini_python.exception;

import mini_python.Location;

import java.util.ArrayList;
import java.util.List;

public class CompileException extends Error {
    final Location loc;
    final List<String> lines;
    final String file;

    public CompileException(Location loc, String message, List<String> lines, String file) {
        super(message);

        this.loc = loc;
        this.lines = lines;
        this.file = file;
    }

    public Location getLocation() {
        return loc;
    }

    private static final int DISPLAY_PREVIOUS_LINES = 5;
    private static final boolean DISPLAY_EMPTY_LINES = false;
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE = "\u001B[38m";

    public void printError() {
        final List<String> out = new ArrayList<>();

        if (loc != Location.NONE) {
            out.add(ANSI_RED + "Compilation error at line " + loc.line + ", column " + loc.column + " of file " + this.file + ":");
            out.add("");
            displayLines(out);
            out.add("");
        }

        out.add(ANSI_RED + getClass().getSimpleName() + ANSI_WHITE + ": " + getMessage());

        // Print
        for (String lineout : out) System.out.println(lineout);
    }

    private String getAlignment(int maxDigits) {
        return getAlignment(1, maxDigits + 1);
    }

    private String getAlignment(int lineNumber, int maxDigits) {
        final int digits = digits(lineNumber);
        if (digits == maxDigits) return "";
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < maxDigits - digits; i++)
            builder.append(' ');
        return builder.toString();
    }

    private int digits(int n) {
        return (int) Math.floor(Math.log10(n));
    }

    /**
     * This method allows to skip empty lines so that the dev understands
     * more easily where the error is.
     *
     * @return Number of digits of the maximum
     */
    private int displayLines(List<String> out) {

        // Find lines and numbers
        int maximumLine = 0;
        final List<Integer> numbers = new ArrayList<>();
        final List<String> lines = new ArrayList<>();
        int disp = 0;
        int i = this.loc.line - 1;
        while (i >= 0 && disp < DISPLAY_PREVIOUS_LINES) {
            final String nextLine = this.lines.get(i--);
            if (!DISPLAY_EMPTY_LINES && isEmpty(nextLine)) continue;

            final int lineNumber = i + 2;
            lines.add(0, nextLine);
            numbers.add(0, lineNumber);
            maximumLine = Math.max(lineNumber, maximumLine);
            disp++;
        }

        // Calculate
        final int maxDigits = digits(maximumLine);

        // Add ... if code remaining
        if (i >= 0) out.add(getAlignment(maxDigits) + ANSI_WHITE + "     ...");

        // Display lines
        int index = 0;
        for (String line : lines) {
            final int lineNumber = numbers.get(index++);
            final String alignment = getAlignment(lineNumber, maxDigits);
            out.add("    " + ANSI_GREEN + alignment + lineNumber + " " + ANSI_WHITE + line);
        }

        // Target line
        final String tgtline = this.lines.get(loc.line - 1); // Target line
        final StringBuilder colDisp = new StringBuilder("       ");
        int j = 0;
        while (j++ < loc.column) colDisp.append(' ');
        colDisp.append(ANSI_RED + "^");
        while (j++ < tgtline.length() * 2) colDisp.append(' ');
        out.add(colDisp.toString());

        return maximumLine;
    }

    private boolean isEmpty(String line) {
        for (char c : line.toCharArray())
            if (c != ' ' && c != '\t' && c != '\n') return false;
        return true;
    }
}
