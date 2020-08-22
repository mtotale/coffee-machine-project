package com.practice.temp.coffeemachine;

import com.practice.temp.coffeemachine.api.Beverage;
import com.practice.temp.coffeemachine.api.CoffeeMachine;
import com.practice.temp.coffeemachine.api.CoffeeMachineBuilder;
import com.practice.temp.coffeemachine.api.Ingredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoffeeMachineBuilderTest {

    @Test
    public void testCoffeeMachineBuild() {

        CoffeeMachineBuilder coffeeMachineBuilder = new CoffeeMachineBuilder();

        coffeeMachineBuilder.setNumberOfOutlets(3);

        coffeeMachineBuilder.addIngredient(new Ingredient("hot_water", 500));
        coffeeMachineBuilder.addIngredient(new Ingredient("hot_milk", 500));
        coffeeMachineBuilder.addIngredient(new Ingredient("sugar_syrup", 100));
        coffeeMachineBuilder.addIngredient(new Ingredient("ginger_syrup", 40));
        coffeeMachineBuilder.addIngredient(new Ingredient("tea_leaves_syrup", 100));

        coffeeMachineBuilder.addBeverage(new Beverage("hot_tea", createIngredientsListForTea()));
        coffeeMachineBuilder.addBeverage(new Beverage("hot_coffee", createIngredientsListForCoffee()));

        CoffeeMachine coffeeMachine = coffeeMachineBuilder.build();

        assertEquals(3, coffeeMachine.getNumberOfOutlets());

        assertEquals(5, coffeeMachine.getAllIngredients().size());

        //assert all ingredients exist
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 500));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 500));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 40));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 100));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 100));


        assertEquals(2, coffeeMachine.getAllBeverages().size());
        //assert all beverages exist
        assertTrue(coffeeMachine.getAllBeverages().stream().anyMatch(i -> i.equals("hot_coffee")));
        assertTrue(coffeeMachine.getAllBeverages().stream().anyMatch(i -> i.equals("hot_tea")));


        assertEquals(1, coffeeMachine.getLowIngredients().size());
        assertTrue(coffeeMachine.getLowIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 40));
    }

    private List<Ingredient> createIngredientsListForTea() {
        Ingredient ingredient1 = new Ingredient("hot_water", 300);
        Ingredient ingredient2 = new Ingredient("hot_milk", 100);
        Ingredient ingredient3 = new Ingredient("ginger_syrup", 10);
        Ingredient ingredient4 = new Ingredient("sugar_syrup", 10);
        Ingredient ingredient5 = new Ingredient("tea_leaves_syrup", 30);

        List<Ingredient> ret = new ArrayList<>();
        Collections.addAll(ret, ingredient1, ingredient2, ingredient3, ingredient4, ingredient5);

        return ret;
    }

    private List<Ingredient> createIngredientsListForCoffee() {
        Ingredient ingredient1 = new Ingredient("hot_water", 100);
        Ingredient ingredient2 = new Ingredient("hot_milk", 400);
        Ingredient ingredient3 = new Ingredient("ginger_syrup", 30);
        Ingredient ingredient4 = new Ingredient("sugar_syrup", 50);
        Ingredient ingredient5 = new Ingredient("tea_leaves_syrup", 30);

        List<Ingredient> ret = new ArrayList<>();
        Collections.addAll(ret, ingredient1, ingredient2, ingredient3, ingredient4, ingredient5);

        return ret;
    }

}
