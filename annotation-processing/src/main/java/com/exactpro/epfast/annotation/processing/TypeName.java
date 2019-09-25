package com.exactpro.epfast.annotation.processing;

class TypeName {
    private String packageName;

    private String className;

    TypeName(String fullTypeName) {
        int lastDotIndex = fullTypeName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            this.packageName = "";
            this.className = fullTypeName;
        }  else {
            this.packageName = fullTypeName.substring(0, lastDotIndex);
            this.className = fullTypeName.substring(lastDotIndex + 1);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return packageName.isEmpty() ? className : packageName + "." + className;
    }
}
