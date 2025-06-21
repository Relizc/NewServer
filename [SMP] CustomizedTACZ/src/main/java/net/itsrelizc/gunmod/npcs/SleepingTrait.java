package net.itsrelizc.gunmod.npcs;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.Bukkit;

import net.citizensnpcs.api.trait.Trait;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class SleepingTrait extends Trait {

    private static final EntityDataAccessor<Pose> POSE_ACCESSOR = new EntityDataAccessor<>(6, EntityDataSerializers.POSE);

    public SleepingTrait() {
        super("sleepingtrait");
    }

    @Override
    public void onSpawn() {
        setPose(Pose.SLEEPING); // or Pose.SLEEPING (must be on a bed)
    }

    private void setPose(Pose pose) {
        if (npc == null || !npc.isSpawned()) return;
        if (!(npc.getEntity() instanceof org.bukkit.entity.LivingEntity)) return;

        // Get NMS handle
        LivingEntity nms = ((CraftLivingEntity) npc.getEntity()).getHandle();

        // Set the pose on the server side (just in case)
        nms.setPose(pose);

        // Build and send metadata packet to all players
        SynchedEntityData data = nms.getEntityData();
        data.set(POSE_ACCESSOR, pose);
        DataValue<Pose> poseData = SynchedEntityData.DataValue.create(POSE_ACCESSOR, pose);

        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                nms.getId(),
                List.of(poseData)
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }
}


