package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeastMasterClass extends MineClassImpl {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_PICKAXE);
          add(Material.GOLDEN_PICKAXE);
          add(Material.IRON_PICKAXE);
          add(Material.NETHERITE_PICKAXE);
          add(Material.CROSSBOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.NIGHT_VISION, 1},
                {PotionEffectType.SLOW, 1},
                {PotionEffectType.WEAKNESS, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.BONE, new ArrayList<Pair<Enchantment, Integer>>()),
              new AbstractMap.SimpleEntry<>(
                  Material.SALMON, new ArrayList<Pair<Enchantment, Integer>>()),
              new AbstractMap.SimpleEntry<>(
                  Material.SADDLE, new ArrayList<Pair<Enchantment, Integer>>()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  @Override
  public Set<Material> getForbiddenItems() {
    return forbiddenItems;
  }

  @Override
  public Map<PotionEffectType, Integer> getPotionEffects() {
    return potionEffects;
  }

  @Override
  public Map<Material, List<Pair<Enchantment, Integer>>> getClassEnchantments() {
    return classEnchantments;
  }

  @Override
  public String getCode() {
    return "beast_master";
  }

  @Override
  public void giveItems(Player player) {
    if (!player.getInventory().contains(Material.BONE)) {
      ItemStack itemStack = new ItemStack(Material.BONE, 1);
      enchantItem(itemStack);
      player.getInventory().addItem(itemStack);
    }
    if (!player.getInventory().contains(Material.SALMON)) {
      ItemStack itemStack = new ItemStack(Material.SALMON, 1);
      enchantItem(itemStack);
      player.getInventory().addItem(itemStack);
    }
    if (!player.getInventory().contains(Material.SADDLE)) {
      ItemStack itemStack = new ItemStack(Material.SADDLE, 1);
      enchantItem(itemStack);
      player.getInventory().addItem(itemStack);
    }
  }
}
