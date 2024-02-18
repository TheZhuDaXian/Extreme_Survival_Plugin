package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private Main plugin;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // 将玩家模式改为冒险模式
        player.setGameMode(GameMode.SPECTATOR);
        //清空玩家物品栏
        player.getInventory().clear();
        // 设置玩家处于死亡模式的标记，防止聊天与除了/respawn之外的指令
        plugin.setPlayerDeathMode(player, true);

        // 发送提示信息(假设插件有sendMessage方法处理颜色和本地化)
        plugin.sendMessage(player, ChatColor.RED + "你死了! 使用 /respawn 来复活。");
    }
}