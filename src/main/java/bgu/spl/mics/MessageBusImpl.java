package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceHashMap; //For register. TODO check the type of the queue
    private ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> eventLinkedListHashMap; //For subscribing Event.
    private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<MicroService>> broadcastLinkedListHashMap; //For subscribing Broadcast.
    private ConcurrentHashMap<Event<?>, Future<?>> eventToFutureHashMap; //That's how we are going to save the future instance for the method Complete.
    private static MessageBusImpl messageBus=null;

    public static synchronized MessageBusImpl getInstance() {
        if (messageBus == null)
            messageBus = new MessageBusImpl();
        return messageBus;
    }

    private MessageBusImpl() {
        microServiceHashMap = new ConcurrentHashMap<>();
        eventLinkedListHashMap = new ConcurrentHashMap<>();
        broadcastLinkedListHashMap = new ConcurrentHashMap<>();
        eventToFutureHashMap = new ConcurrentHashMap<>();
    }

    /**
     * @param type The type to subscribe to,
     * @param m    The subscribing micro-service.
     * @param <T>
     * @pre: none
     * @post: this.eventLinkedListHashMap[type].size() == @pre(his.eventLinkedListHashMap[type].size())+1
     */
    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        ConcurrentLinkedQueue<MicroService> queue = eventLinkedListHashMap.get(type);
        if (queue == null)
            queue = new ConcurrentLinkedQueue<>();
        queue.add(m);
        eventLinkedListHashMap.put(type, queue);
    }

    /**
     * @param type The type to subscribe to.
     * @param m    The subscribing micro-service.
     * @pre: none
     * @post: this.broadcastLinkedListHashMap[type].size() == @pre(his.broadcastLinkedListHashMap[type].size())+1
     */
    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        ConcurrentLinkedQueue<MicroService> queue = broadcastLinkedListHashMap.get(type);
        if (queue == null)
            queue = new ConcurrentLinkedQueue<>();
        queue.add(m);
        broadcastLinkedListHashMap.put(type, queue);
    }

    /**
     * @param e      The completed event.
     * @param result The resolved result of the completed event.
     * @param <T>
     * @pre: none
     * @post: e.getFuture().getResult() = result
     */
    @Override
    public <T> void complete(Event<T> e, T result) {
        Future<T> temp = (Future<T>) eventToFutureHashMap.get(e);
        temp.resolve(result);
    }

    /**
     * @param b The message to added to the queues.
     * @pre: none
     * @post: (microService : this.broadcastLinkedListHashMap[b]) @pre(microService.size())+ 1 == microService.size()
     */
    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<MicroService> micros = broadcastLinkedListHashMap.get(b.getClass());
        if (micros != null) {
            //TODO CHECK IF need to be synchronized because of TIKILILE bRoAdCaSt
            for (MicroService m : micros) {
                ConcurrentLinkedQueue<Message> queue = microServiceHashMap.get(m);
                queue.add(b);
                microServiceHashMap.put(m, queue);
            }
            notifyAll();
        }
    }

    /**
     * @param e   The event to add to the queue.
     * @param <T>
     * @return
     * @pre: this.eventLinkedListHashMap.get(e) != null || this.eventLinkedListHashMap.get(e).isEmpty() == false
     * @post: this.eventLinkedListHashMap[e].size() == @pre(this.eventLinkedListHashMap[e].size()) + 1
     */
    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        // Receives all the MicroServices that can process the Event e
        ConcurrentLinkedQueue<MicroService> micros = eventLinkedListHashMap.get(e.getClass());
        synchronized (this) { //TODO check if necessary
            // If none of the MS can process
            if (micros != null) {
                // synchronized for the right Round Robin Pattern.
                MicroService m = micros.poll(); // when created micro-queue (subscribeEvent), we always add at least one micro.
                ConcurrentLinkedQueue<Message> queue = microServiceHashMap.get(m);
                queue.add(e);
                microServiceHashMap.put(m, queue);
                micros.offer(m);
                Future<T> output = new Future<>();
                eventToFutureHashMap.put(e, output);
                notifyAll(); //TODO CHECK FOR AWAIT MSG
                return output;
            }
        }
        return null;
    }

    /**
     * @param m the micro-service to create a queue for.
     * @pre: this.microServiceHashMap.containsKey(m) == false
     * @post: this.microServiceHashMap[m].size() == 0
     */
    @Override
    public void register(MicroService m) {
        microServiceHashMap.putIfAbsent(m, new ConcurrentLinkedQueue<>());
    }

    /**
     * @param m the micro-service to unregister.
     * @pre: this.microServiceHashMap.containsKey(m) == true
     * @post: this.microServiceHashMap.containsKey(m) == false
     */
    @Override
    public void unregister(MicroService m) { //TODO remember future list IGNORED
        ConcurrentLinkedQueue<Message> messages = microServiceHashMap.get(m);
        for (Message msg : messages) {
            if (msg instanceof Event<?>) {
                ConcurrentLinkedQueue<MicroService> micros = eventLinkedListHashMap.get(msg.getClass());
                micros.remove(m);
            } else if (msg instanceof Broadcast) {
                ConcurrentLinkedQueue<MicroService> micros = broadcastLinkedListHashMap.get(msg.getClass());
                micros.remove(m);
            }
        }
    }

    /**
     * @param m The micro-service requesting to take a message from its message
     *          queue.
     * @return Message
     * @throws InterruptedException
     * @pre: this.microServiceHashMap.containsKey(m) == true
     * @post: this.microServiceHashMap[m].size() == @pre(this.microServiceHashMap[m].size())-1
     * k
     */
    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        ConcurrentLinkedQueue<Message> queue = microServiceHashMap.get(m);
        while (queue.isEmpty())
            wait();
        return queue.poll();
    }


}
