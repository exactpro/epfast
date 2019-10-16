import com.exactpro.epfast.CreatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationProcessingTest {
    @Test
    void testProviders() {
        assertEquals (2, CreatorService.providers().size());
    }
}
