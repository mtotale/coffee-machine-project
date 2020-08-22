package com.practice.temp.coffeemachine;


import com.practice.temp.coffeemachine.api.Beverage;
import com.practice.temp.coffeemachine.api.CoffeeMachine;
import com.practice.temp.coffeemachine.api.CoffeeMachineBuilder;
import com.practice.temp.coffeemachine.api.Ingredient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class CoffeeMachineTest {

    @Test
    public void testDispenseDrink() throws ExecutionException, InterruptedException {

        CoffeeMachine coffeeMachine = buildTestCoffeeMachine();

        Future<String> response = coffeeMachine.dispenseBeverage("hot_coffee", 2);

        assertEquals("hot_coffee", response.get());

        //check updated quantities
        assertEquals(5, coffeeMachine.getAllIngredients().size());

        //assert all ingredients exist
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 400));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 100));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 10));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 50));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 70));

        //check if ingredients have become low
        assertEquals(2, coffeeMachine.getLowIngredients().size());
        assertTrue(coffeeMachine.getLowIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 10));
        assertTrue(coffeeMachine.getLowIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 50));
    }

    @Test
    public void testDispenseMultipleDrinksinParallel() throws ExecutionException, InterruptedException {

        CoffeeMachine coffeeMachine = buildTestCoffeeMachine();

        //try to dispense two drinks from one outlet. Expect either error or success based on thread behaviour
        Future<String> response = coffeeMachine.dispenseBeverage("hot_coffee", 1);
        Future<String> response2 = coffeeMachine.dispenseBeverage("hot_tea", 2);

        assertEquals("hot_coffee", response.get());
        assertEquals("hot_tea", response2.get());

        assertEquals(5, coffeeMachine.getAllIngredients().size());

        //assert all ingredients exist
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 100));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 0));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 0));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 40));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 40));

        //check if ingredients have become low
        assertEquals(4, coffeeMachine.getLowIngredients().size());
    }

    @Test
    public void testDispenseMultipleDrinksWithError() throws ExecutionException, InterruptedException {

        CoffeeMachine coffeeMachine = buildTestCoffeeMachine();

        //try to dispense two drinks which are possible
        Future<String> response = coffeeMachine.dispenseBeverage("hot_coffee", 1);
        Future<String> response2 = coffeeMachine.dispenseBeverage("hot_tea", 2);

        //for above dispense to complete
        Thread.sleep(100);

        //try to get drink which is not possible to dispense
        Future<String> response3 = coffeeMachine.dispenseBeverage("hot_tea", 3);

        assertEquals("hot_coffee", response.get());
        assertEquals("hot_tea", response2.get());

        assertEquals(5, coffeeMachine.getAllIngredients().size());

        //assert all ingredients exist
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 100));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 0));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 0));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 40));
        assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 40));

        //check if ingredients have become low
        assertEquals(4, coffeeMachine.getLowIngredients().size());

        Throwable exception = assertThrows(ExecutionException.class, response3::get);

        Exception expectedException = (Exception) exception.getCause();

        assertEquals(RuntimeException.class, expectedException.getClass());

        assertEquals("Cannot make beverage hot_tea: hot_water, hot_milk, ginger_syrup not sufficient.", expectedException.getMessage());
    }

    @Test
    public void testDispenseDrinkFromBusyOutlet() {

        CoffeeMachine coffeeMachine = buildTestCoffeeMachine();

        //try to dispense two drinks from one outlet. Expect either error or success based on thread behaviour.
        Future<String> response = coffeeMachine.dispenseBeverage("hot_coffee", 2);
        Future<String> response2 = coffeeMachine.dispenseBeverage("hot_tea", 2);

        try {
            assertEquals("hot_coffee", response.get());
        } catch (Exception e) {
            //confirm that coffee was not dispensed. return since assertion is made.
            assertEquals("java.lang.RuntimeException: Cannot make beverage hot_coffee: Outlet 2 is not free", e.getMessage());
            return;
        }

        try {
            //check if tea was also dispensed
            if(response2.get().equals("hot_tea")) {

                //check updated quantities
                assertEquals(5, coffeeMachine.getAllIngredients().size());

                //assert all ingredients exist
                assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_water") && i.getQuantity() == 100));
                assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("hot_milk") && i.getQuantity() == 0));
                assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("ginger_syrup") && i.getQuantity() == 0));
                assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("sugar_syrup") && i.getQuantity() == 40));
                assertTrue(coffeeMachine.getAllIngredients().stream().anyMatch(i -> i.getName().equals("tea_leaves_syrup") && i.getQuantity() == 40));

                //check if ingredients have become low
                assertEquals(4, coffeeMachine.getLowIngredients().size());
            }
        } catch (Exception e) {
            assertEquals("java.lang.RuntimeException: Cannot make beverage hot_tea: Outlet 2 is not free", e.getMessage());
        }
    }

    private CoffeeMachine buildTestCoffeeMachine() {
        CoffeeMachineBuilder coffeeMachineBuilder = new CoffeeMachineBuilder();

        coffeeMachineBuilder.setNumberOfOutlets(3);

        coffeeMachineBuilder.addIngredient(new Ingredient("hot_water", 500));
        coffeeMachineBuilder.addIngredient(new Ingredient("hot_milk", 500));
        coffeeMachineBuilder.addIngredient(new Ingredient("sugar_syrup", 100));
        coffeeMachineBuilder.addIngredient(new Ingredient("ginger_syrup", 40));
        coffeeMachineBuilder.addIngredient(new Ingredient("tea_leaves_syrup", 100));

        coffeeMachineBuilder.addBeverage(new Beverage("hot_tea", createIngredientsListForTea()));
        coffeeMachineBuilder.addBeverage(new Beverage("hot_coffee", createIngredientsListForCoffee()));

        return coffeeMachineBuilder.build();
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
