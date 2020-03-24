package com.exactpro.epfast.template;

import com.exactpro.epfast.template.assertion.TemplateAssert;
import com.exactpro.epfast.template.simple.Identity;
import com.exactpro.epfast.template.simple.Reference;
import com.exactpro.epfast.template.simple.Template;
import org.junit.jupiter.api.Test;

class TemplateAssertTests {

    private Template temp = new Template();

    private Template otherTemp = new Template();

    public TemplateAssertTests() {
        Reference ref = new Reference();
        Reference otherRef = new Reference();
        Identity id = new Identity();
        Identity otherId = new Identity();

        ref.setNamespace("namespace");
        otherRef.setNamespace("otherNamespace");
        id.setName("name");
        otherId.setName("otherName");
        id.setAuxiliaryId("ID");
        otherId.setAuxiliaryId("otherID");

        temp.setTypeRef(ref);
        otherTemp.setTypeRef(otherRef);
        temp.setTemplateId(id);
        otherTemp.setTemplateId(otherId);
    }

    @Test
    void testTemplateEquality() {
        TemplateAssert.assertThat(temp).isSameTemplateAs(temp);
        try {
            TemplateAssert.assertThat(temp).isSameTemplateAs(otherTemp);
        } catch (AssertionError error) {
            System.out.print(error.getMessage() + "\n");
        }
    }
}
