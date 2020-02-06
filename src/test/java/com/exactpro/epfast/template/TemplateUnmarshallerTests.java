package com.exactpro.epfast.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
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
        templateUnmarshaller = JAXBContext.newInstance(Templates.class).createUnmarshaller();
    }

    @Test
    void testUnmarshal() throws Exception {
        Object template = templateUnmarshaller.unmarshal(getResourceInputStream("input.xml"));
        assertThat(template).isInstanceOf(Templates.class);

        ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .activateDefaultTypingAsProperty(
                LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.EVERYTHING, "_class")
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.writeValue(System.out, template);
    }

    private InputStream getResourceInputStream(String resourceName) {
        Class<?> thisClass = this.getClass();
        String path = thisClass.getPackage().getName().replaceAll(Pattern.quote("."), "/");
        return thisClass.getClassLoader().getResourceAsStream(path + "/" + resourceName);
    }

}
