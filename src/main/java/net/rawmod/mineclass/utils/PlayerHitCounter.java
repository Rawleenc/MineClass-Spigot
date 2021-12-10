package net.rawmod.mineclass.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerHitCounter {
  /** Instance unique pré-initialisée */
  private static PlayerHitCounter INSTANCE;

  private final HashMap<String, Integer> byPlayerCounter;

  /** Constructeur privé */
  private PlayerHitCounter() {
    byPlayerCounter = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized PlayerHitCounter getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PlayerHitCounter();
    }

    return INSTANCE;
  }

  public synchronized void increaseHitCount(Player player) {
    Integer counter = byPlayerCounter.getOrDefault(player.getName(), 0);
    byPlayerCounter.put(player.getName(), counter + 1);
  }

  public synchronized void resetHitCounter(Player player) {
    byPlayerCounter.put(player.getName(), 0);
  }

  public synchronized Integer getHitCounter(Player player) {
    return byPlayerCounter.getOrDefault(player.getName(), 0);
  }
}
