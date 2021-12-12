package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireDwarfClass extends MineClassImpl {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_SWORD);
          add(Material.GOLDEN_SWORD);
          add(Material.IRON_SWORD);
          add(Material.NETHERITE_SWORD);
          add(Material.DIAMOND_HOE);
          add(Material.GOLDEN_HOE);
          add(Material.IRON_HOE);
          add(Material.NETHERITE_HOE);
          add(Material.DIAMOND_SHOVEL);
          add(Material.GOLDEN_SHOVEL);
          add(Material.IRON_SHOVEL);
          add(Material.NETHERITE_SHOVEL);
          add(Material.BOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.FIRE_RESISTANCE, 1},
                {PotionEffectType.FAST_DIGGING, 1},
                {PotionEffectType.JUMP, 2},
                {PotionEffectType.NIGHT_VISION, 1},
                {PotionEffectType.HEALTH_BOOST, 2},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.CROSSBOW,
                  Collections.singletonList(new Pair<>(Enchantment.ARROW_INFINITE, 1))),
              new AbstractMap.SimpleEntry<>(
                  Material.FLINT_AND_STEEL, new ArrayList<Pair<Enchantment, Integer>>()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  @Override
  public void reapplyEffects(Player player) {
    super.reapplyEffects(player);
    if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
      player.addPotionEffect(
          new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));
      player.addPotionEffect(
          new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
      player.addPotionEffect(
          new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }
  }

  @Override
  public Set<Material> getForbiddenItems() {
    return forbiddenItems;
  }

  @Override
  public Map<PotionEffectType, Integer> getPotionEffects(Player player) {
    return potionEffects;
  }

  @Override
  public Map<Material, List<Pair<Enchantment, Integer>>> getClassEnchantments() {
    return classEnchantments;
  }

  @Override
  public String getCode() {
    return "fire_dwarf";
  }

  @Override
  public String getName() {
    return "Fire dwarf";
  }

  @Override
  public void giveItems(Player player) {}
}
