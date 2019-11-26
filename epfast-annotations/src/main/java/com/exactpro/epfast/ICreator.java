package com.exactpro.epfast;

public interface ICreator {
    IFieldSetter createSetter(String fastTypeName) throws Exception;

    String getFastPackage();
}
