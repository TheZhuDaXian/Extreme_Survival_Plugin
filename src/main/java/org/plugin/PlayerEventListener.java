package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import java.util.Random;

// 其他导入...

public class PlayerEventListener implements Listener {
    private Main plugin;

    public PlayerEventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 检查这是否是玩家第一次加入游戏
        if (!player.hasPlayedBefore()) {
            // 玩家是第一次加入，生成一个随机的安全位置
            Location randomSafeLocation = findRandomSafeLocation(player.getWorld());
            if (randomSafeLocation != null) {
                player.teleport(randomSafeLocation);
                player.sendMessage(ChatColor.GREEN + "欢迎加入！你已随机传送到一个安全的地方。");
            } else {
                // 如果没有找到安全位置，则做一些备用处理，例如传送到默认出生点
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage(ChatColor.YELLOW + "欢迎加入！你已传送到世界出生点。");
            }
        } else {
            // 玩家以前玩过，不需要特殊处理，他们会在他们退出时的位置出现
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