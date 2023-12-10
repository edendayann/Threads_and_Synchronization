package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {
    private static CPU cpu;
    private static DataBatch dataBatch;
    private static int time;

    @BeforeEach
    void setUp() {
        cpu = new CPU();
        dataBatch = new DataBatch();
        time = 1;
    }

    @Test
    void getDataBatch() {
        int size = cpu.getData().size();
        cpu.addDataBatch(dataBatch);
        assertEquals(size+1, cpu.getData().size());
    }

    @Test
    void process() {
        DataBatch dataBatch = new DataBatch();
        int ticks=0;

        if (dataBatch.getData().getType() == Data.Type.Images)
            ticks = 4;
        if (dataBatch.getData().getType() == Data.Type.Text)
            ticks = 2;
        if (dataBatch.getData().getType() == Data.Type.Tabular)
            ticks = 1;

        cpu.addDataBatch(dataBatch);
        assertFalse(cpu.isEmpty());
        assertTrue(cpu.isAvailable());
        int time = cpu.getTime();
        cpu.process();
        int sleeping_time = (32/cpu.getCores())*ticks;

        assertEquals(time+sleeping_time, cpu.getTime());
    }

    @Test
    void updateTime() {
        int time = cpu.getTime();
        cpu.updateTime();
        assertEquals(time+1, cpu.getTime());
    }
}