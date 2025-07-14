package net.itsrelizc.players;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomPlayerTeleportEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Location from;
    private final Location to;
    private boolean cancelled;

    public CustomPlayerTeleportEvent(Player player, Location from, Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public static void teleport(Player player, Location loc) {
    	CustomPlayerTeleportEvent event = new CustomPlayerTeleportEvent(player, player.getLocation(), loc);
    	Bukkit.getPluginManager().callEvent(event);
    }
}
