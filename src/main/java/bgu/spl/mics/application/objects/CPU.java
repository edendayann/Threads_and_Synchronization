package bgu.spl.mics.application.objects;

import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private Collection<DataBatch> data; //We will use a LinkedList/Queue
    private int time;
    private int process_time;
    private boolean isAvailable = true;
    private Cluster cluster;

    /**
     * The cluster will call this function in order the deliver a BatchData.
     * @pre: None
     * @post: @pre(this.data.size() + 1) == this.data.size()
     */
    public void addDataBatch(DataBatch dataBatch) {

    }

    /**
     * Takes the first unprocessed DataBatch from the queue and process it.
     * After the process finishes send the process
     * @pre: this.data.size() > 0
     * @pre: this.isAvailable == true
     * @post: this.data.size() = @pre(this.data.size() - 1)
     * @return processed DataBatch
     */
    public DataBatch process() { //TODO check if it needs to be synchronized.
        return null;
    }

    /**
     * Updates the cpu time.
     * @pre: None
     * @post: this.time == @pre(this.time) + 1
     */
    public void updateTime() {

    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Collection<DataBatch> getData() {
        return data;
    }

    public int getCores() {
        return cores;
    }

    public int getTime() {
        return time;
    }
}
