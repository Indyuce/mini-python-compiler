package mini_python.exception;

import mini_python.Location;

public class TypeError extends Error {
    final Location loc;

    public TypeError(Location loc, String message) {
        super(loc + "\nerror: " + message);

        this.loc = loc;
    }

    public Location getLocation() {
        return loc;
    }
}
