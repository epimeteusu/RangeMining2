package epi.rangemining;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class command {
    public static List<String> cmd_Supplement(Command cmd, String alias, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("rm"))
            return cmd_Supplement(cmd, alias, args);
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
        return cmd_Supplement(cmd, alias, args);
    }

    public static boolean cmd_check(CommandSender sender, Command cmd, String[] args, HashMap<String, Integer> map) {
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


}
