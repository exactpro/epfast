package com.exactpro.epfast.annotation.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class FastPackageElement {

    private ArrayList<FastTypeElement> fastTypes = new ArrayList<>();

    public void addFastType(FastTypeElement fastType) {
        fastTypes.add(fastType);
    }

    public Collection<FastTypeElement> getFastTypes() {
        return Collections.unmodifiableList(fastTypes);
    }

    public abstract boolean hasValidPackageName();

    public abstract String getPackageName();

}
