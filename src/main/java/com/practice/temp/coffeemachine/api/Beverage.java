package com.practice.temp.coffeemachine.api;

import java.util.List;

/**
 Simple POJO class to hold a Beverage object
 along with its recipe(ingredient requirements).
 */
public class Beverage {

    private String name;
    private List<Ingredient> ingredientList;

    public Beverage(String name, List<Ingredient> ingredientList) {
        this.name = name;
        this.ingredientList = ingredientList;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }
}
