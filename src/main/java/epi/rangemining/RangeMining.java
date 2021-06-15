package epi.rangemining;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class RangeMining extends JavaPlugin implements Listener {
    HashMap<String, Integer> map = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("プラグインが有効になったよ!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが無効になったよ!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("プレイヤーがログインしました: " + e.getPlayer().getName());
        map.put(e.getPlayer().getName(), 0);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage("プレイヤーがログインしました: " + e.getPlayer().getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("rm")) return super.onTabComplete(sender, command, alias, args);
        if (args.length == 1) {
            if (args[0].length() == 0) { // /testまで
                return Arrays.asList("normal", "small", "medium", "large");
            } else {
                //入力されている文字列と先頭一致
                if ("normal".startsWith(args[0])) {
                    return Collections.singletonList("normal");
                } else if ("small".startsWith(args[0])) {
                    return Collections.singletonList("small");
                } else if ("medium".startsWith(args[0])) {
                    return Collections.singletonList("medium");
                } else if ("large".startsWith(args[0])) {
                    return Collections.singletonList("large");
                }
            }
        }
        //JavaPlugin#onTabComplete()を呼び出す
        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // test コマンドの処理
        if (cmd.getName().equalsIgnoreCase("rm")) {
            sender.sendMessage(sender.getName());
            if (args[0].equalsIgnoreCase("normal")) {
                sender.sendMessage(ChatColor.GREEN + "通常の採掘範囲です");
                map.put(sender.getName(), 0);
                return true;
            }
            if (args[0].equalsIgnoreCase("small")) {
                sender.sendMessage(ChatColor.GREEN + "3*3範囲を採掘します");
                map.put(sender.getName(), 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("medium")) {
                sender.sendMessage(ChatColor.GREEN + "5*5範囲を採掘します");
                map.put(sender.getName(), 2);
                return true;
            }
            if (args[0].equalsIgnoreCase("large")) {
                sender.sendMessage(ChatColor.GREEN + "7*7範囲を採掘します");
                map.put(sender.getName(), 3);
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(BlockBreakEvent e) {
        Block b = e.getBlock();//破壊ブロックの取得
        ItemStack p = e.getPlayer().getItemInHand();
        if (p.getType() == Material.DIAMOND_PICKAXE) {
            List<ItemStack> giveItemList = null;
            int r = map.get(e.getPlayer().getName());
            Location startLoc = b.getLocation().subtract(r, 0, r);

            for (int x = startLoc.getBlockX(); x < startLoc.getBlockX() + 1 + r * 2; x++) {
                for (int y = startLoc.getBlockY(); y < startLoc.getBlockY() + 1 + r * 2; y++) {
                    for (int z = startLoc.getBlockZ(); z < startLoc.getBlockZ() + 1 + r * 2; z++) {
                        Location loc = new Location(startLoc.getWorld(), x, y, z);
                        Block b2 = loc.getBlock();
                        Material brock_type = b2.getType();
                        if (check_brock(brock_type)) {
                            giveItemList = give_item(giveItemList, b2);
                            //e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), giveItemList.get(0));
                            b2.setType(Material.AIR);
                            //b2.breakNaturally();
                        }
                    }
                }
            }
            for (int i = 0; i < Objects.requireNonNull(giveItemList).size(); i++) {
                e.getPlayer().sendMessage(String.valueOf(giveItemList.get(i)));
                e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), giveItemList.get(i));
            }
            e.getPlayer().sendMessage("-------------------------------------------");
        }
    }

    public List<ItemStack> give_item(List<ItemStack> giveItemList, Block b2) {
        List<ItemStack> new_giveItemList = (List<ItemStack>) b2.getDrops();//新規リストの仮代入
        if (giveItemList == null) {
            giveItemList = new_giveItemList;
        } else {
            for (int i = 0; i < new_giveItemList.size(); i++) {//新規リストの要素数ループ
                ItemStack get_item = new_giveItemList.get(i);//新入手アイテムの要素を抜き出し
                boolean contains = false; //重複判定
                int index = 0; //重複No.
                for (int f = 0; f < giveItemList.size(); f++) {
                    ItemStack give_item = giveItemList.get(f);//最終アイテムリストから抜き出し
                    if (give_item.getType() == get_item.getType()) { //同一アイテム名があった時
                        contains = true;
                        index = f;
                        break;
                    }
                }
                if (contains) {
                    giveItemList.set(index, new ItemStack(get_item.getType(), get_item.getAmount() + giveItemList.get(index).getAmount()));
                } else {
                    giveItemList.add(get_item);
                }
            }
        }
        return giveItemList;
    }

    //giveItemList.set(f, new ItemStack(get_item.getType(),get_item.getAmount() + giveItemList.get(f).getAmount()));
    //giveItemList.add(get_item);

    private boolean check_brock(Material brock_type) {
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
