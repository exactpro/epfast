package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

class MainFields {

    private String name;

    private Integer id;

    private Presence presence;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Presence getPresence() {
        return presence;
    }

    @XmlAttribute(name = "presence")
    public void setPresence(Presence presence) {
        this.presence = presence;
    }
}
