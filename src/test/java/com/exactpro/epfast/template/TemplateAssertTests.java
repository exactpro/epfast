package com.exactpro.epfast.template;

import com.exactpro.epfast.template.simple.Identity;
import com.exactpro.epfast.template.simple.Reference;
import com.exactpro.epfast.template.simple.Template;
import org.junit.jupiter.api.Test;

class TemplateAssertTests {

    private Template temp1 = new Template();

    private Template temp2 = new Template();

    private Reference ref1 = new Reference();

    private Reference ref2 = new Reference();

    private Identity id1 = new Identity();

    private Identity id2 = new Identity();

    public TemplateAssertTests() {
        ref1.setNamespace("namespace");
        ref2.setNamespace("namespace");
        id1.setName("name");
        id2.setName("name");
        id1.setAuxiliaryId("ID");
        id2.setAuxiliaryId("ID");
        temp1.setTypeRef(ref1);
        temp2.setTypeRef(ref2);
        temp1.setTemplateId(id1);
        temp2.setTemplateId(id2);
    }

    @Test
    void testTemplateEquality() {
        TemplateAssert.assertThat(temp1).isSameTemplateAs(temp2);
    }
}
