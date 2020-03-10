package com.exactpro.epfast.template;

import java.util.List;

public interface Template {

    Identity getTemplateId();

    Reference getTypeRef();

    List<? extends Instruction> getInstructions();

}
