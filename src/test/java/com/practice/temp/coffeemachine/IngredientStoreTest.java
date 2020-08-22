package com.practice.temp.coffeemachine;

import com.practice.temp.coffeemachine.api.Ingredient;
import com.practice.temp.coffeemachine.impl.IngredientStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientStoreTest {

    @Test
    public void testIngredientStorePopulate() {

        IngredientStore ingredientStore = getFilledIngredientStore();

        List<Ingredient> ingredientsStored = ingredientStore.getListOfAvailableIngredients();

        assertEquals(5, ingredientsStored.size());

        //assert all elements exist
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 500));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 500));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 100));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 100));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 100));
    }

    @Test
    public void testIngredientStoreConsume() {
        IngredientStore ingredientStore = getFilledIngredientStore();

        //consume part of ingredients
        ingredientStore.getIngredientsFromStore(createConsumableIngredients());

        List<Ingredient> ingredientsStored = ingredientStore.getListOfAvailableIngredients();

        assertEquals(5, ingredientsStored.size());

        //assert all elements exist
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 200));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 400));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 90));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 90));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 70));

        //check failure to consume and correct exception thrown
        Throwable exception = assertThrows(RuntimeException.class, () -> ingredientStore.getIngredientsFromStore(createConsumableIngredients()));
        assertEquals("hot_water not sufficient.", exception.getMessage());
    }

    @Test
    public void testIngredientRefill() {

        IngredientStore ingredientStore = getFilledIngredientStore();

        List<Ingredient> ingredientsStored = ingredientStore.getListOfAvailableIngredients();

        assertEquals(5, ingredientsStored.size());

        //assert all elements exist
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 500));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 500));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 100));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 100));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 100));

        ingredientStore.refillOrAddIngredient(new Ingredient("sugar_syrup", 100));
        ingredientStore.refillOrAddIngredient(new Ingredient("cold_milk", 500));

        ingredientsStored = ingredientStore.getListOfAvailableIngredients();

        assertEquals(6, ingredientsStored.size());

        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("cold_milk") && i.getQuantity() == 500));
        assertTrue(ingredientsStored.stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 200));
    }


    private List<Ingredient> createConsumableIngredients() {
        Ingredient ingredient1 = new Ingredient("hot_water", 300);
        Ingredient ingredient2 = new Ingredient("hot_milk", 100);
        Ingredient ingredient3 = new Ingredient("ginger_syrup", 10);
        Ingredient ingredient4 = new Ingredient("sugar_syrup", 10);
        Ingredient ingredient5 = new Ingredient("tea_leaves_syrup", 30);

        List<Ingredient> ret = new ArrayList<>();
        Collections.addAll(ret, ingredient1, ingredient2, ingredient3, ingredient4, ingredient5);

        return ret;
    }

    private IngredientStore getFilledIngredientStore() {
        List<Ingredient> ingredientList = createIngredients();
        return new IngredientStore(ingredientList);
    }

    private List<Ingredient> createIngredients() {
        Ingredient ingredient1 = new Ingredient("hot_water", 500);
        Ingredient ingredient2 = new Ingredient("hot_milk", 500);
        Ingredient ingredient3 = new Ingredient("ginger_syrup", 100);
        Ingredient ingredient4 = new Ingredient("sugar_syrup", 100);
        Ingredient ingredient5 = new Ingredient("tea_leaves_syrup", 100);

        List<Ingredient> ret = new ArrayList<>();
        Collections.addAll(ret, ingredient1, ingredient2, ingredient3, ingredient4, ingredient5);

        return ret;
    }
}
