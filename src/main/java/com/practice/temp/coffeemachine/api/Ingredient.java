package com.practice.temp.coffeemachine.api;

/**
 Simple POJO class to hold an Ingredient object
 along with its quantity.
 */
public class Ingredient {
    String name;
    int quantity;

    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
