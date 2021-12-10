package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DwarfClass extends MineClassImpl {

  private final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_AXE);
          add(Material.GOLDEN_AXE);
          add(Material.IRON_AXE);
          add(Material.NETHERITE_AXE);
          add(Material.DIAMOND_HOE);
          add(Material.GOLDEN_HOE);
          add(Material.IRON_HOE);
          add(Material.NETHERITE_HOE);
          add(Material.BOW);
          add(Material.TRIDENT);
        }
      };

  private final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.HEALTH_BOOST, 2},
                {PotionEffectType.DAMAGE_RESISTANCE, 1},
                {PotionEffectType.HERO_OF_THE_VILLAGE, 1},
                {PotionEffectType.FAST_DIGGING, 1},
                {PotionEffectType.NIGHT_VISION, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))))
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
    return "dwarf";
  }

  @Override
  public void giveItems(Player player) {}
}
