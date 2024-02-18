package org.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RespawnGUI {
    public static void openRespawnGUI(Player player) {
        // 你可以使用一个字段或配置文件来管理此变量，以便于修改它

        String guiName = ChatColor.GREEN + "复活界面";
        Inventory inv = Bukkit.createInventory(null, 27, guiName);

        ItemStack respawnItem = new ItemStack(Material.EMERALD);
        ItemMeta meta = respawnItem.getItemMeta();
        if (meta != null) { // 为了安全起见，检查meta是否不为null
            meta.setDisplayName(ChatColor.GREEN + "点击复活");
            // 如果需要，添加物品详细信息
            // meta.setLore(Arrays.asList(ChatColor.GRAY + "这将花费你respawnCost金币", ChatColor.GRAY + "来复活。"));
            respawnItem.setItemMeta(meta);
        }

        // 将绿宝石放置在界面中间
        inv.setItem(13, respawnItem);

        // 可以选择添加其他物品来填充空的格子，比如用玻璃板隔开复活物品

        player.openInventory(inv);
    }
}