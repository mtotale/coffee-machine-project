package com.practice.temp.coffeemachine.impl;

import com.practice.temp.coffeemachine.api.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 The IngredientStore acts like an in-memory
 store for managing the quantity of ingredients
 in the coffee machine. All methods have synchronised
 access to the Map. So all gets and puts are
 consistent.
 */
public class IngredientStore {

    //low meter threshold
    private static final int LOW_THRESHOLD = 50;

    private final Map<String, Integer> ingredientQuantity = new HashMap<>();

    public IngredientStore(List<Ingredient> ingredients) {
        if(ingredients != null) {
            for(Ingredient ingredient: ingredients) {
                ingredientQuantity.put(ingredient.getName(), ingredient.getQuantity());
            }
        }
    }

    public void getIngredientsFromStore(List<Ingredient> requiredIngredients) {

        synchronized (ingredientQuantity) {

            List<String> unavailableIngredients = requiredIngredients
                    .stream()
                    .filter(ingredient -> !ingredientQuantity.containsKey(ingredient.getName()))
                    .map(Ingredient::getName)
                    .collect(Collectors.toList());

            List<String> insufficientIngredients = requiredIngredients
                    .stream()
                    .filter(ingredient -> ingredient.getQuantity() > ingredientQuantity.get(ingredient.getName()))
                    .map(Ingredient::getName)
                    .collect(Collectors.toList());

            if(unavailableIngredients.size() > 0 || insufficientIngredients.size() > 0) {
                StringBuilder sb = new StringBuilder();

                if(unavailableIngredients.size() > 0) {
                    for(String s: unavailableIngredients) {
                        sb.append(s);
                        sb.append(", ");
                    }
                    sb.deleteCharAt(sb.length()-2);
                    sb.append("not available.");
                }
                if(insufficientIngredients.size() > 0) {
                    for(String s: insufficientIngredients) {
                        sb.append(s);
                        sb.append(", ");

                    }
                    sb.deleteCharAt(sb.length()-2);
                    sb.append("not sufficient.");
                }

                throw new RuntimeException(sb.toString());
            }

            for(Ingredient requiredIngredient: requiredIngredients) {
                ingredientQuantity.put(requiredIngredient.getName(),
                        ingredientQuantity.get(requiredIngredient.getName()) - requiredIngredient.getQuantity());
            }
        }

    }

    public void refillOrAddIngredient(Ingredient ingredient) {

        synchronized (ingredientQuantity) {
            ingredientQuantity.put(ingredient.getName(),
                    ingredientQuantity.getOrDefault(ingredient.getName(), 0) + ingredient.getQuantity());
        }
    }

    public List<Ingredient> getLowIngredients() {

        synchronized (ingredientQuantity) {
            return ingredientQuantity
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() <= LOW_THRESHOLD)
                    .map(entry -> new Ingredient(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
    }

    public List<Ingredient> getListOfAvailableIngredients() {
        synchronized (ingredientQuantity) {
            return ingredientQuantity
                    .entrySet()
                    .stream()
                    .map(entry -> new Ingredient(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
