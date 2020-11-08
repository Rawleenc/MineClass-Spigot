package net.babamod.mineclass.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class NumberOfInvocations {
  /** Instance unique pré-initialisée */
  private static NumberOfInvocations INSTANCE;

  private final HashMap<String, Integer> numberOfInvocations;

  /** Constructeur privé */
  private NumberOfInvocations() {
    numberOfInvocations = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized NumberOfInvocations getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NumberOfInvocations();
    }

    return INSTANCE;
  }

  public boolean increaseNumber(Player player) {
    Integer integer = numberOfInvocations.getOrDefault(player.getName(), 0);
    if (integer == 8) {
      return true;
    }
    numberOfInvocations.put(player.getName(), integer + 1);
    return false;
  }

  public void decreaseNumber(Player player) {
    decreaseNumber(player.getName());
  }

  public void decreaseNumber(String playerName) {
    Integer integer = numberOfInvocations.getOrDefault(playerName, 0);
    if (integer == 0) {
      return;
    }
    numberOfInvocations.put(playerName, integer - 1);
  }
}
