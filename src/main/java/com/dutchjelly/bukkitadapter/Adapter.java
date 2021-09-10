package com.dutchjelly.bukkitadapter;


import com.dutchjelly.craftenhance.CraftEnhance;
import org.bukkit.DyeColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Adapter {


    public static List<String> compatibleVersions(){
        return Arrays.asList("1.9", "1.10", "1.11", "1.12", "1.13", "1.14", "1.15", "1.16", "1.17");
    }


    public static Material getMaterial(String name){
        try {
            return Material.getMaterial(name, false);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static ItemStack getColoredItem(String name, DyeColor color){
        return new ItemStack(Material.valueOf(color.name() + "_" + name));
    }

    private static NamespacedKey getNamespacedKey(Plugin plugin, String key) {
        return new NamespacedKey(plugin, key);
    }

    public static ShapedRecipe getShapedRecipe(Plugin plugin, String key, ItemStack result){
        return new ShapedRecipe(getNamespacedKey(plugin, key), result);
    }

    public static ShapelessRecipe getShapelessRecipe(Plugin plugin, String key, ItemStack result){
        return new ShapelessRecipe(getNamespacedKey(plugin, key), result);
    }

    public static ItemStack setDurability(ItemStack item, int damage){
        item.setDurability((short)damage);
        return item;
    }

    public static void setIngredient(ShapedRecipe recipe, char key, ItemStack ingredient){
        if(!CraftEnhance.self().getConfig().getBoolean("learn-recipes")){
            recipe.setIngredient(key, ingredient.getType());
            return;
        }
        RecipeChoice.ExactChoice choice = new RecipeChoice.ExactChoice(ingredient);
        recipe.setIngredient(key, choice);
    }

    public static void addIngredient(ShapelessRecipe recipe, ItemStack ingredient){
        if(!CraftEnhance.self().getConfig().getBoolean("learn-recipes")){
            recipe.addIngredient(ingredient.getType());
            return;
        }
        RecipeChoice.ExactChoice choice = new RecipeChoice.ExactChoice(ingredient);
        recipe.addIngredient(choice);
    }

    public static void discoverRecipes(Player player, List<Recipe> recipes){
        for (Recipe recipe : recipes) {
            if(recipe instanceof ShapedRecipe){
                ShapedRecipe shaped = (ShapedRecipe) recipe;
                player.discoverRecipe(shaped.getKey());
            }else if(recipe instanceof ShapelessRecipe){
                ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                player.discoverRecipe(shapeless.getKey());
            }
        }
    }

    public static void setOwningPlayer(SkullMeta meta, OfflinePlayer player){
        meta.setOwningPlayer(player);
    }

    public static Recipe filterRecipes(List<Recipe> recipes, String name){
        for(Recipe r : recipes){
            String id = getRecipeIdentifier(r);
            if(id == null) continue;
            if(id.equalsIgnoreCase(name))
                return r;
        }

        return recipes.stream().filter(Objects::nonNull).filter(x -> x.getResult().getType().name().equalsIgnoreCase(name)).findFirst().orElse(null);

    }


    public static String getRecipeIdentifier(Recipe r){
        if (r instanceof Keyed) {
            return ((Keyed) r).getKey().toString();
        } else {
            return r.getResult().getType().name();
        }
    }

}
