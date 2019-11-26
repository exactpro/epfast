package com.exactpro.epfast;

public interface IFieldSetter {

    void setField(String name, Object value) throws Exception;

    Object getObject();
}
