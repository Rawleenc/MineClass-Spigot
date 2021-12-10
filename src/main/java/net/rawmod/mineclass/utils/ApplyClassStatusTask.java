package net.rawmod.mineclass.utils;

import net.rawmod.mineclass.classes.MineClassFactory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ApplyClassStatusTask extends BukkitRunnable {

  private final Player player;

  public ApplyClassStatusTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    MineClassFactory.getInstance()
        .getRightClass(player)
        .ifPresent(mineClass -> mineClass.reapplyEffects(player));
  }
}
