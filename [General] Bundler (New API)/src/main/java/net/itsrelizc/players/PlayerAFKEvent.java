package net.itsrelizc.players;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAFKEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final boolean isAFK;

    public PlayerAFKEvent(Player player, boolean isAFK) {
        this.player = player;
        this.isAFK = isAFK;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAFK() {
        return isAFK;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
