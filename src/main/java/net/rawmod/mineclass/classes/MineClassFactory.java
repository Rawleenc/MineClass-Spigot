package net.rawmod.mineclass.classes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class MineClassFactory {
  /** Instance unique pré-initialisée */
  private static MineClassFactory INSTANCE;

  private final Map<String, MineClass> availableClasses;

  /** Constructeur privé */
  private MineClassFactory() {
    availableClasses = new HashMap<>();
    availableClasses.put("dwarf", new DwarfClass());
    availableClasses.put("elf", new ElfClass());
    availableClasses.put("fire_dwarf", new FireDwarfClass());
    availableClasses.put("ender_elf", new EnderElfClass());
    availableClasses.put("beast_master", new BeastMasterClass());
    availableClasses.put("naga", new NagaClass());
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized MineClassFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MineClassFactory();
    }

    return INSTANCE;
  }

  public static boolean isSimpleSoulBound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null) {
      return itemStack.getItemMeta().getLore().contains("Soulbound");
    }
    return false;
  }

  public static boolean isSoulBound(ItemStack itemStack, Player player) {
    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null) {
      Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
      return itemStack.getItemMeta().getLore().contains("Soulbound")
          && itemStack.getItemMeta().getLore().contains(player.getName())
          && mineClass.isPresent()
          && itemStack.getItemMeta().getLore().contains(mineClass.get().getName());
    }
    return false;
  }

  public static void clearAllClassEffects(Player player) {
    for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
      if (activePotionEffect.getDuration() > 32766) {
        player.removePotionEffect(activePotionEffect.getType());
      }
    }
  }

  public static void clearClassItem(Player player, ItemStack itemStack) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta != null) {
      List<String> loreList = itemMeta.getLore();
      if (loreList != null && loreList.contains("Soulbound")) {
        String mineClassName = loreList.get(2);
        System.out.println(mineClassName);
        Optional<MineClass> optionalMineClass =
            MineClassFactory.getInstance().findClassByName(mineClassName);
        optionalMineClass.ifPresent(System.out::println);
        optionalMineClass.ifPresent(mineClass -> mineClass.disenchantItem(itemStack, player));
      }
    }
  }

  public static void setUnbreakableAndSoulbound(ItemStack itemStack, Player player) {
    if (itemStack.getItemMeta() != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setUnbreakable(true);
      List<String> loreList = new ArrayList<>();
      loreList.add("Soulbound");
      loreList.add(player.getName());
      Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
      mineClass.ifPresent(aClass -> loreList.add(aClass.getName()));
      itemMeta.setLore(loreList);
      itemStack.setItemMeta(itemMeta);
    }
  }

  public static void removeUnbreakableAndSoulbound(ItemStack itemStack, Player player) {
    if (itemStack.getItemMeta() != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setUnbreakable(false);
      List<String> loreList = new ArrayList<>();
      itemMeta.setLore(loreList);
      itemStack.setItemMeta(itemMeta);
    }
  }

  public synchronized Set<String> getAvailableClassCodes() {
    return availableClasses.keySet();
  }

  public synchronized String getClassCode(Player player) {
    return player.getScoreboardTags().stream()
        .filter(availableClasses::containsKey)
        .findFirst()
        .orElse("steve");
  }

  public synchronized void setClassCode(Player player, String code) {
    player.getScoreboardTags().removeAll(availableClasses.keySet());
    player.addScoreboardTag(code);
  }

  public synchronized Optional<MineClass> getRightClass(Player player) {
    for (Map.Entry<String, MineClass> stringMineClassEntry : availableClasses.entrySet()) {
      if (getClassCode(player) != null
          && getClassCode(player).equals(stringMineClassEntry.getKey())) {
        return Optional.of(stringMineClassEntry.getValue());
      }
    }
    return Optional.empty();
  }

  public synchronized Optional<MineClass> findClassByName(String name) {
    for (Map.Entry<String, MineClass> stringMineClassEntry : availableClasses.entrySet()) {
      if (name != null && name.equals(stringMineClassEntry.getValue().getName())) {
        return Optional.of(stringMineClassEntry.getValue());
      }
    }
    return Optional.empty();
  }

  public void reapplyEffectsByCode(String code, Player player) {
    availableClasses.get(code).reapplyEffects(player);
  }

  public void giveItemsForClassByCode(String code, Player player) {
    availableClasses.get(code).giveItems(player);
  }

  public void dropForbiddenItemsForClassByCode(String code, Player player) {
    availableClasses.get(code).dropForbiddenItems(player);
  }
}
