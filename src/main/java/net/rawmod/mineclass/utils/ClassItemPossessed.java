package net.rawmod.mineclass.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassItemPossessed {
  /** Instance unique pré-initialisée */
  private static ClassItemPossessed INSTANCE;

  private final HashMap<String, List<ItemStack>> itemsPossessed;

  /** Constructeur privé */
  private ClassItemPossessed() {
    itemsPossessed = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized ClassItemPossessed getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ClassItemPossessed();
    }

    return INSTANCE;
  }

  public synchronized void addItems(String playerName, List<ItemStack> itemStacks) {
    List<ItemStack> itemStackList = itemsPossessed.getOrDefault(playerName, new ArrayList<>());
    itemStackList.addAll(itemStacks);
    itemsPossessed.put(playerName, itemStackList);
  }

  public synchronized void clearItems(String playerName) {
    itemsPossessed.put(playerName, new ArrayList<>());
  }

  public synchronized List<ItemStack> getItems(String playerName) {
    return itemsPossessed.getOrDefault(playerName, new ArrayList<>());
  }
}
