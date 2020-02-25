package com.exactpro.epfast.template;

public interface FieldInstruction extends Instruction {

    Identity getFieldId();

    boolean isOptional();

}
