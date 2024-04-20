import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.curiositycore.voidvaults.VoidVaults;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//TODO need to get it hooked up to test SQL
public class InitTests {
    private ServerMock server;
    private VoidVaults plugin;

    @Before
    public void setUp() {
        server = MockBukkit.getOrCreateMock();
        plugin = MockBukkit.load(VoidVaults.class);
    }

    @Test
    public void testPlugin() {
        assertNotNull(plugin);
    }
}
