package managers;

import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
public void  testDefaultMangerNotNull () {
        assertNotNull(Managers.getDefault(), "default manager should not be not null");
        assertNotNull(Managers.getDefaultHistory(), "default history manager should be not null");
    }
}