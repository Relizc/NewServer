package net.itsrelizc.health2.ballistics;

import org.bukkit.Material;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.EnumSet;

public class BounceSurfaces {

    // Very hard surfaces: stone, metal, etc.
    public static final Set<Material> HARD_SURFACES = EnumSet.of(
        Material.STONE,
        Material.GRANITE,
        Material.POLISHED_GRANITE,
        Material.DIORITE,
        Material.POLISHED_DIORITE,
        Material.ANDESITE,
        Material.POLISHED_ANDESITE,
        Material.DEEPSLATE,
        Material.COBBLED_DEEPSLATE,
        Material.POLISHED_DEEPSLATE,
        Material.BLACKSTONE,
        Material.POLISHED_BLACKSTONE,
        Material.NETHERRACK,
        Material.BASALT,
        Material.SMOOTH_BASALT,
        Material.TERRACOTTA,
        Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA,
        Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA,
        Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA,
        Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA,
        Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA,
        Material.BLACK_TERRACOTTA,
        Material.IRON_BLOCK,
        Material.COPPER_BLOCK,
        Material.GOLD_BLOCK,
        Material.DIAMOND_BLOCK,
        Material.EMERALD_BLOCK,
        Material.REDSTONE_BLOCK,
        Material.LAPIS_BLOCK,
        Material.NETHERITE_BLOCK,
        Material.ANVIL,
        Material.STONE_BRICKS,
        Material.CRACKED_STONE_BRICKS,
        Material.MOSSY_STONE_BRICKS,
        Material.CHISELED_STONE_BRICKS,
        Material.BRICKS,
        Material.QUARTZ_BLOCK,
        Material.SMOOTH_QUARTZ,
        Material.END_STONE,
        Material.PURPUR_BLOCK,
        Material.OBSIDIAN
    );

    // Hard wood-based surfaces
    public static final Set<Material> WOOD_SURFACES = EnumSet.of(
        Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS,
        Material.JUNGLE_PLANKS, Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS,
        Material.MANGROVE_PLANKS, Material.CHERRY_PLANKS, Material.BAMBOO_PLANKS,
        Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_BIRCH_LOG,
        Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG,
        Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_CHERRY_LOG, Material.STRIPPED_BAMBOO_BLOCK,
        Material.NOTE_BLOCK,
        Material.BARREL,
        Material.CRAFTING_TABLE,
        Material.LOOM,
        Material.BOOKSHELF
    );

    // Glass, ice, glazed surfaces
    public static final Set<Material> SLICK_SURFACES = EnumSet.of(
        
        Material.WHITE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA,
        Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
        Material.PINK_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
        Material.CYAN_GLAZED_TERRACOTTA, Material.PURPLE_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA,
        Material.BROWN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA,
        Material.BLACK_GLAZED_TERRACOTTA,
        Material.SEA_LANTERN,
        Material.BEACON,
        Material.ICE,
        Material.PACKED_ICE,
        Material.BLUE_ICE
    );
    
    public static final Set<Material> GLASSES = EnumSet.of(Material.GLASS,
        Material.TINTED_GLASS,
        Material.GLASS_PANE);

    // Custom physics-based or special gameplay surfaces
    public static final Set<Material> SPECIAL_SURFACES = EnumSet.of(
        Material.SLIME_BLOCK,
        Material.HONEY_BLOCK,
        Material.SNOW_BLOCK,
        Material.SCAFFOLDING,
        Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
        Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
        Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
        Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
        Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
        Material.BLACK_WOOL
    );
    
    public static final Set<Material> ALL_BOUNCE_SURFACES =
    	    Stream.of(HARD_SURFACES, WOOD_SURFACES, SLICK_SURFACES, SPECIAL_SURFACES)
    	          .flatMap(Set::stream)
    	          .collect(Collectors.toSet());


    public static double getBounceCoefficient(Material material) {
        if (BounceSurfaces.SPECIAL_SURFACES.contains(material)) {
            // Custom logic bounciness
            if (material == Material.SLIME_BLOCK) return 0.95;
            if (material == Material.HONEY_BLOCK) return 0.2;
            if (material.name().endsWith("_WOOL")) return 0.4;
            if (material == Material.SNOW_BLOCK) return 0.3;
            if (material == Material.SCAFFOLDING) return 0.6;
        }

        if (BounceSurfaces.SLICK_SURFACES.contains(material)) {
            if (material == Material.ICE) return 0.8;
            if (material == Material.PACKED_ICE) return 0.85;
            if (material == Material.BLUE_ICE) return 0.8;
            return 0.75; // general glazed/glass
        }

        if (BounceSurfaces.WOOD_SURFACES.contains(material)) {
            return 0.5;
        }

        if (BounceSurfaces.HARD_SURFACES.contains(material)) {
            if (material == Material.OBSIDIAN) return 0.95;
            if (material == Material.NETHERRACK) return 0.35;
            return 0.8; // generic hard material
        }

        // Default (non-bounceable or unknown surface)
        return 0;
    }


}

