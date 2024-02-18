package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerActivityListener implements Listener {

    private final Main plugin;

    public PlayerActivityListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.isPlayerInDeathMode(player)) {
            // 可以给玩家一条消息说明为何不能聊天
            player.sendMessage(ChatColor.RED + "你在死亡模式下不能聊天。");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.isPlayerInDeathMode(player)) {
            String command = event.getMessage().split(" ")[0];
            if (!command.equalsIgnoreCase("/respawn")) {
                // 可以给玩家一条消息说明为何不能使用指令
                player.sendMessage(ChatColor.RED + "你在死亡模式下不能使用指令，输入/respawn复活。");
                event.setCancelled(true);
            }
        }
    }
}