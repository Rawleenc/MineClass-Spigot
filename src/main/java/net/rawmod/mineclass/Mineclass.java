package net.rawmod.mineclass;

import net.rawmod.mineclass.classes.MineClassFactory;
import net.rawmod.mineclass.commands.CommandClass;
import net.rawmod.mineclass.listeners.MineClassListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Mineclass extends JavaPlugin {

  @Override
  public void onEnable() {
    new MineClassListeners(this);
    PluginCommand pluginCommand = this.getCommand("class");
    if (pluginCommand != null) {
      List<String> arguments =
          new ArrayList<>(MineClassFactory.getInstance().getAvailableClassCodes());
      arguments.add("steve");
      arguments.add("whoami");
      pluginCommand.setTabCompleter((sender, command, alias, args) -> arguments);
      pluginCommand.setExecutor(new CommandClass());
    }
    Bukkit.getOnlinePlayers()
        .forEach(
            player ->
                MineClassFactory.getInstance()
                    .getRightClass(player)
                    .ifPresent(aClass -> aClass.reapplyEffects(player)));
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
