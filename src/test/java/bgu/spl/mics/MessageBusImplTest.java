package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleMessageSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    MessageBus messageBus;

    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
    }

    @Test
    void subscribeEvent() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        Event<String> event = new ExampleEvent("Event");
        messageBus.register(microService);
        try {
            messageBus.awaitMessage(microService);
        } catch (InterruptedException e) {
            messageBus.subscribeEvent(ExampleEvent.class, microService);

            messageBus.sendEvent(event);
            try {
                messageBus.awaitMessage(microService);
            } catch (InterruptedException g) {
            }
            assertTrue(true);
        }
    }

    @Test
    void subscribeBroadcast() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        Broadcast event = new ExampleBroadcast("2");
        messageBus.register(microService);
        try {
            messageBus.awaitMessage(microService);
        } catch (InterruptedException e) {
            messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);

            messageBus.sendBroadcast(event);
            try {
                messageBus.awaitMessage(microService);
            } catch (InterruptedException g) {
            }
            assertTrue(true);
        }
    }

    @Test
    void complete() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        Event<String> event = new ExampleEvent("Event");
        messageBus.register(microService);
        messageBus.subscribeEvent(ExampleEvent.class, microService);

        Future<String> f;
        f = messageBus.sendEvent(event);
        messageBus.complete(event, "finished");
        assertEquals("finished", f.get());
    }

    @Test
    void sendBroadcast() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        Broadcast broadcast = new ExampleBroadcast("2");
        messageBus.register(microService);
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        messageBus.sendBroadcast(broadcast);
        try {
            Message newBD = messageBus.awaitMessage(microService);
            assertEquals(newBD, broadcast);
        } catch (InterruptedException e) {
            System.out.println("Failed");
        }
    }

    @Test
    void sendEvent() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        Event<String> event = new ExampleEvent("2");
        messageBus.register(microService);
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        messageBus.sendEvent(event);
        try {
            Message newBD = messageBus.awaitMessage(microService);
            assertEquals(newBD, event);
        } catch (InterruptedException e) {
            System.out.println("Failed");
        }
    }

    @Test
    void register() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        messageBus.register(microService);
        try {
            messageBus.awaitMessage(microService);
        } catch (InterruptedException e) {
            System.out.println("Failed");
        }
    }

    @Test
    void unregister() {
        String s[] = {"send"};
        MicroService microService = new ExampleMessageSenderService("Message sender", s);
        messageBus.register(microService);
        messageBus.unregister(microService);
        try {
            messageBus.awaitMessage(microService);
            System.out.println("Failed");
        } catch (InterruptedException e) {
        }
    }

    @Test
    void awaitMessage() {
        fail();
    }
}