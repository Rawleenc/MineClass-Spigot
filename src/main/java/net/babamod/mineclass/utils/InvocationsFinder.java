package net.babamod.mineclass.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvocationsFinder {

  public static Optional<Entity> findWolf(Player player, int i) {
    return player.getWorld().getEntities().stream()
        .filter(
            entity ->
                entity.hasMetadata("beastMasterWolf")
                    && entity.getMetadata("beastMasterWolf").stream()
                        .anyMatch(
                            metadataValue -> metadataValue.asString().equals(player.getName()))
                    && entity.hasMetadata("beastMasterWolfType")
                    && entity.getMetadata("beastMasterWolfType").stream()
                        .anyMatch(metadataValue -> metadataValue.asInt() == i))
        .findFirst();
  }

  public static List<Entity> findWolfs(Player player) {
    return player.getWorld().getEntities().stream()
        .filter(
            entity ->
                entity.hasMetadata("beastMasterWolf")
                    && entity.getMetadata("beastMasterWolf").stream()
                        .anyMatch(
                            metadataValue -> metadataValue.asString().equals(player.getName())))
        .collect(Collectors.toList());
  }

  public static List<Entity> findCats(Player player) {
    return player.getWorld().getEntities().stream()
        .filter(
            entity ->
                entity.hasMetadata("beastMasterCat")
                    && entity.getMetadata("beastMasterCat").stream()
                        .anyMatch(
                            metadataValue -> metadataValue.asString().equals(player.getName())))
        .collect(Collectors.toList());
  }

  public static List<Entity> findHorses(Player player) {
    return player.getWorld().getEntities().stream()
        .filter(
            entity ->
                entity.hasMetadata("beastMasterHorse")
                    && entity.getMetadata("beastMasterHorse").stream()
                    .anyMatch(
                        metadataValue -> metadataValue.asString().equals(player.getName())))
        .collect(Collectors.toList());
  }
}
