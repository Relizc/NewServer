package net.itsrelizc.gunmod.deathutils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.EnumWrappers.EntityPose;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;

public class SleepingDummyPlayer {

    /**
     * Spawns a sleeping dummy player at the specified location
     * @param name The name of the dummy player
     * @param skinName The name of the player whose skin to use
     * @param location The bed location (must be a bed block)
     * @return The NMS ServerPlayer entity
     */
    public static ServerPlayer spawnSleepingDummy(String name, String skinName, Location location) {
        // First create the dummy player
        ServerPlayer dummyPlayer = DummyPlayerSpawner.spawnDummyPlayer(name, skinName, location);
        
        // Set the player to sleeping pose
        setSleepingPose(dummyPlayer, location);
        
        return dummyPlayer;
    }

    private static void setSleepingPose(ServerPlayer dummyPlayer, Location bedLocation) {
        // Set the sleeping pose
        dummyPlayer.setPose(Pose.SLEEPING);
        
        // Set the bed position
        dummyPlayer.setSleepingPos(new BlockPos(
            bedLocation.getBlockX(),
            bedLocation.getBlockY(),
            bedLocation.getBlockZ()
        ));
        
        // Create entity metadata packet
        List<SynchedEntityData.DataValue<?>> dataValues = dummyPlayer.getEntityData().packDirty();
        ClientboundSetEntityDataPacket metadataPacket = new ClientboundSetEntityDataPacket(
            dummyPlayer.getId(),
            dataValues
        );
        
        // Send metadata update to all players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ServerPlayer nmsPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            nmsPlayer.connection.send(metadataPacket);
        }
    }

    /**
     * Makes the dummy player stop sleeping
     * @param dummyPlayer The dummy player to wake up
     */
    public static void wakeUpDummy(ServerPlayer dummyPlayer) {
        // Clear sleeping state
        dummyPlayer.setPose(Pose.SLEEPING);
        dummyPlayer.setSleepingPos(null);
        
        // Update metadata
        List<SynchedEntityData.DataValue<?>> dataValues = dummyPlayer.getEntityData().packDirty();
        ClientboundSetEntityDataPacket metadataPacket = new ClientboundSetEntityDataPacket(
            dummyPlayer.getId(),
            dataValues
        );
        
        // Send update to all players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ServerPlayer nmsPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            nmsPlayer.connection.send(metadataPacket);
        }
    }
}