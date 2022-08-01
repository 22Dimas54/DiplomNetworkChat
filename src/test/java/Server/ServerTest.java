package Server;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    @BeforeEach
    public void init() {
        System.out.println("test started");
    }

    @BeforeAll
    public static void started() {
        System.out.println("tests started");
    }

    @AfterEach
    public void finished() {
        System.out.println("test compiled");
    }

    @AfterAll
    public static void finishedAll() {
        System.out.println("tests finished");
    }

    @Test
    public void testGetPort() {
        //arrange
        String expected = "56432";
        //act
        String result = Server.getResource("port");
        //assert
        assertEquals(expected, result);
    }
}
