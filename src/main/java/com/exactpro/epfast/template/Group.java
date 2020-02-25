package com.exactpro.epfast.template;

import java.util.List;

public interface Group extends FieldInstruction {

    boolean isOptional();

    Reference getTypeRef();

    List<Instruction> getInstructions();

}
