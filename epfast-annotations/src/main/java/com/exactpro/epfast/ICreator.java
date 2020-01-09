package com.exactpro.epfast;

public interface ICreator {
    IFieldSetter createSetter(String fastTypeName);

    String getFastPackage();
}
