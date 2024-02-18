package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private Main plugin;

    public GUIListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        int respawnCost = plugin.getRespawnCost();
        if (event.getView().getTitle().equals(ChatColor.GREEN + "复活界面")) {
            event.setCancelled(true); // 防止玩家拿取物品

            // ... 如之前的代码 ...
            if (clickedItem != null && clickedItem.getType() == Material.EMERALD) {
                // 玩家点击了绿宝石

                // 检查金币，如果足够则复活玩家
                if (EconomyUtil.hasEnoughMoney(player, respawnCost)) {
                    EconomyUtil.chargePlayer(player, respawnCost);
                    // 复活玩家的逻辑
                    // ...
                    performPlayerRespawn(player);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "你已经复活并且支付了" + respawnCost + "金币。");
                } else {
                    player.sendMessage(ChatColor.GOLD + "你需要" + respawnCost + "金币来复活。");
                }
            }
        }
    }

    // ... 如之前的代码 ...
    private void performPlayerRespawn(Player player) {
        // 取消死亡模式的标记
        plugin.setPlayerDeathMode(player, false);


        // 设置玩家健康和饱食度
        player.setHealth(20.0); // 或者 player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);

        // 可选地恢复玩家火焰量和氧气量
        player.setFireTicks(0);
        player.setRemainingAir(player.getMaximumAir());

        // 传送玩家到世界的出生点
        player.teleport(player.getWorld().getSpawnLocation());

        //设置为生存
        player.setGameMode(GameMode.SURVIVAL);

        // 如果有必要，重置其他玩家状态或游戏模式
        // player.setGameMode(GameMode.SURVIVAL);
    }
}


