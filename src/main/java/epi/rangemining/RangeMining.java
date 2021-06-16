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
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return command.cmd_Supplement(sender,cmd,alias,args);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return command.cmd_check(sender,cmd,commandLabel,args,map);
    }

    @EventHandler
    public void onPlayerJoin(BlockBreakEvent e) {
        Block b = e.getBlock();//破壊ブロックの取得
        ItemStack p = e.getPlayer().getInventory().getItemInMainHand();
        //ItemStack p = e.getPlayer().getItemInHand(); 非推奨
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
                        if (check_item.check_brock(brock_type)) {
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
            //新入手アイテムの要素を抜き出し
            for (ItemStack get_item : new_giveItemList) {//新規リストの要素数ループ
                //↑↑↑↑(int i = 0; i < new_giveItemList.size(); i++)と同じ
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


}
