package net.itsrelizc.string;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Rotation;

/* 
 * 
 * net.minecraft.network.protocol.game.ClientboundAddEntityPacket -> us:
    double MAGICAL_QUANTIZATION -> a
    double LIMIT -> b
    int id -> c
    java.util.UUID uuid -> d
    net.minecraft.world.entity.EntityType type -> e
    double x -> f
    double y -> g
    double z -> h
    int xa -> i
    int ya -> j
    int za -> k
    byte xRot -> l
    byte yRot -> m
    byte yHeadRot -> n
    int data -> o
    35:36:void <init>(net.minecraft.world.entity.Entity) -> <init>
    39:40:void <init>(net.minecraft.world.entity.Entity,int) -> <init>
    43:44:void <init>(net.minecraft.world.entity.Entity,int,net.minecraft.core.BlockPos) -> <init>
    46:61:void <init>(int,java.util.UUID,double,double,double,float,float,net.minecraft.world.entity.EntityType,int,net.minecraft.world.phys.Vec3,double) -> <init>
    63:78:void <init>(net.minecraft.network.FriendlyByteBuf) -> <init>
    82:95:void write(net.minecraft.network.FriendlyByteBuf) -> a
    99:100:void handle(net.minecraft.network.protocol.game.ClientGamePacketListener) -> a
    103:103:int getId() -> a
    107:107:java.util.UUID getUUID() -> c
    111:111:net.minecraft.world.entity.EntityType getType() -> d
    115:115:double getX() -> e
    119:119:double getY() -> f
    123:123:double getZ() -> g
    127:127:double getXa() -> h
    131:131:double getYa() -> i
    135:135:double getZa() -> j
    139:139:float getXRot() -> k
    143:143:float getYRot() -> l
    147:147:float getYHeadRot() -> m
    151:151:int getData() -> n
    15:15:void handle(net.minecraft.network.PacketListener) -> a
 * 
 * */

public class Hologram {
	
	private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	private UUID entityUUID;
	private int entityId;
	private Player player;
	
	public Hologram(Player player, Location location, String name) {
		
		int entity = (int) (Math.random() * Integer.MAX_VALUE);
		UUID uuid = UUID.randomUUID();
		
		PacketContainer spawn = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
		
		CompoundTag a = new CompoundTag();
		
		
		spawn.getIntegers().writeDefaults();
		spawn.getDoubles().writeDefaults();
		spawn.getBytes().writeDefaults();
		
		spawn.getIntegers().write(0, entity); // entityId
		spawn.getUUIDs().write(0, uuid); // uuid
		spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
		spawn.getDoubles().write(0, location.getX()).write(1, location.getY()).write(2, location.getZ());
		
		protocolManager.sendServerPacket(player, spawn);
		
		SynchedEntityData watcher = new SynchedEntityData(null);
		EntityDataAccessor<Byte> FLAGS = new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);
		EntityDataAccessor<Integer> AIR_TICKS = new EntityDataAccessor<>(1, EntityDataSerializers.INT);
		EntityDataAccessor<Component> name1 = new EntityDataAccessor<>(2, EntityDataSerializers.COMPONENT);
		EntityDataAccessor<Boolean> NAME_VISIBLE = new EntityDataAccessor<>(3, EntityDataSerializers.BOOLEAN);
		EntityDataAccessor<Boolean> IS_SILENT = new EntityDataAccessor<>(4, EntityDataSerializers.BOOLEAN);
		EntityDataAccessor<Boolean> NO_GRAVITY = new EntityDataAccessor<>(5, EntityDataSerializers.BOOLEAN);
		EntityDataAccessor<Pose> POSE = new EntityDataAccessor<>(6, EntityDataSerializers.POSE);
		EntityDataAccessor<Integer> POWDERED_SNOW_TICK = new EntityDataAccessor<>(7, EntityDataSerializers.INT);
		
		// ARMOR STAND ONLY
		EntityDataAccessor<Byte> ARMOR_STAND_OPTIONS = new EntityDataAccessor<>(15, EntityDataSerializers.BYTE);
		EntityDataAccessor<Rotations> HEAD_ROTATION = new EntityDataAccessor<>(16, EntityDataSerializers.ROTATIONS);
		EntityDataAccessor<Rotations> BODY_ROTATION = new EntityDataAccessor<>(17, EntityDataSerializers.ROTATIONS);
		EntityDataAccessor<Rotations> LARM_ROTATION = new EntityDataAccessor<>(18, EntityDataSerializers.ROTATIONS);
		EntityDataAccessor<Rotations> RARM_ROTATION = new EntityDataAccessor<>(19, EntityDataSerializers.ROTATIONS);
		EntityDataAccessor<Rotations> LLEG_ROTATION = new EntityDataAccessor<>(20, EntityDataSerializers.ROTATIONS);
		EntityDataAccessor<Rotations> RLEG_ROTATION = new EntityDataAccessor<>(21, EntityDataSerializers.ROTATIONS);
		
		watcher.define(FLAGS, (byte) 0x20); // 0b01000000 invisible
		watcher.define(name1, Component.literal("Test"));
		watcher.define(NAME_VISIBLE, true);
		watcher.define(AIR_TICKS, 300);
		
		watcher.define(IS_SILENT, true);
		watcher.define(NO_GRAVITY, true);
		watcher.define(POSE, Pose.STANDING);
		watcher.define(POWDERED_SNOW_TICK, 0);
		
		watcher.define(ARMOR_STAND_OPTIONS, (byte) 0);
		watcher.define(HEAD_ROTATION, new Rotations(0, 0, 0));
		watcher.define(BODY_ROTATION, new Rotations(0, 0, 0));
		watcher.define(LARM_ROTATION, new Rotations(-10, 0, -10));
		watcher.define(RARM_ROTATION, new Rotations(-15, 0, 10));
		watcher.define(LLEG_ROTATION, new Rotations(-1, 0, -1));
		watcher.define(RLEG_ROTATION, new Rotations(1, 0, 1));
		
		ClientboundSetEntityDataPacket pack = new ClientboundSetEntityDataPacket(entity, watcher.packDirty());
		CraftPlayer p = (CraftPlayer) player;
		p.getHandle().connection.send(pack);
		
		
	}
	
	public void remove() {
	    PacketContainer destroyPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
	    destroyPacket.getIntegerArrays().write(0, new int[]{entityId});

	    try {
	        protocolManager.sendServerPacket(player, destroyPacket);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void teleport(double x, double y, double z) {
	    PacketContainer teleportPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

	    teleportPacket.getIntegers().write(0, entityId); // Entity ID
	    teleportPacket.getDoubles()
	            .write(0, x)
	            .write(1, y)
	            .write(2, z);

	    teleportPacket.getBytes()
	            .write(0, (byte) 0) // Yaw (not needed for ArmorStand holograms)
	            .write(1, (byte) 0); // Pitch

	    try {
	        protocolManager.sendServerPacket(player, teleportPacket);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static Hologram spawn(Player player, Location loc, String name) {
		return new Hologram(player, loc, name);
	}

}
