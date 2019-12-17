package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

class Instruction extends MainFields {

    private TemplateRef templateRef;

    private List<UInt64> uInt64s;

    private List<UInt32> uInt32s;

    private List<Int64> int64s;

    private List<Int32> int32s;

    private List<Decimal> decimals;

    private List<StringClass> strings;

    private List<ByteVector> byteVectors;

    private List<Sequence> sequences;

    private List<Group> groups;

    public TemplateRef getTemplateRef() {
        return templateRef;
    }

    @XmlElement(name = "templateRef", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTemplateRef(TemplateRef templateRef) {
        this.templateRef = templateRef;
    }

    public List<UInt64> getuInt64s() {
        return uInt64s;
    }

    @XmlElement(name = "uInt64", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setuInt64s(List<UInt64> uInt64s) {
        this.uInt64s = uInt64s;
    }

    public List<UInt32> getuInt32s() {
        return uInt32s;
    }

    @XmlElement(name = "uInt32", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setuInt32s(List<UInt32> uInt32s) {
        this.uInt32s = uInt32s;
    }

    public List<Int64> getInt64s() {
        return int64s;
    }

    @XmlElement(name = "int64", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setInt64s(List<Int64> int64s) {
        this.int64s = int64s;
    }

    public List<Int32> getInt32s() {
        return int32s;
    }

    @XmlElement(name = "int32", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setInt32s(List<Int32> int32s) {
        this.int32s = int32s;
    }

    public List<Decimal> getDecimals() {
        return decimals;
    }

    @XmlElement(name = "decimal", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setDecimals(List<Decimal> decimals) {
        this.decimals = decimals;
    }

    public List<StringClass> getStrings() {
        return strings;
    }

    @XmlElement(name = "string", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setStrings(List<StringClass> strings) {
        this.strings = strings;
    }

    public List<ByteVector> getByteVectors() {
        return byteVectors;
    }

    @XmlElement(name = "byteVector", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setByteVectors(List<ByteVector> byteVectors) {
        this.byteVectors = byteVectors;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    @XmlElement(name = "sequence", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setSequences(List<Sequence> sequences) {
        this.sequences = sequences;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @XmlElement(name = "group", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
