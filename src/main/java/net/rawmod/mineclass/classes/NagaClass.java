package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
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

public class NagaClass extends MineClassImpl {

  private static final Set<Material> forbiddenItems =
      new HashSet<>() {
        {
          add(Material.DIAMOND_SWORD);
          add(Material.GOLDEN_SWORD);
          add(Material.IRON_SWORD);
          add(Material.NETHERITE_SWORD);
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
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffectsInWater =
      Stream.of(
              new Object[][] {
                {PotionEffectType.WATER_BREATHING, 1},
                {PotionEffectType.HEALTH_BOOST, 2},
                {PotionEffectType.CONDUIT_POWER, 1},
                {PotionEffectType.DOLPHINS_GRACE, 3},
                {PotionEffectType.SATURATION, 1},
                {PotionEffectType.NIGHT_VISION, 1},
                {PotionEffectType.DAMAGE_RESISTANCE, 2},
                {PotionEffectType.INCREASE_DAMAGE, 2},
                {PotionEffectType.FAST_DIGGING, 10},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<PotionEffectType, Integer> potionEffectsOnEarth =
      Stream.of(
              new Object[][] {
                {PotionEffectType.SLOW, 4},
                {PotionEffectType.SLOW_DIGGING, 1},
                {PotionEffectType.HUNGER, 10},
                {PotionEffectType.WEAKNESS, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.TRIDENT,
                  Arrays.asList(
                      new Pair<>(Enchantment.LOYALTY, 3),
                      new Pair<>(Enchantment.CHANNELING, 1),
                      new Pair<>(Enchantment.IMPALING, 5))))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  @Override
  public Set<Material> getForbiddenItems() {
    return forbiddenItems;
  }

  @Override
  public Map<PotionEffectType, Integer> getPotionEffects(Player player) {
    return player.isInWater() ? potionEffectsInWater : potionEffectsOnEarth;
  }

  @Override
  public Map<Material, List<Pair<Enchantment, Integer>>> getClassEnchantments() {
    return classEnchantments;
  }

  @Override
  public String getCode() {
    return "naga";
  }

  @Override
  public String getName() {
    return "Naga";
  }

  @Override
  public void giveItems(Player player) {
    if (!player.getInventory().contains(Material.TRIDENT)) {
      ItemStack itemStack = new ItemStack(Material.TRIDENT, 1);
      enchantItem(itemStack, player);
      player.getInventory().addItem(itemStack);
    }
  }
}
