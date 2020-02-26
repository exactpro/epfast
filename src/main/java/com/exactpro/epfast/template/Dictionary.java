package com.exactpro.epfast.template;

public class Dictionary {

    private final String name;

    public static final Dictionary TEMPLATE = new Dictionary("template");

    public static final Dictionary TYPE = new Dictionary("type");

    public static final Dictionary GLOBAL = new Dictionary("global");

    public static Dictionary getDictionary(String name) {
        if (TEMPLATE.getName().equals(name)) {
            return TEMPLATE;
        } else if (TYPE.getName().equals(name)) {
            return TYPE;
        } else if (GLOBAL.getName().equals(name)) {
            return GLOBAL;
        }
        return new Dictionary(name);
    }

    private Dictionary(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Dictionary) && getName().equals(((Dictionary) o).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
