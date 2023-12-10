package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


class FutureTest {
    private static String result;
    private static Future<String> future;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        result = "";
        future = new Future<String>();
    }

    @org.junit.jupiter.api.Test
    void get() {
        assertNull(future.getResult());
        future.resolve(result);
        assertEquals(result, future.get());
    }

    @org.junit.jupiter.api.Test
    void resolve() {
        assertNull(future.getResult());
        future.resolve("The result has been resolved");
        assertEquals("The result has been resolved", future.getResult());
    }

    @org.junit.jupiter.api.Test
    void isDone() {
        assertFalse(future.isDone());
        future.resolve("Message");
        assertTrue(future.isDone());
    }

    @org.junit.jupiter.api.Test
    void testGet() {
       assertEquals("", future.getResult());
       Thread t1 = new Thread(() -> {
           try {
               assertEquals("Completed",future.get(2, TimeUnit.valueOf("MILLISECONDS")));
               assertTrue(future.isDone());
           } catch (Exception e) {
               throw new UnknownError("Exception");
           }
        });
        Thread t2 = new Thread(() -> {
            future.resolve("Completed");
        });
        t1.start();
        t2.start();

    }
}