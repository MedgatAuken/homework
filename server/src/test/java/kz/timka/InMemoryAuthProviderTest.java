package kz.timka;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InMemoryAuthProviderTest {
    private InMemoryAuthProvider authProvider;

    @Before
    public void setUp() {
        authProvider = new InMemoryAuthProvider();
    }

    @Test
    public void testGetUsernameByLoginAndPassword() {
        String username = authProvider.getUsernameByLoginAndPassword("Alex@gmail.com", "111");
        assertEquals("Alex", username);

        username = authProvider.getUsernameByLoginAndPassword("Ben@gmail.com", "111");
        assertEquals("Ben", username);

        username = authProvider.getUsernameByLoginAndPassword("John@gmail.com", "111");
        assertEquals("John", username);

        username = authProvider.getUsernameByLoginAndPassword("NonExistent@gmail.com", "111");
        assertNull(username);
    }
}