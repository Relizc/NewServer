package net.itsrelizc.health2;

public class BedHealListener implements Listener {

    private final Map<UUID, BukkitTask> repeatingTasks = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return; // Main hand only

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !isBed(clickedBlock.getType())) return;

        Player player = event.getPlayer();

        // Conditions
        if (!player.isSneaking()) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) return;

        UUID uuid = player.getUniqueId();

        // Start only if not already running
        if (!repeatingTasks.containsKey(uuid)) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    // Check again if player still meets the conditions
                    if (!player.isSneaking()
                            || player.getInventory().getItemInMainHand().getType() != Material.AIR
                            || !isBed(player.getTargetBlockExact(5) != null ? player.getTargetBlockExact(5).getType() : null)) {
                        this.cancel();
                        repeatingTasks.remove(uuid);
                        return;
                    }

                    player.sendMessage(ChatColor.YELLOW + "You're holding right-click on a bed!");
                }
            }.runTaskTimer(YourPluginInstance.getInstance(), 0L, 10L); // 10 ticks = 0.5 sec

            repeatingTasks.put(uuid, task);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (repeatingTasks.containsKey(uuid)) {
            repeatingTasks.get(uuid).cancel();
            repeatingTasks.remove(uuid);
        }
    }

    private boolean isBed(Material material) {
        return material != null && material.name().endsWith("_BED");
    }
}

