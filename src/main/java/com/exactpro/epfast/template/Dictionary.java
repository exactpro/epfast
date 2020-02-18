package com.exactpro.epfast.template;

public class Dictionary {

    private String name;

    // TODO create proper constants
    public static final Dictionary PREDEFINED_1 = new Dictionary("predefined-1");

    public static Dictionary getDictionary(String name) {
        if (PREDEFINED_1.getName().equals(name)) {
            return PREDEFINED_1;
        }
        return new Dictionary(name);
    }

    private Dictionary(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    // TODO implement equals and hashCode
}
