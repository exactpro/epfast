package com.exactpro.epfast.annotation.processing;

class CreatorTemplateParameters {
    private TypeName typeName;

    private FastPackageElement fastPackage;

    CreatorTemplateParameters(
        TypeName typeName, FastPackageElement fastPackage) {
        this.typeName = typeName;
        this.fastPackage = fastPackage;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public FastPackageElement getFastPackage() {
        return fastPackage;
    }
}
