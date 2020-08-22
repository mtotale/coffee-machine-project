package com.practice.temp.coffeemachine.api;

import java.util.List;
import java.util.concurrent.Future;

/**
 The Coffee Machine interface to be exposed.
 The user of this interface is given methods
 to interact with the machine. The object can
 be obtained from the builder.
 */
public interface CoffeeMachine {
    Future<String> dispenseBeverage(String beverage, int outlet);
    void refillOrAddIngredient(Ingredient ingredient);
    List<Ingredient> getLowIngredients();
    List<Ingredient> getAllIngredients();
    List<String> getAllBeverages();
    int getNumberOfOutlets();
}
