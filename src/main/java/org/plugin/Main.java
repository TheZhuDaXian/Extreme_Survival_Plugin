package org.plugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Main extends JavaPlugin {
    private File deathModeFile;
    private FileConfiguration deathModeConfig;


    // 管理死亡模式的玩家集合
    private Set<UUID> deathModePlayers = new HashSet<>();
    private int respawnCost;
    @Override
    public void onEnable() {
        getLogger() .info("插件已启动~~~作者：哔哩哔哩@我就是猪大仙啦");

        //初始化config.yml
        saveDefaultConfig();
        reloadConfig();
        respawnCost = getConfig().getInt("settings.respawn.cost");

        // 初始化配置文件
        deathModeFile = new File(getDataFolder(), "deathModePlayers.yml");
        if (!deathModeFile.exists()) {
            deathModeFile.getParentFile().mkdirs();
            saveResource("deathModePlayers.yml", false);
        }

        deathModeConfig = new YamlConfiguration();
        try {
            try {
                deathModeConfig.load(deathModeFile);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
            // 加载UUIDs到deathModePlayers集合
            deathModeConfig.getStringList("players").forEach(uuid -> deathModePlayers.add(UUID.fromString(uuid)));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        // 注册 PlayerEventListener 以便监听事件
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        //玩家聊天事件监听器
        getServer().getPluginManager().registerEvents(new PlayerActivityListener(this), this);
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new org.plugin.PlayerDeathListener(this), this);
        // 注册GUI事件监听
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        // 注册命令
        getCommand("respawn").setExecutor(new RespawnCommand(this));
        // 设置经济系统
        if (!EconomyUtil.setupEconomy(this)) {
            getLogger().severe(String.format("[%s] - 禁用插件，未找到Vault依赖!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    public int getRespawnCost() {
        return respawnCost;
    }
    @Override
    public void onDisable() {
        getLogger().info("插件已停用");
        // 保存UUIDs从deathModePlayers集合到文件
        deathModeConfig.set("players", deathModePlayers.stream().map(UUID::toString).collect(Collectors.toList()));
        try {
            deathModeConfig.save(deathModeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 插件停用时的逻辑
    }

    // 管理玩家死亡模式的方法
    public void setPlayerDeathMode(Player player, boolean mode) {
        if (mode) {
            deathModePlayers.add(player.getUniqueId());
        } else {
            deathModePlayers.remove(player.getUniqueId());
        }
    }

    public boolean isPlayerInDeathMode(Player player) {
        return deathModePlayers.contains(player.getUniqueId());
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }
}
