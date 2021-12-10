package net.rawmod.mineclass.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class SmeltingEngine {
  /** Instance unique pré-initialisée */
  private static SmeltingEngine INSTANCE;

  private final HashMap<String, HashMap<Material, Float>> expModifier;

  /** Constructeur privé */
  private SmeltingEngine() {
    expModifier = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized SmeltingEngine getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SmeltingEngine();
    }

    return INSTANCE;
  }

  public synchronized Optional<ItemStack> smelt(
      Player player, Location location, ItemStack itemStack) {
    Optional<ItemStack> result = Optional.empty();
    Iterator<Recipe> iter = Bukkit.recipeIterator();
    while (iter.hasNext()) {
      Recipe recipe = iter.next();
      if (!(recipe instanceof FurnaceRecipe)) continue;
      if (((FurnaceRecipe) recipe).getInput().getType() != itemStack.getType()) continue;
      result = Optional.of(recipe.getResult());
      expModifier.computeIfAbsent(player.getName(), k -> new HashMap<>());
      expModifier.get(player.getName()).putIfAbsent(result.get().getType(), 0.0f);
      expModifier
          .get(player.getName())
          .put(
              result.get().getType(),
              expModifier.get(player.getName()).get(result.get().getType())
                  + ((FurnaceRecipe) recipe).getExperience());
      if (expModifier.get(player.getName()).get(result.get().getType()) >= 1) {
        int exp = expModifier.get(player.getName()).get(result.get().getType()).intValue();
        player.getWorld().spawn(location, ExperienceOrb.class).setExperience(exp);
        expModifier
            .get(player.getName())
            .put(
                result.get().getType(),
                expModifier.get(player.getName()).get(result.get().getType()) - exp);
      }
      break;
    }
    return result;
  }
}
