package com.exactpro.epfast;

public interface IFieldSetter<T> {

    void setField(String name, Object value) throws Exception;

    T getObject();
}
