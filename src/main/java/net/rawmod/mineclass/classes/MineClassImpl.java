package net.rawmod.mineclass.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public abstract class MineClassImpl implements MineClass {

  @Override
  public void reapplyEffects(Player player) {
    MineClassFactory.clearAllClassEffects(player);
    getPotionEffects(player)
        .forEach(
            (key, value) -> {
              if (player.hasPotionEffect(key)) {
                player.removePotionEffect(key);
              }
              player.addPotionEffect(
                  new PotionEffect(key, Integer.MAX_VALUE, value - 1, false, false));
            });
    MineClassFactory.getInstance().setClassCode(player, getCode());
  }

  @Override
  public boolean isItemForbidden(Material type) {
    return getForbiddenItems().contains(type);
  }

  @Override
  public void enchantItem(ItemStack itemStack, Player player) {
    if (getClassEnchantments().containsKey(itemStack.getType())) {
      getClassEnchantments()
          .getOrDefault(itemStack.getType(), new ArrayList<>())
          .forEach(
              enchantmentIntegerPair ->
                  itemStack.addUnsafeEnchantment(
                      enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond()));
      MineClassFactory.setUnbreakableAndSoulbound(itemStack, player);
    }
  }

  @Override
  public void disenchantItem(ItemStack itemStack, Player player) {
    if (getClassEnchantments().containsKey(itemStack.getType())) {
      getClassEnchantments()
          .getOrDefault(itemStack.getType(), new ArrayList<>())
          .forEach(
              enchantmentIntegerPair ->
                  itemStack.removeEnchantment(enchantmentIntegerPair.getFirst()));
      MineClassFactory.removeUnbreakableAndSoulbound(itemStack, player);
    }
  }

  @Override
  public void dropForbiddenItems(Player player) {
    for (ItemStack content : player.getInventory().getContents()) {
      if (content != null && getForbiddenItems().contains(content.getType())) {
        player.getInventory().remove(content);
        player.getWorld().dropItemNaturally(player.getLocation(), content);
      }
    }
  }
}
