package net.rawmod.mineclass.commands;

import net.rawmod.mineclass.classes.MineClassFactory;
import net.rawmod.mineclass.utils.InvocationsFinder;
import net.rawmod.mineclass.utils.NumberOfInvocations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandClass implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(
          "You need to use this command with one of the suggested arguments (press space then tab to see suggested arguments).");
      return false;
    }
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (MineClassFactory.getInstance().getAvailableClassCodes().contains(args[0])) {
        if (MineClassFactory.getInstance().getClassCode(player).equals("beast_master")) {
          InvocationsFinder.findWolfs(player)
              .forEach(
                  entity -> {
                    entity.remove();
                    NumberOfInvocations.getInstance().decreaseNumber(player);
                  });
          InvocationsFinder.findCats(player)
              .forEach(
                  entity -> {
                    entity.remove();
                    NumberOfInvocations.getInstance().decreaseNumber(player);
                  });
          InvocationsFinder.findHorses(player).forEach(Entity::remove);
        }
        MineClassFactory.clearAllClassEffects(player);
        MineClassFactory.getInstance().reapplyEffectsByCode(args[0], player);
        MineClassFactory.getInstance().giveItemsForClassByCode(args[0], player);
        MineClassFactory.getInstance().dropForbiddenItemsForClassByCode(args[0], player);
        MineClassFactory.getInstance().setClassCode(player, args[0]);
        if (!player.hasPotionEffect(PotionEffectType.SATURATION)) {
          player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 9));
        }
        return true;
      }
      if (args[0].equals("steve")) {
        MineClassFactory.getInstance().setClassCode(player, "steve");
        MineClassFactory.clearAllClassEffects(player);
        return true;
      }
      if (args[0].equals("whoami")) {
        String classCode = MineClassFactory.getInstance().getClassCode(player);
        if (classCode != null) {
          player.sendMessage(String.format("You are a %s.", classCode));
        } else {
          player.sendMessage("You are a steve.");
        }
        return true;
      }
    }
    return false;
  }
}
