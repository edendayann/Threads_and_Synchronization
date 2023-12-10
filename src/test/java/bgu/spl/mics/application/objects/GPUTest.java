package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {
    private static GPU gpu;
    private static Cluster cluster;

    @BeforeEach
    void setUp() {
        gpu = new GPU();
    }

    @Test
    void sendDataBatch() {
        int number_data_batch = gpu.getDisk().size();
        int num = 5;
        gpu.sendDataBatch(num);
        int curr_number = gpu.getDisk().size();
        assertEquals(number_data_batch-num, curr_number);
    }

    @Test
    void receiveDataBatch() {
        int number_data_batch = gpu.getvRAM().size();
        DataBatch dataBatch[] = {new DataBatch(), new DataBatch()};
        gpu.receiveDataBatch(dataBatch);
        int curr_number = gpu.getvRAM().size();
        assertEquals(number_data_batch-dataBatch.length, curr_number);
    }

    @Test
    void divide() {
        int size = gpu.getData().getSize();
        gpu.divide();
        assertEquals((size/1000)+1,gpu.getDisk().size());
    }

    @Test
    void updateTime() {
        int time = gpu.getTime();
        gpu.updateTime();
        assertEquals(time+1, gpu.getTime());
    }

    @Test
    void process() {
        int ticks=0;
        DataBatch dataBatch[] = {new DataBatch()};
        if (gpu.getType()== GPU.Type.GTX1080)
            ticks = 4;
        if (gpu.getType()== GPU.Type.RTX2080)
            ticks = 2;
        if (gpu.getType()== GPU.Type.RTX3090)
            ticks = 1;

        gpu.receiveDataBatch(dataBatch);
        assertFalse(gpu.isEmpty());
        assertTrue(gpu.isAvailable());
        int time = gpu.getTime();
        gpu.process();

        assertEquals(time+ticks, gpu.getTime());
    }
}