import com.exactpro.epfast.annotation.CreatorService;
import com.exactpro.epfast.annotation.internal.CreatorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationProcessingTest {
    @Test
    void testProviders() {
        assertEquals (1, CreatorService.providers().size());
        assertEquals (CreatorImpl.class, CreatorService.providers().get(0).getClass());
    }
}
