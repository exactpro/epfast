package com.exactpro.epfast.template;

import java.util.Objects;

public class Dictionary {

    private String name;

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
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dictionary that = (Dictionary) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
