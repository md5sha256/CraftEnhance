package com.dutchjelly.craftenhance.crafthandling.util;


import com.dutchjelly.bukkitadapter.Adapter;
import com.dutchjelly.craftenhance.CraftEnhance;
import com.dutchjelly.craftenhance.crafthandling.recipes.WBRecipe;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class ServerRecipeTranslator {

    private static final String KeyPrefix = "cehrecipe";

    private static List<String> UsedKeys = new ArrayList<>();

    public static ShapedRecipe translateShapedEnhancedRecipe(ItemStack[] content, ItemStack result, String key){
        if(Arrays.stream(content).noneMatch(Objects::nonNull))
            return null;
        Random r = new Random();
        String recipeKey = key.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        recipeKey = recipeKey.trim();
        while(UsedKeys.contains(recipeKey)) recipeKey += String.valueOf(r.nextInt(10));
        if(!UsedKeys.contains(recipeKey))
            UsedKeys.add(recipeKey);
        ShapedRecipe shaped = Adapter.getShapedRecipe(
                CraftEnhance.getPlugin(CraftEnhance.class), KeyPrefix + recipeKey, result
        );
        shaped.shape(GetShape(content));
        MapIngredients(shaped, content);
        return shaped;
    }

    public static ShapedRecipe translateShapedEnhancedRecipe(WBRecipe recipe){
        return translateShapedEnhancedRecipe(recipe.getContent(), recipe.getResult(), recipe.getKey());
    }



    public static ShapelessRecipe translateShapelessEnhancedRecipe(ItemStack[] content, ItemStack result, String key){
        List<ItemStack> ingredients = Arrays.stream(content).filter(Objects::nonNull).collect(Collectors.toList());
        if(ingredients.size() == 0)
            return null;

        Random r = new Random();
        String recipeKey = key.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        recipeKey = recipeKey.trim();
        while(UsedKeys.contains(recipeKey)) recipeKey += String.valueOf(r.nextInt(10));
        if(!UsedKeys.contains(recipeKey))
            UsedKeys.add(recipeKey);
        ShapelessRecipe shapeless = Adapter.getShapelessRecipe(
                CraftEnhance.getPlugin(CraftEnhance.class), KeyPrefix + recipeKey, result
        );
        ingredients.forEach(x -> Adapter.addIngredient(shapeless, x));
        return shapeless;
    }

    public static ShapelessRecipe translateShapelessEnhancedRecipe(WBRecipe recipe){
        return translateShapelessEnhancedRecipe(recipe.getContent(), recipe.getResult(), recipe.getKey());
    }


    public static ItemStack[] translateShapedRecipe(ShapedRecipe recipe){
        ItemStack[] content = new ItemStack[9];
        String[] shape = recipe.getShape();
        int columnIndex;
        for(int i = 0; i < shape.length; i++){
            columnIndex = 0;
            for(char c : shape[i].toCharArray()){
                content[(i*3) + columnIndex] = recipe.getIngredientMap().get(c);
                columnIndex++;
            }
        }
        return content;
    }

    public static ItemStack[] translateShapelessRecipe(ShapelessRecipe recipe){
        if(recipe == null || recipe.getIngredientList() == null) return null;
        return recipe.getIngredientList().toArray(new ItemStack[0]);
    }


    //Gets the shape of the recipe 'content'.
    private static String[] GetShape(ItemStack[] content){
        String[] recipeShape = {"","",""};
        for(int i = 0; i < 9; i++){
            if(content[i] != null)
                recipeShape[i/3] += (char)('A' + i);
            else
                recipeShape[i/3] += ' ';
        }
        return TrimShape(recipeShape);
    }

    //Trims the shape so that there are no redundant spaces or elements in shape.
    private static String[] TrimShape(String[] shape){
        if(shape.length == 0) return shape;

        //Trim the start and end of the list
        List<String> trimmed = Arrays.asList(shape);
        while(!trimmed.isEmpty() && (trimmed.get(0).trim().equals("")
                ||trimmed.get(trimmed.size()-1).trim().equals(""))){
            if(trimmed.get(0).trim().equals(""))
                trimmed = trimmed.subList(1, trimmed.size());
            else trimmed = trimmed.subList(0, trimmed.size()-1);
        }

        if(trimmed.isEmpty())
            throw new IllegalStateException("empty shape is not allowed");

        //Find the first and last indexes of the total shape.
        int firstIndex = trimmed.get(0).length();
        int lastIndex = 0;

        for(String line : trimmed){
            int firstChar = 0;
            while(firstChar < line.length() && line.charAt(firstChar) == ' ') firstChar++;
            firstIndex = Math.min(firstChar, firstIndex);
            lastIndex = Math.max(lastIndex, StringUtils.stripEnd(line, " ").length());
        }

        //Trim the shape with the first and last indexes.
        final int first = firstIndex, last = lastIndex;
        return trimmed.stream().map(x -> x.substring(first, last)).toArray(String[]::new);
    }

    private static void MapIngredients(ShapedRecipe recipe, ItemStack[] content){
        for(int i = 0; i < 9; i++){
            if(content[i] != null){
                Adapter.setIngredient(recipe, (char) ('A' + i), content[i]);
            }
        }
    }

}
