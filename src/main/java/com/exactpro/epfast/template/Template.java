package com.exactpro.epfast.template;

import java.util.List;

public interface Template {

    Identity getTemplateId();

    Dictionary getDictionary(); // XXX I'm not sure we need it here

    IdentityRef getTypeRef();

    List<Instruction> getInstructions();
}
