package com.practice.temp.coffeemachine.impl;

import java.util.concurrent.*;

/**
 This class is responsible just for handling
 the outlets. For each of the n outlets,
 it gives an outlet to the thread asking in
 First come basis. And the outlet is blocked
 for subsequent threads until it is released.
 Uses a ConcurrentMap to maintain the outlets.
 */
public class OutletManager {

    private static final ConcurrentMap<Integer, Boolean> outletsUsedMap = new ConcurrentHashMap<>();

    public OutletManager(int number_outlets) {
        for(int i=1;i<=number_outlets;i++) {
            outletsUsedMap.put(i, false);
        }
    }

    public void getOutletIfFree(int outlet) {
        if(!outletsUsedMap.replace(outlet, false, true)) {
            throw new RuntimeException("Outlet " + outlet + " is not free");
        }
    }

    public void freeUpOutlet(int outlet) {
        outletsUsedMap.put(outlet, false);
    }

    public boolean isOutletFree(int outlet) {
        return !outletsUsedMap.get(outlet);
    }

    public int getNumberOfOutlets() {
        return outletsUsedMap.size();
    }
}
