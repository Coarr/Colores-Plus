package me.coarr.main;

import java.util.ArrayList;
import java.util.HashMap;
import me.coarr.main.API.CoolPlaceholders;
import me.coarr.main.API.MySqlManager;
import me.coarr.main.API.StatsManager;
import me.coarr.main.Commands.ColorCommand;
import me.coarr.main.Commands.NameColorCommand;
import me.coarr.main.Commands.TabColor;
import me.coarr.main.Events.ColorListener;
import me.coarr.main.Listener.ChatListener;
import me.coarr.main.Listener.InventoryClickListener;
import me.coarr.main.config.FileAPI;
import net.md_5.bungee.api.Colores;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    public static Main plugin;
    public static HashMap<Player, String> currentColor;
    public static HashMap<Player, String> currentNameColor;
    public static HashMap<Player, String> currentTabColor;
    public static FileAPI fileAPI;
    public static String INV_TITLE;
    public static String NAME_INV_TITLE;
    public static String TAB_INV_TITLE;
    public static String secondInvTitle;
    public static String seconNameInvTitle;
    public static ArrayList<String> itemId;
    public static ArrayList<String> NameitemId;
    public static ArrayList<String> messages;
    public static ArrayList<String> Namemessages;
    public static ArrayList<String> Tabmessages;
    public static ArrayList<String> TabitemId;
    public static MySqlManager mysql;
    private StatsManager manager;

    public void onEnable() {
        String str2;
        plugin = this;
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CoolPlaceholders(plugin).hook();
            System.out.println("Yep");
        } else {
            System.out.println("nop");
        }
        this.registerEvents();
        fileAPI.loadAllFiles();
        this.registerCommands();
        NAME_INV_TITLE = Main.fileAPI.cfg.getString("NameColorMessage.ColorInv");
        INV_TITLE = Main.fileAPI.cfg.getString("Message.ColorInv");
        TAB_INV_TITLE = Main.fileAPI.cfg.getString("TabColorMessage.ColorInv");
        secondInvTitle = Main.fileAPI.cfg.getString("secondInv.TitleMain");
        for (String str : Main.fileAPI.cfg.getConfigurationSection("Items").getKeys(false)) {
            str2 = Main.fileAPI.cfg.getString("Items." + str);
            itemId.add(str2);
            messages.add(str);
        }
        for (String str : Main.fileAPI.cfg.getConfigurationSection("NameColorItems").getKeys(false)) {
            str2 = Main.fileAPI.cfg.getString("NameColorItems." + str);
            NameitemId.add(str2);
            Namemessages.add(str);
        }
        if (Namemessages.contains("Rainbow")) {
            Namemessages.remove("Rainbow");
        } else if (NameitemId.contains("Rainbow")) {
            NameitemId.remove("Rainbow");
        }
        for (String str : Main.fileAPI.cfg.getConfigurationSection("TabColorItems").getKeys(false)) {
            str2 = Main.fileAPI.cfg.getString("TabColorItems." + str);
            TabitemId.add(str2);
            Tabmessages.add(str);
        }
        if (Tabmessages.contains("Rainbow")) {
            Tabmessages.remove("Rainbow");
        } else if (TabitemId.contains("Rainbow")) {
            TabitemId.remove("Rainbow");
        }
        fileAPI.loadMySql();
    }

    public void onDisable() {
        fileAPI.saveAllFiles();
        if (mysql == null) {
            System.out.println("Error: me.coarr.main line 72");
        }
        if (Main.getMySqlState()) {
            try {
                mysql.disconnect();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new InventoryClickListener(), (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new ColorListener(this), (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new ChatListener(this), (Plugin)this);
    }

    private void registerCommands() {
        this.getCommand("Colores").setExecutor((CommandExecutor)new ColorCommand());
        this.getCommand("namecolor").setExecutor((CommandExecutor)new NameColorCommand());
        this.getCommand("tabcolor").setExecutor((CommandExecutor)new TabColor());
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static boolean getMySqlState() {
        try {
            return Main.fileAPI.MySqlCfg.getBoolean("MySql.data.MySql");
        }
        catch (Exception e) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.isOp()) continue;
                p.sendMessage("Colores+ error 32 pls send the latest log to coarr on Spigot.");
            }
            return false;
        }
    }

    public static String noperm() {
        return Colores.translateAlternateColorCodes((char)'&', (String)Main.fileAPI.cfg.getString("Message.noperm"));
    }

    public static String NameColornoperm() {
        return Colores.translateAlternateColorCodes((char)'&', (String)Main.fileAPI.cfg.getString("NameColorMessage.noperm"));
    }

    public static String TabColornoperm() {
        return Colores.translateAlternateColorCodes((char)'&', (String)Main.fileAPI.cfg.getString("TabColorMessage.noperm"));
    }

    static {
        currentColor = new HashMap();
        currentNameColor = new HashMap();
        currentTabColor = new HashMap();
        fileAPI = new FileAPI();
        itemId = new ArrayList();
        NameitemId = new ArrayList();
        messages = new ArrayList();
        Namemessages = new ArrayList();
        Tabmessages = new ArrayList();
        TabitemId = new ArrayList();
    }
}