package mini_python;

public enum Type {
    NONE,
    BOOL,
    INT,
    STRING,
    LIST;

    public final int id;
    public final String name;

    Type() {
        this.id = ordinal();
        this.name = name().toLowerCase();
    }

    public static Type fromId(int id) {
        for (Type t : values())
            if (t.id == id) return t;
        throw new IllegalArgumentException("could not find type with id '" + id + "'");
    }

    public static Type fromName(String name) {
        for (Type t : values())
            if (t.name.equals(name)) return t;
        throw new IllegalArgumentException("could not find type with name '" + name + "'");
    }
}
