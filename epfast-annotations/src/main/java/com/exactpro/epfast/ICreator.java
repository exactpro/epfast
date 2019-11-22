package com.exactpro.epfast;

public interface ICreator {
    Object create(String fastTypeName) throws Exception;

    String getFastPackage();
}
