package net.rawmod.mineclass.listeners;

import net.rawmod.mineclass.Mineclass;
import net.rawmod.mineclass.classes.MineClass;
import net.rawmod.mineclass.classes.MineClassFactory;
import net.rawmod.mineclass.utils.*;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MineClassListeners implements Listener {

  private final Mineclass plugin;

  public MineClassListeners(Mineclass plugin) {
    this.plugin = plugin;
    this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void on(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent()) {
      mineClass.get().reapplyEffects(player);
      player.sendMessage(String.format("Reminder : You are a %s.", mineClass.get().getCode()));
    } else {
      player.sendMessage(
          "Hello ! The amazing MineClass mod is available on this server ! You can pick a class with the /class command.");
    }
  }

  @EventHandler
  public void on(PlayerItemConsumeEvent event) {
    if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
      new ApplyClassStatusTask(event.getPlayer()).runTaskLater(this.plugin, 10);
    }
  }

  @EventHandler
  public void on(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
      if (mineClass.isPresent()) {
        if (mineClass.get().isItemForbidden(event.getItem().getItemStack().getType())) {
          event.setCancelled(true);
        }
        mineClass.get().enchantItem(event.getItem().getItemStack());
      }
    }
  }

  @EventHandler
  public void on(PlayerDeathEvent event) {
    List<ItemStack> itemStackList =
        event.getDrops().stream()
            .filter(MineClassFactory::isSoulBound)
            .collect(Collectors.toList());
    event.getDrops().removeAll(itemStackList);
    ClassItemPossessed.getInstance().addItems(event.getEntity().getName(), itemStackList);
  }

  @EventHandler
  public void on(PlayerRespawnEvent event) {
    new ApplyClassStatusTask(event.getPlayer()).runTaskLater(this.plugin, 10);
    ClassItemPossessed.getInstance()
        .getItems(event.getPlayer().getName())
        .forEach(itemStack -> event.getPlayer().getInventory().addItem(itemStack));
    ClassItemPossessed.getInstance().clearItems(event.getPlayer().getName());
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      if (isForbiddenItem(event)) {
        event.setCancelled(true);
        return;
      }
      enchantItem(event);
    }
  }

  private boolean isForbiddenItem(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent()) {
      if (event.getCurrentItem() != null
          && mineClass.get().isItemForbidden(event.getCurrentItem().getType())) {
        return true;
      }
      return event.getCursor() != null
          && mineClass.get().isItemForbidden(event.getCursor().getType());
    }
    return false;
  }

  private void enchantItem(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent()) {
      if (event.getCurrentItem() != null && !MineClassFactory.isSoulBound(event.getCurrentItem())) {
        mineClass.get().enchantItem(event.getCurrentItem());
      }
      if (event.getCursor() != null && !MineClassFactory.isSoulBound(event.getCursor())) {
        mineClass.get().enchantItem(event.getCursor());
      }
    }
  }

  @EventHandler
  public void on(BlockDropItemEvent event) {
    Player player = event.getPlayer();
    if (MineClassFactory.getInstance().getClassCode(player).equals("fire_dwarf")) {
      event
          .getItems()
          .forEach(
              item ->
                  SmeltingEngine.getInstance()
                      .smelt(player, event.getBlock().getLocation(), item.getItemStack())
                      .ifPresent(item::setItemStack));
    }
  }

  @EventHandler
  public void on(EntityShootBowEvent event) {
    if (event.getBow() != null && event.getBow().getType().equals(Material.CROSSBOW)) {
      if (event.getEntity() instanceof Player) {
        Player player = (Player) event.getEntity();
        if (event.getProjectile() instanceof AbstractArrow
            && event.getBow().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
          player.getInventory().addItem(new ItemStack(Material.ARROW));
          ((AbstractArrow) event.getProjectile())
              .setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
        if (MineClassFactory.getInstance().getClassCode(player).equals("fire_dwarf")) {
          event.getProjectile().setFireTicks(10000);
        }
      }
    }
  }

  @EventHandler
  public void on(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)
          && (MineClassFactory.getInstance().getClassCode(player).equals("elf")
              || MineClassFactory.getInstance().getClassCode(player).equals("ender_elf"))) {
        event.setCancelled(true);
      }
    } else {
      if (event.getEntity().hasMetadata("beastMasterHorse")
          && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void on(FoodLevelChangeEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      if (MineClassFactory.getInstance().getClassCode(player).equals("elf")) {
        int difference = player.getFoodLevel() - event.getFoodLevel();
        if (difference > 0) {
          event.setCancelled(true);
        }
      }
      if (MineClassFactory.getInstance().getClassCode(player).equals("ender_elf")) {
        int difference = player.getFoodLevel() - event.getFoodLevel();
        if (difference > 0) {
          event.setFoodLevel(player.getFoodLevel() - (difference * 2));
        }
      }
    }
  }

  @EventHandler
  public void on(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player) {
      Player player = (Player) event.getDamager();
      if (MineClassFactory.getInstance().getClassCode(player).equals("ender_elf")
          && player.getInventory().getItemInMainHand().getType().equals(Material.ENDER_PEARL)) {
        PlayerHitCounter.getInstance().increaseHitCount(player);
        if (player.getAttackCooldown() == 1) {
          // Vampirisme
          if (player.getHealth() <= 19) {
            player.setHealth(player.getHealth() + 1);
          }
        }
        if (PlayerHitCounter.getInstance().getHitCounter(player) == 15) {
          // Absorption
          PlayerHitCounter.getInstance().resetHitCounter(player);
          PotionEffect absorption =
              new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 0, false, false);
          player.removePotionEffect(PotionEffectType.ABSORPTION);
          player.addPotionEffect(absorption);
        }
        // Damage
        event.setDamage(event.getDamage() * (player.getAttackCooldown() * 10));
      }
    } else if (event.getDamager().hasMetadata("beastMasterWolfType")) {
      event.getDamager().getMetadata("beastMasterWolfType").stream()
          .map(MetadataValue::asInt)
          .findFirst()
          .ifPresent(
              integer -> {
                if (integer == 2) {
                  event.getEntity().setFireTicks(200);
                }
              });
    }
  }

  @EventHandler
  public void on(CreatureSpawnEvent event) {
    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.ENDER_PEARL)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void on(ProjectileHitEvent event) {
    if (event.getEntity().getShooter() instanceof Player) {
      Player player = (Player) event.getEntity().getShooter();
      if (player.getGameMode().equals(GameMode.CREATIVE)) {
        return;
      }
      if (MineClassFactory.getInstance().getClassCode(player).equals("ender_elf")
          && event.getEntity() instanceof EnderPearl) {
        ItemStack itemStack = new ItemStack(Material.ENDER_PEARL, 1);
        MineClassFactory.setUnbreakableAndSoulbound(itemStack);
        player.getInventory().addItem(itemStack);
      }
    }
  }

  @EventHandler
  public void on(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    MineClassFactory.getInstance()
        .getRightClass(player)
        .ifPresent(mineClass -> mineClass.reapplyEffects(player));
  }

  @EventHandler
  public void on(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (player.isSneaking()
        && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
        && event.useInteractedBlock().equals(Event.Result.ALLOW)
        && MineClassFactory.getInstance().getClassCode(player).equals("beast_master")
        && event.getClickedBlock() != null
        && event.getItem() != null) {
      event.setCancelled(true);
      ItemStack itemStack = event.getItem();
      switch (itemStack.getType()) {
        case SADDLE:
          invokeHorse(event, player);
          break;
        case BONE:
          try {
            invokeWolf(event, player, itemStack);
          } catch (IllegalStateException e) {
            player.sendMessage(e.getMessage());
          }
          break;
        case SALMON:
          invokeCat(event, player, itemStack);
          break;
        default:
          break;
      }
    }
    if (player.isSneaking()
        && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
            || event.getAction().equals(Action.RIGHT_CLICK_AIR))
        && MineClassFactory.getInstance().getClassCode(player).equals("ender_elf")
        && event.getItem() != null
        && event.getItem().getType().equals(Material.ENDER_PEARL)) {
      player.openInventory(player.getEnderChest());
      event.setCancelled(true);
    }
  }

  private void invokeCat(PlayerInteractEvent event, Player player, ItemStack itemStack) {
    Objects.requireNonNull(event.getClickedBlock());
    if (NumberOfInvocations.getInstance().increaseNumber(player)) {
      player.sendMessage("Invocation limit reached.");
      return;
    }
    Cat cat =
        player
            .getWorld()
            .spawn(event.getClickedBlock().getRelative(BlockFace.UP).getLocation(), Cat.class);
    cat.setTamed(true);
    cat.setOwner(player);
    cat.setMetadata("beastMasterCat", new FixedMetadataValue(this.plugin, player.getName()));
    cat.setCustomName(String.format("%s's Beast Master Cat", player.getName()));
    cat.setCustomNameVisible(true);
    cat.setCollarColor(DyeColor.BLACK);
    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
      itemStack.setAmount(itemStack.getAmount() - 1);
    }
  }

  private void invokeWolf(PlayerInteractEvent event, Player player, ItemStack itemStack) {
    Objects.requireNonNull(event.getClickedBlock());
    ItemStack offhandItem = player.getInventory().getItemInOffHand();
    switch (offhandItem.getType()) {
      case BEEF:
        if (offhandItem.getAmount() >= 8) {
          Optional<Entity> wolfOptional = InvocationsFinder.findWolf(player, 0);
          if (wolfOptional.isPresent()) {
            wolfOptional.get().remove();
            NumberOfInvocations.getInstance().decreaseNumber(player);
            Wolf wolf = spawnBasicWolf(event, player);
            updateToBMWolf(player, wolf);
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
              offhandItem.setAmount(offhandItem.getAmount() - 8);
            }
          } else {
            spawnBasicWolf(event, player);
          }
        }
        break;
      case BLAZE_ROD:
        if (offhandItem.getAmount() >= 8) {
          Optional<Entity> wolfOptional = InvocationsFinder.findWolf(player, 1);
          if (wolfOptional.isPresent()) {
            wolfOptional.get().remove();
            NumberOfInvocations.getInstance().decreaseNumber(player);
            Wolf wolf = spawnBasicWolf(event, player);
            updateToHellhound(player, wolf);
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
              offhandItem.setAmount(offhandItem.getAmount() - 8);
            }
          } else {
            spawnBasicWolf(event, player);
          }
        }
        break;
      default:
        spawnBasicWolf(event, player);
        break;
    }

    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
      itemStack.setAmount(itemStack.getAmount() - 1);
    }
  }

  private void updateToBMWolf(Player player, Wolf wolf) {
    wolf.setMetadata("beastMasterWolfType", new FixedMetadataValue(this.plugin, 1));
    wolf.setCustomName(String.format("%s's Beast Master Wolf", player.getName()));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
  }

  private void updateToHellhound(Player player, Wolf wolf) {
    wolf.setMetadata("beastMasterWolfType", new FixedMetadataValue(this.plugin, 2));
    wolf.setCustomName(String.format("%s's Beast Master Hellhound", player.getName()));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4));
    wolf.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
  }

  private Wolf spawnBasicWolf(PlayerInteractEvent event, Player player) {
    Objects.requireNonNull(event.getClickedBlock());
    if (NumberOfInvocations.getInstance().increaseNumber(player)) {
      throw new IllegalStateException("Invocation limit reached.");
    }
    Wolf wolf =
        player
            .getWorld()
            .spawn(event.getClickedBlock().getRelative(BlockFace.UP).getLocation(), Wolf.class);
    wolf.setTamed(true);
    wolf.setOwner(player);
    wolf.setMetadata("beastMasterWolf", new FixedMetadataValue(this.plugin, player.getName()));
    wolf.setMetadata("beastMasterWolfType", new FixedMetadataValue(this.plugin, 0));
    wolf.setCustomName(String.format("%s's Wolf", player.getName()));
    wolf.setCustomNameVisible(true);
    wolf.setCollarColor(DyeColor.BLACK);
    return wolf;
  }

  private void invokeHorse(PlayerInteractEvent event, Player player) {
    Objects.requireNonNull(event.getClickedBlock());
    player.getWorld().getEntities().stream()
        .filter(
            entity ->
                entity.getCustomName() != null
                    && entity.hasMetadata("beastMasterHorse")
                    && entity.getMetadata("beastMasterHorse").stream()
                        .anyMatch(
                            metadataValue -> metadataValue.asString().equals(player.getName())))
        .findFirst()
        .ifPresent(Entity::remove);
    Horse horse =
        player
            .getWorld()
            .spawn(event.getClickedBlock().getRelative(BlockFace.UP).getLocation(), Horse.class);
    horse.setTamed(true);
    horse.setOwner(player);
    horse.setMetadata("beastMasterHorse", new FixedMetadataValue(this.plugin, player.getName()));
    horse.setCustomName(String.format("%s's Beast Master Horse", player.getName()));
    horse.setCustomNameVisible(true);
    horse.setColor(Horse.Color.DARK_BROWN);
    horse.setJumpStrength(1.1);
    horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
    horse.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3));
    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
  }

  @EventHandler
  public void on(EntityDeathEvent event) {
    if (event.getEntity().hasMetadata("beastMasterHorse")) {
      event.getDrops().clear();
    }
    if (event.getEntity().hasMetadata("beastMasterWolf")) {
      event.getEntity().getMetadata("beastMasterWolf").stream()
          .map(MetadataValue::asString)
          .findFirst()
          .ifPresent(s -> NumberOfInvocations.getInstance().decreaseNumber(s));
    }
    if (event.getEntity().hasMetadata("beastMasterCat")) {
      event.getEntity().getMetadata("beastMasterCat").stream()
          .map(MetadataValue::asString)
          .findFirst()
          .ifPresent(s -> NumberOfInvocations.getInstance().decreaseNumber(s));
    }
  }
}
