package org.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RespawnCommand implements CommandExecutor {

    private Main plugin;
    private static final String ONLY_PLAYER_COMMAND = ChatColor.RED + "此命令仅限玩家使用。";
    private static final String NOT_IN_DEATH_MODE = ChatColor.RED + "你当前不处于死亡模式，无法使用此命令。";

    public RespawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 确保命令是由玩家发起的
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // 检查玩家是否处于死亡模式
            if (plugin.isPlayerInDeathMode(player)) {
                // 玩家处于死亡模式，可以使用这个命令
                // 执行复活逻辑，比如打开复活界面
                RespawnGUI.openRespawnGUI(player);
                return true;
            } else {
                // 玩家不处于死亡模式，不允许使用这个命令
                player.sendMessage(NOT_IN_DEATH_MODE);
                return true;
            }
        } else {
            // 命令是从控制台或其他非玩家实体执行
            sender.sendMessage(ONLY_PLAYER_COMMAND);
            return true;
        }
    }
}