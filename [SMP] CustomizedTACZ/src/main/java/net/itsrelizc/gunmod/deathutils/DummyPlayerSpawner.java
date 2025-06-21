package net.itsrelizc.gunmod.deathutils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class DummyPlayerSpawner {

    /**
     * Spawns a dummy player at the specified location
     * @param name The name of the dummy player
     * @param skinName The name of the player whose skin to use (null for default Steve/Alex)
     * @param location The location to spawn the dummy
     * @return The NMS ServerPlayer entity
     */
    public static ServerPlayer spawnDummyPlayer(String name, String skinName, Location location) {
        // Create a GameProfile for the dummy player
        GameProfile gameProfile = new GameProfile(
            UUID.randomUUID(), 
            name.length() > 16 ? name.substring(0, 16) : name
        );

        // Get the NMS world
        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        net.minecraft.server.level.ServerLevel nmsWorld = craftWorld.getHandle();

        // Create the player entity
        ServerPlayer dummyPlayer = new ServerPlayer(
            ((CraftServer) Bukkit.getServer()).getServer(),
            nmsWorld,
            gameProfile
        );

        // Set position and rotation
        dummyPlayer.setPos(location.getX(), location.getY(), location.getZ());
        dummyPlayer.setXRot(location.getYaw());
        dummyPlayer.setYRot(location.getPitch());

        // Make the player look like another player (skin)
        if (skinName != null) {
            Player skinPlayer = Bukkit.getPlayer(skinName);
            if (skinPlayer != null) {
                dummyPlayer.getGameProfile().getProperties().putAll(
                    ((CraftPlayer) skinPlayer).getHandle().getGameProfile().getProperties()
                );
            }
        }

        // Make the player invisible to server (but visible to clients)
        dummyPlayer.valid = false;

        // Add to player list (optional)
        ((CraftServer) Bukkit.getServer()).getHandle().players.add(dummyPlayer);

        // Send packets to all players to show this dummy
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ServerPlayer nmsPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            ServerGamePacketListenerImpl connection = nmsPlayer.connection;
            
            // Send player info packet (adds to tablist)
            connection.send(new net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket(
                net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                dummyPlayer
            ));
            
            // Send spawn packet
            connection.send(new net.minecraft.network.protocol.game.ClientboundAddPlayerPacket(dummyPlayer));
        }

        return dummyPlayer;
    }

    /**
     * Removes a dummy player from the world
     * @param dummyPlayer The NMS ServerPlayer to remove
     */
    public static void removeDummyPlayer(ServerPlayer dummyPlayer) {
        // Send remove packets to all players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ServerPlayer nmsPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            ServerGamePacketListenerImpl connection = nmsPlayer.connection;
            
            // Remove from tablist
            connection.send(new net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket(
                List.of(dummyPlayer.getUUID())
            ));
            
            // Send destroy entity packet
            connection.send(new net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket(dummyPlayer.getId()));
        }

        // Remove from player list
        ((CraftServer) Bukkit.getServer()).getHandle().players.remove(dummyPlayer);
    }
}
