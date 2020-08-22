package com.practice.temp.coffeemachine.impl;


import com.practice.temp.coffeemachine.api.Beverage;
import com.practice.temp.coffeemachine.api.CoffeeMachine;
import com.practice.temp.coffeemachine.api.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 The main class depicting an implementation
 of CoffeeMachine interface. It has its own
 OutletManager, BeverageList and ingredientStore.
 When it's initialised, it creates an instance of
 each of the dependent objects and populates them
 with the required parameters. For a user, having
 an instance of this is enough for all interactions
 with the CoffeeMachine. A threadPool is also maintained
 and each dispenseBeverage call is delegated to a
 separate thread. The response is a Future object
 that the user can use to get the drink
 or get an Exception if one has occurred.
*/
public class CoffeeMachineImpl implements CoffeeMachine {

    private final OutletManager outletManager;
    private final IngredientStore ingredientStore;
    private final Map<String, Beverage> beverageCatalogue;
    private final ThreadPoolExecutor threadPoolExecutor;

    public CoffeeMachineImpl(int n, List<Ingredient> ingredients, List<Beverage> beveragesList) {

        outletManager = new OutletManager(n);
        ingredientStore = new IngredientStore(ingredients);

        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);

        beverageCatalogue = new HashMap<>();
        for(Beverage beverage: beveragesList) {
            beverageCatalogue.put(beverage.getName(), beverage);
        }
    }

    public Future<String> dispenseBeverage(String beverage, int outlet) {

        Beverage beverageToDispense = getBeverageInCatalogue(beverage);

        return dispense(beverageToDispense, outlet);
    }

    private Beverage getBeverageInCatalogue(String beverageName) {

        if(beverageCatalogue.containsKey(beverageName)) {

            return beverageCatalogue.get(beverageName);

        } else {
            throw new IllegalArgumentException("No beverage named " + beverageName + " exists");
        }
    }

    private Future<String> dispense(Beverage beverage, int outlet) {
        return threadPoolExecutor.submit(new BeverageDispenser(beverage, outlet, ingredientStore, outletManager));
    }

    @Override
    public void refillOrAddIngredient(Ingredient ingredient) {
        ingredientStore.refillOrAddIngredient(ingredient);
    }

    public void addNewBeverageToCatalogue(Beverage beverage) {
        beverageCatalogue.put(beverage.getName(), beverage);
    }

    @Override
    public List<Ingredient> getLowIngredients() {
        return ingredientStore.getLowIngredients();
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientStore.getListOfAvailableIngredients();
    }

    @Override
    public List<String> getAllBeverages() {
        return new ArrayList<>(beverageCatalogue.keySet());
    }

    @Override
    public int getNumberOfOutlets() {
        return outletManager.getNumberOfOutlets();
    }

}
