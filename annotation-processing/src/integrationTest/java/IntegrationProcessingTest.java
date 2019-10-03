import com.exactpro.epfast.CreatorService;
import com.exactpro.epfast.annotation.internal.CreatorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationProcessingTest {
    @Test
    void testServiceProviders() {
        assertEquals (1, CreatorService.providers().size());
        assertEquals (CreatorImpl.class, CreatorService.providers().stream().findFirst().get().getClass());
    }
}
