package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElfClass extends MineClassImpl {

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
          add(Material.CROSSBOW);
          add(Material.TRIDENT);
          add(Material.ENDER_PEARL);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.SPEED, 2},
                {PotionEffectType.JUMP, 3},
                {PotionEffectType.LUCK, 1},
                {PotionEffectType.NIGHT_VISION, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.BOW,
                  Arrays.asList(
                      new Pair<>(Enchantment.ARROW_INFINITE, 1),
                      new Pair<>(Enchantment.ARROW_DAMAGE, 8))))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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
    return "elf";
  }

  @Override
  public String getName() {
    return "Elf";
  }

  @Override
  public void giveItems(Player player) {}
}
