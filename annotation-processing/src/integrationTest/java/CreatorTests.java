import com.exactpro.epfast.annotation.DefaultAnnotated;
import com.exactpro.epfast.annotation.NamedAnnotated;
import com.exactpro.epfast.annotation.internal.CreatorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreatorTests {
    private CreatorImpl creator = new CreatorImpl();

    @Test
    void testCreatorDefaultAnnotation() {
        assertEquals(DefaultAnnotated.class, creator.create("DefaultAnnotated").getClass());
    }

    @Test
    void testCreatorNonDefaultAnnotation() {
        assertEquals(NamedAnnotated.class, creator.create("named").getClass());
    }

    @Test
    void testExceptionThrown() {
        Exception exception = assertThrows(RuntimeException.class, ()-> new CreatorImpl().create("null"));
        assertEquals("FastType name=\"null\" not found", exception.getMessage());
    }
}
