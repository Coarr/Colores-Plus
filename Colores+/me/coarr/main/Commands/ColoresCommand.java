package me.coarr.main.Commands;

import me.coarr.main.API.StatsManager;
import me.coarr.main.Enum.ColorEnum;
import me.coarr.main.Main;
import me.chemmic.main.config.FileAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ColorCommand
implements CommandExecutor {
    private static FileAPI api = new FileAPI();
    StatsManager manager = new StatsManager();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p = (Player)sender;
            if (p.hasPermission("colores.use")) {
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)Main.getPlugin(), (Runnable)new /* Unavailable Anonymous Inner Class!! */);
            } else {
                p.sendMessage(Main.noperm());
            }
        } else {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reset")) {
                    if (sender.hasPermission("colores.command.reset")) {
                        if (Bukkit.getServer().getPlayer(args[1]) == null) {
                            sender.sendMessage("\u00a7a" + args[1] + " \u00a7cwas never on this Server or does not exist.");
                            return false;
                        }
                        Player user = Bukkit.getServer().getPlayer(args[1]);
                        if (!Main.getMySqlState()) {
                            if (api.getcoloresCfg().getString("Colors." + user.getName() + ".color") != null) {
                                api.getcoloresCfg().set("Colors." + user.getName() + ".color", null);
                                api.saveFile(api.getcoloresCfg(), api.getColorFile());
                                sender.sendMessage("\u00a7cYou have changed \u00a7a" + user.getName() + "'s\u00a7c colores back to normal!");
                            } else if (this.manager.playerExists(user.getName())) {
                                this.manager.setcolores(user.getName(), null);
                            }
                        } else {
                            sender.sendMessage("\u00a7c" + user.getName() + " \u00a74does not have a colores!");
                        }
                    } else {
                        sender.sendMessage(Main.noperm());
                    }
                    return false;
                }
                sender.sendMessage("\u00a7cUsage: /colores reset <player> or /colores set <Player> <color>\n\u00a7eExample: /colores set " + sender.getName() + " blue");
                return false;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (sender.hasPermission("colores.command.set")) {
                        if (Bukkit.getServer().getPlayer(args[1]) == null) {
                            sender.sendMessage("\u00a7a" + args[1] + " \u00a7cwas never on this Server or does not exist.");
                            return false;
                        }
                        Player user = Bukkit.getServer().getPlayer(args[1]);
                        String color = args[2];
                        if (!FileAPI.Colors.contains(color.toUpperCase())) {
                            sender.sendMessage("\u00a7cWrong color! Available Colors: \u00a7e" + FileAPI.Colors.toString());
                            return false;
                        }
                        color = color.equalsIgnoreCase("rainbow") ? "rainbow" : ColorEnum.convertColorCode((String)color);
                        if (!Main.getMySqlState()) {
                            api.getcoloresCfg().set("Colors." + user.getName() + ".color", (Object)color);
                            api.saveFile(api.getcoloresCfg(), api.getColorFile());
                        } else if (this.manager.playerExists(user.getName())) {
                            this.manager.setcolores(user.getName(), color);
                        }
                        sender.sendMessage("\u00a7cYou have changed \u00a7a" + user.getName() + "'s\u00a7c colores to " + args[2]);
                    } else {
                        sender.sendMessage(Main.noperm());
                    }
                } else {
                    sender.sendMessage("\u00a7cUsage: /colores set <player> <color> \n\u00a7eExample: /colores set " + sender.getName() + " blue");
                }
            } else {
                sender.sendMessage("\u00a7cUsage: /colores reset <player> or /colores set <Player> <color>\n\u00a7eExample: /chatcolor set " + sender.getName() + " blue");
            }
        }
        return false;
    }
}