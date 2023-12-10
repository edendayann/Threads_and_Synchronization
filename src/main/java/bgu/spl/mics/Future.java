package bgu.spl.mics;

import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
    private T result;
    private boolean done;

    /**
     * This should be the the only public constructor in this class.
     */
    public Future() { //TODO gOOD jOb
        result = null;
        done = false;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type T if it is available, if not wait until it is available.
     * @pre: None
     * @post: None
     */
    public T get() {
        try {
            while (result == null)
                wait();
        } catch (InterruptedException e) {
            throw new UnknownError("Result hasn't been resolved"); //TODO check what to do
        }
        return result;
    }

    /**
     * Resolves the result of this Future object.
     *
     * @pre: result == None
     * @post: this.result = result
     */
    public void resolve(T result) {
        this.result = result;
        this.done = true;
        notifyAll();
    }

    /**
     * @return true if this object has been resolved, false otherwise
     * @pre: None
     * @post: None
     */
    public boolean isDone() {
        return done;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     *
     * @param timout the maximal amount of time units to wait for the result.
     * @param unit   the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * wait for {@code timeout} TimeUnits {@code unit}. If time has
     * elapsed, return null.
     * @pre: None
     * @post: None
     */
    public T get(long timeout, TimeUnit unit) {
        if (result != null)
            return result;
        long expirationTime = new Date().getTime() + unit.toMillis(timeout);
        while (result==null){
            long timeLeft = expirationTime - new Date().getTime();
            try {
                unit.timedWait(this, timeLeft); //TODO check if works
                if (timeLeft <= 0)
                    return null;
            } catch (InterruptedException e) {
                throw new UnknownError("Result hasn't been resolved"); //TODO check what to do
            }
        }
        return result;
    }

    public T getResult() {
        return result;
    }
}
