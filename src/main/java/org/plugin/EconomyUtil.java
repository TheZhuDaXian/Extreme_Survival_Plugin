package org.plugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyUtil {
    private static Economy econ = null;

    public static boolean setupEconomy(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static boolean hasEnoughMoney(Player player, double amount) {
        return econ.has(player, amount);
    }

    public static void chargePlayer(Player player, double amount) {
        econ.withdrawPlayer(player, amount);
    }

    public static Economy getEconomy() {
        return econ;
    }
}