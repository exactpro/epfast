package com.exactpro.epfast.template;

import java.util.List;

public interface Template {

    Identity getTemplateId();

    IdentityRef getTypeRef();

    List<Instruction> getInstructions();

}
