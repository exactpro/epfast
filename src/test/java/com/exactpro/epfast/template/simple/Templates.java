package com.exactpro.epfast.template.simple;

import java.util.ArrayList;
import java.util.List;

public class Templates implements com.exactpro.epfast.template.Templates {

    private final List<Template> templates = new ArrayList<>();

    @Override
    public List<Template> getTemplates() {
        return templates;
    }
}
