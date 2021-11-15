package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.*;

public class Day_21 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_21.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/21/input.txt");
        List<Recipe> recipes = new ArrayList<>();
        for (var line : input) {
            recipes.add(new Recipe(line));
        }
        final List<String> allergensToSolve = new ArrayList<>();
        // Allergen key, ingredient value
        final Map<String, String> allergensWithIngredients = new HashMap<>();
        // Ingredient key, allergen value, empty if no allergen
        final Map<String, String> ingredientsWithAllergens = new HashMap<>();

        for (var recipe : recipes) {
            for (var allergen : recipe.allergensSet) {
                if (!allergensToSolve.contains(allergen)) {
                    allergensToSolve.add(allergen);
                }
            }
        }

        var changed = true;
        totalWhile:
        while (allergensToSolve.size() > 0 && changed) {
            changed = false;
            for (var allergen : allergensToSolve) {
                var solved = false;
                var isIngredient = "";
                //solve
                // get all recipes that have this allergen
                List<Recipe> recipesWithAllergen = new ArrayList<>();
                for (var recipe : recipes) {
                    if (recipe.allergensSet.contains(allergen)) {
                        recipesWithAllergen.add(recipe);
                    }
                }
                // Find all ingredients that exist in each recipe, then remove the ones that are solved
                if (recipesWithAllergen.size() == 1) {
                    var recipe = recipesWithAllergen.get(0);
                    // Can't infer anything here unless it is one ingredient or the other ingredient has been solved
                    List<String> unsolvedIngredients = new ArrayList<>();
                    for (var ingredient : recipe.ingredientsSet) {
                        if (!ingredientsWithAllergens.containsKey(ingredient)) {
                            unsolvedIngredients.add(ingredient);
                        }
                    }
                    if (unsolvedIngredients.size() == 1) {
                        isIngredient = unsolvedIngredients.get(0);
                        solved = true;
                    }
                } else {
                    // Find all ingredients that exist in each
                    List<String> possibleIngredients;
                    var firstRecipe = recipesWithAllergen.get(0);
                    possibleIngredients = new ArrayList<>(firstRecipe.ingredientsSet);
                    for (var recipe : recipesWithAllergen) {
                        possibleIngredients.removeIf(ingredient -> !recipe.ingredientsSet.contains(ingredient));
                    }
                    // Now remove any ingredients that are solved
                    possibleIngredients.removeIf(ingredient -> ingredientsWithAllergens.containsKey(ingredient));
                    if (possibleIngredients.size() == 1) {
                        isIngredient = possibleIngredients.get(0);
                        solved = true;
                    }
                }

                if (solved) {
                    allergensToSolve.remove(allergen);
                    ingredientsWithAllergens.put(isIngredient, allergen);
                    allergensWithIngredients.put(allergen, isIngredient);
                    changed = true;
                    continue totalWhile;
                }
            }
        }

        // Count how many times ingredients without allergens appear
        var totalCount = 0;
        for (var recipe : recipes) {
            for (var ingredient : recipe.ingredients) {
                if (!ingredientsWithAllergens.containsKey(ingredient)) {
                    totalCount += 1;
                }
            }
        }
        LogUtilities.log("Part 1 total count: " + totalCount);

        List<String> dangerousAllergens = new ArrayList<>(allergensWithIngredients.keySet());
        Collections.sort(dangerousAllergens);
        var list = "";
        for (var allergen : dangerousAllergens) {
            if (list.length() > 0) {
                list += ",";
            }
            list += allergensWithIngredients.get(allergen);
        }
        LogUtilities.log("Part 2: " + list);


        // One allergen per ingredient
        // Each ingredient contains zero or one allergen
        // Identify all allergens first and their corresponding ingredient
    }
    static class Recipe {
        public List<String> ingredients;
        public List<String> allergens;
        public Set<String> ingredientsSet;
        public Set<String> allergensSet;
        Recipe(String rawLine) {
            var split = StringUtilities.splitStringIntoList(rawLine, " (contains ");
            ingredients = StringUtilities.splitStringIntoList(split.get(0), " ");
            allergens = StringUtilities.splitStringIntoList(StringUtilities.removeEndChunk(split.get(1), ")"), ", ");
            ingredientsSet = new HashSet<>(ingredients);
            allergensSet = new HashSet<>(allergens);
        }
        public boolean containsAllergen(final String allergen) {
            return allergensSet.contains(allergen);
        }
    }
}