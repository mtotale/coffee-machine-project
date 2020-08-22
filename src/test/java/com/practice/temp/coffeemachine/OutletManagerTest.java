package com.practice.temp.coffeemachine;

import com.practice.temp.coffeemachine.impl.OutletManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OutletManagerTest {

    @Test
    public void testGetAndReleaseOutlet() {

        OutletManager outletManager = new OutletManager(3);

        outletManager.getOutletIfFree(2);

        assertFalse(outletManager.isOutletFree(2));

        Throwable exception = assertThrows(RuntimeException.class, () -> outletManager.getOutletIfFree(2));
        assertEquals("Outlet 2 is not free", exception.getMessage());

        outletManager.freeUpOutlet(2);

        assertTrue(outletManager.isOutletFree(2));
    }
}
