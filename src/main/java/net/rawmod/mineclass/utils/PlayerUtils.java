package net.rawmod.mineclass.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class PlayerUtils {
  public static void heal(Player player, double amount) {
    AttributeInstance maxHealh = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (maxHealh != null) {
      player.setHealth(Math.min(player.getHealth() + amount, maxHealh.getValue()));
    }
  }
}
