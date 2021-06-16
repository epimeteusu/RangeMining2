package epi.rangemining;

import org.bukkit.Material;

public class check_item {
    static boolean check_brock(Material brock_type) {
        boolean check;
        switch (brock_type) {
            case STONE:
            case GRANITE:
            case DIORITE:
            case ANDESITE:
            case GRASS_BLOCK:
            case DIRT:
            case COARSE_DIRT:
            case PODZOL:
            case CRIMSON_NYLIUM:
            case WARPED_NYLIUM:
            case COBBLESTONE:
            case SAND:
            case RED_SAND:
            case GRAVEL:
            case GOLD_ORE:
            case IRON_ORE:
            case COAL_ORE:
            case NETHER_GOLD_ORE:
            case LAPIS_ORE:
            case SANDSTONE:
            case CHISELED_SANDSTONE:
            case CUT_SANDSTONE:
            case OBSIDIAN:
            case DIAMOND_ORE:
            case REDSTONE_ORE:
            case CLAY:
            case NETHERRACK:
            case SOUL_SAND:
            case SOUL_SOIL:
            case BASALT:
            case MYCELIUM:
            case END_STONE:
            case EMERALD_ORE:
            case QUARTZ_BLOCK:
            case MAGMA_BLOCK:
            case NETHER_WART_BLOCK:
            case RED_NETHER_BRICKS:
            case COPPER_BLOCK:
            case DEEPSLATE:
            case TUFF:
                check = true;
                break;
            default:
                check = false;
        }
        return check;
    }
}
