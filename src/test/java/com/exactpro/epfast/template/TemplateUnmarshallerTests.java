package com.exactpro.epfast.template;

import com.exactpro.epfast.template.xml.TemplateXml;
import com.exactpro.epfast.template.xml.TemplatesXml;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateUnmarshallerTests {

    private Unmarshaller templateUnmarshaller;

    @BeforeEach
    public void setupUnmarshaller() throws JAXBException {
        templateUnmarshaller = JAXBContext.newInstance(TemplatesXml.class, TemplateXml.class).createUnmarshaller();
    }

    @Test
    void testUnmarshal() throws Exception {
        Object template = templateUnmarshaller.unmarshal(getResourceInputStream("input.xml"));
        assertThat(template).isInstanceOfAny(TemplatesXml.class, TemplateXml.class);

        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
            .serializeAllExcept("parentNsProvider");
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("myFilter", theFilter);

        ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .activateDefaultTypingAsProperty(
                LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.EVERYTHING, "_class")
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .addMixIn(Object.class, PropertyFilterMixIn.class)
            .setFilterProvider(filters);
        mapper.writeValue(System.out, template);
    }

    private InputStream getResourceInputStream(String resourceName) {
        Class<?> thisClass = this.getClass();
        String path = thisClass.getPackage().getName().replaceAll(Pattern.quote("."), "/");
        return thisClass.getClassLoader().getResourceAsStream(path + "/" + resourceName);
    }

    @JsonFilter("myFilter")
    public static class PropertyFilterMixIn {
    }
}
