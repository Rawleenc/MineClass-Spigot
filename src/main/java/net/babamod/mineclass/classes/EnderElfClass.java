package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class EnderElfClass extends MineClassImpl {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_SWORD);
          add(Material.GOLDEN_SWORD);
          add(Material.IRON_SWORD);
          add(Material.NETHERITE_SWORD);
          add(Material.DIAMOND_PICKAXE);
          add(Material.GOLDEN_PICKAXE);
          add(Material.IRON_PICKAXE);
          add(Material.NETHERITE_PICKAXE);
          add(Material.DIAMOND_SHOVEL);
          add(Material.GOLDEN_SHOVEL);
          add(Material.IRON_SHOVEL);
          add(Material.NETHERITE_SHOVEL);
          add(Material.DIAMOND_HOE);
          add(Material.GOLDEN_HOE);
          add(Material.IRON_HOE);
          add(Material.NETHERITE_HOE);
          add(Material.DIAMOND_AXE);
          add(Material.GOLDEN_AXE);
          add(Material.IRON_AXE);
          add(Material.NETHERITE_AXE);
          add(Material.CROSSBOW);
          add(Material.BOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.NIGHT_VISION, 1},
                {PotionEffectType.ABSORPTION, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.ENDER_PEARL, new ArrayList<Pair<Enchantment, Integer>>()))
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
    return "ender_elf";
  }

  @Override
  public void reapplyEffects(Player player) {
    super.reapplyEffects(player);
    if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
      PotionEffect saturation =
          new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 9, false, false);
      player.addPotionEffect(saturation);
    }
  }

  @Override
  public void giveItems(Player player) {
    if (!player.getInventory().contains(Material.ENDER_PEARL)) {
      ItemStack itemStack = new ItemStack(Material.ENDER_PEARL, 2);
      enchantItem(itemStack);
      player.getInventory().addItem(itemStack);
    }
  }
}
