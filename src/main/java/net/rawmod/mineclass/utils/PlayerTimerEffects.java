package net.rawmod.mineclass.utils;

import net.rawmod.mineclass.classes.MineClass;
import net.rawmod.mineclass.classes.MineClassFactory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PlayerTimerEffects extends BukkitRunnable {

  private final Player player;
  private boolean inWater;

  public PlayerTimerEffects(Player player) {
    this.player = player;
    this.inWater = player.isInWater();
  }

  @Override
  public void run() {
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent() && mineClass.get().getCode().equals("naga")) {
      if (!player.isInWater()) {
        player.damage(1);

      }
      if (player.isInWater() != inWater) {
        inWater = player.isInWater();
        mineClass.get().reapplyEffects(player);
      }
    }
    if (mineClass.isPresent() && mineClass.get().getCode().equals("fire_dwarf")) {
      if (player.getFireTicks() > 0) {
        PlayerUtils.heal(player, 2);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1));
      }
      if (player.isInWater()) {
        player.damage(1);
      }
    }
  }
}
