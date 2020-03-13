package com.exactpro.epfast.template.xml.helper;

import com.exactpro.epfast.template.Identity;

public class AfterUnmarshal {

    public static class IdentityBase implements Identity {

        private String name;

        private String namespace;

        private String auxiliaryId;

        public void setName(String name) {
            this.name = name;
        }

        public void setNs(String namespace) {
            this.namespace = namespace;
        }

        public void setAuxiliaryId(String auxiliaryId) {
            this.auxiliaryId = auxiliaryId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getNs() {
            return namespace;
        }

        @Override
        public String getAuxiliaryId() {
            return auxiliaryId;
        }
    }

    public static class ApplicationIdentity extends IdentityBase {

        public String ns;

        @Override
        public String getNs() {
            if (super.getNs() != null) {
                return super.getNs();
            }
            if (ns != null) {
                return ns;
            }
            return "";
        }
    }

    public static class TemplateIdentity extends IdentityBase {

        public String templateNs;

        @Override
        public String getNs() {
            if (super.getNs() != null) {
                return super.getNs();
            }
            if (templateNs != null) {
                return templateNs;
            }
            return "";
        }
    }
}
