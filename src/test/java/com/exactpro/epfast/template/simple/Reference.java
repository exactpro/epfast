package com.exactpro.epfast.template.simple;

import java.util.Objects;

public class Reference implements com.exactpro.epfast.template.Reference {

    private String name;

    private String namespace = DEFAULT_NAMESPACE;

    public Reference() {
    }

    public Reference(String name) {
        this.name = name;
    }

    public Reference(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reference)) {
            return false;
        }
        Reference that = (Reference) o;
        return Objects.equals(name, that.getName()) &&
            Objects.equals(namespace, that.getNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, namespace);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
