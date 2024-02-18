package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.Random;
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
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "正在寻找复活点...");
                    performPlayerRespawn(player);
                    player.sendMessage(ChatColor.GOLD + "你已经复活并且支付了" + respawnCost + "金币。");
                } else {
                    player.sendMessage(ChatColor.GOLD + "你需要" + respawnCost + "金币来复活。");
                }
            }
        }
    }

    private void performPlayerRespawn(Player player) {
        // 取消死亡模式的标记
        plugin.setPlayerDeathMode(player, false);

        // 设置玩家健康和饱食度
        player.setHealth(20.0); // 或者 player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);

        // 可选地恢复玩家火焰量和氧气量
        player.setFireTicks(0);
        player.setRemainingAir(player.getMaximumAir());

        // 随机传送玩家到一个安全的位置
        Location safeRespawnLocation = findRandomSafeLocation(player.getWorld());
        if (safeRespawnLocation != null) {
            player.teleport(safeRespawnLocation);
            player.setGameMode(GameMode.SURVIVAL); // 设置为生存模式
        } else {
            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(ChatColor.YELLOW + "未能找到安全的复活地点，你已在世界出生点复活。");

        }
    }

    private Location findRandomSafeLocation(World world) {
        Random random = new Random();
        int range = 1000; // 确定一个合适的范围值
        for (int i = 0; i < 50; i++) { // 尝试10次找到安全位置
            int x = random.nextInt(range * 2) - range;
            int z = random.nextInt(range * 2) - range;
            int y = world.getHighestBlockYAt(x, z) + 1;

            Location location = new Location(world, x + 0.5, y, z + 0.5);
            if (isLocationSafe(location)) {
                return location;
            }
        }
        return null; // 如果没有找到安全位置则返回null
    }

    private boolean isLocationSafe(Location location) {
        Block feet = location.getBlock();
        Block ground = feet.getRelative(0, -1, 0);
        return ground.getType().isSolid() && feet.getType() == Material.AIR && feet.getRelative(0, 1, 0).getType() == Material.AIR;
    }
}


