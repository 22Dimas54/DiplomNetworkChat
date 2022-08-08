package Client;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
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

    @ParameterizedTest
    @MethodSource("source")
    public void testGetResource(String key, String expected) {
        //act
        String result = MessageHandler.getResource(key);
        //assert
        assertEquals(expected, result);
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("host", "127.0.0.1"),
                Arguments.of("port", "56432"));
    }
}

