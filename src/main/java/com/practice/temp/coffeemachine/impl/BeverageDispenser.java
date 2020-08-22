package com.practice.temp.coffeemachine.impl;

import com.practice.temp.coffeemachine.api.Beverage;

import java.util.concurrent.Callable;

/**
 A Callable that is used by coffee machine
 to trigger a dispense action. Invoked in a separate
 thread. It first checks if the outlet is free,
 if free it blocks the outlet. Then checks the ingredient
 store to see if beverage to dispense can be created
 with its requirements. Finally releases the outlet.
 */
public class BeverageDispenser implements Callable<String> {

    Beverage beverage;
    int outlet;
    IngredientStore ingredientStore;
    OutletManager outletManager;

    public BeverageDispenser(Beverage beverage, int outlet, IngredientStore ingredientStore, OutletManager outletManager) {
        this.beverage = beverage;
        this.outlet = outlet;
        this.ingredientStore = ingredientStore;
        this.outletManager = outletManager;
    }

    @Override
    public String call() throws Exception {

        try {
            //check to see if the outlet can be used
            outletManager.getOutletIfFree(outlet);

            //get ingredients from the store. this throws exception if not possible
            ingredientStore.getIngredientsFromStore(beverage.getIngredientList());

            //successfully made beverage
            return beverage.getName();

        } catch (Exception e) {

            throw new RuntimeException("Cannot make beverage "+ beverage.getName() + ": " + e.getMessage());

        } finally {
            //free the outlet to be used by other threads
            outletManager.freeUpOutlet(outlet);
        }
    }
}
