package net.rawmod.mineclass.classes;

import net.rawmod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MineClass {

  Set<Material> getForbiddenItems();

  Map<PotionEffectType, Integer> getPotionEffects(Player player);

  Map<Material, List<Pair<Enchantment, Integer>>> getClassEnchantments();

  void reapplyEffects(Player player);

  boolean isItemForbidden(Material type);

  void enchantItem(ItemStack itemStack, Player player);

  void disenchantItem(ItemStack itemStack, Player player);

  void giveItems(Player player);

  String getCode();

  String getName();

  void dropForbiddenItems(Player player);
}
