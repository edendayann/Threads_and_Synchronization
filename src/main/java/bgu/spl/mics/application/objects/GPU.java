package bgu.spl.mics.application.objects;

import java.util.LinkedList;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private Cluster cluster;
    private Data data;
    private LinkedList<DataBatch> disk;
    private LinkedList<DataBatch> vRAM;
    int time;
    private boolean isAvailable;
    private int max_num_batches_in_vRAM;
    private int start_process;
    private int total_number_of_batches;
    private int processed_number_of_batches;

    public Type getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public Data getData() {
        return data;
    }

    public LinkedList<DataBatch> getvRAM() {
        return vRAM;
    }

    public LinkedList<DataBatch> getDisk() {
        return disk;
    }

    public int getMax_num_batches_in_vRAM() {
        return max_num_batches_in_vRAM;
    }

    public int getTotal_number_of_batches() {
        return total_number_of_batches;
    }




    /**
     * @pre: num <= disk.size()
     * @post: disk.size() == @pre(disk.size())-num
     * @param num
     * @return
     */
    public DataBatch[] sendDataBatch(int num) {
        return null;
    }

    /**
     * @pre: dataBatches.length() + vRAM.size() <= max_num_batches_in_vRAM
     * @post: dataBatches.length() + @pre(vRAM.size()) = vRAM.size()
     * @param dataBatches
     */
    public void receiveDataBatch(DataBatch[] dataBatches) {

    }

    /**
     * @pre: None
     * @post: disk.length() == (data.getSize()/1000)+1
     */
    public void divide() {

    }

    /**
     * Updates the cpu time.
     * @pre: None
     * @post: this.time == @pre(this.time) + 1
     */
    public void updateTime() {

    }

    /**
     * @pre: vRAM.size() > 0
     * @pre: this.isAvailable == true
     * @post: vRAM.size() = @pre(vRAM.size() - 1)
     */
    public void process() {

    }

    public boolean isEmpty() {
        return vRAM.isEmpty();
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
