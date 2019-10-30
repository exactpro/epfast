package com.exactpro.epfast;

public interface ICreator {
    <T> IFieldSetter<T> create(String fastTypeName) throws Exception;

    String getFastPackage();
}
