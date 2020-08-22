package com.practice.temp.coffeemachine.api;

import com.practice.temp.coffeemachine.impl.CoffeeMachineImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 The Coffee Machine Builder class to be used for
 creating an instance of the CoffeeMachine interface.
 This builder provides for a user to add 3 important
 parts of a coffee machine. The number of outlets,
 the beverages(with recipes) that it should dispense and the
 list of ingredients and their quantity.
 */
public class CoffeeMachineBuilder {

    private List<Beverage> beverageList;
    private List<Ingredient> ingredientList;
    int outlets;

    public CoffeeMachineBuilder() {
        beverageList = new ArrayList<>();
        ingredientList = new ArrayList<>();
    }

    public CoffeeMachineBuilder addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient);
        return this;
    }

    public CoffeeMachineBuilder addBeverage(Beverage beverage) {
        beverageList.add(beverage);
        return this;
    }

    public CoffeeMachineBuilder addIngredients(Collection<Ingredient> ingredients) {
        ingredientList.addAll(ingredients);
        return this;
    }

    public CoffeeMachineBuilder addBeverages(Collection<Beverage> beverages) {
        beverageList.addAll(beverages);
        return this;
    }

    public CoffeeMachineBuilder setNumberOfOutlets(int n) {
        outlets = n;
        return this;
    }

    public CoffeeMachine build() {
        return new CoffeeMachineImpl(outlets, ingredientList, beverageList);
    }
}
