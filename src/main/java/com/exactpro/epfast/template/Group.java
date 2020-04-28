package com.exactpro.epfast.template;

import java.util.List;

public interface Group extends FieldInstruction {

    Reference getTypeRef();

    Dictionary getDictionary();

    List<? extends Instruction> getInstructions();

}
