package net.babamod.mineclass.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class MineClassImpl implements MineClass {

  @Override
  public boolean is(Player player) {
    return player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .collect(Collectors.toList())
        .containsAll(getPotionEffects().keySet());
  }

  @Override
  public void reapplyEffects(Player player) {
    getPotionEffects()
        .forEach(
            (key, value) -> {
              if (player.hasPotionEffect(key)) {
                player.removePotionEffect(key);
              }
              player.addPotionEffect(
                  new PotionEffect(key, Integer.MAX_VALUE, value - 1, false, false));
            });
  }

  @Override
  public boolean isItemForbidden(Material type) {
    return getForbiddenItems().contains(type);
  }

  @Override
  public boolean isItemEnchantable(Material type) {
    return getClassEnchantments().containsKey(type);
  }

  @Override
  public void enchantItem(ItemStack itemStack) {
    getClassEnchantments()
        .getOrDefault(itemStack.getType(), new ArrayList<>())
        .forEach(
            enchantmentIntegerPair ->
                itemStack.addUnsafeEnchantment(
                    enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond()));
    MineClassFactory.setUnbreakableAndSoulbound(itemStack);
  }
}
