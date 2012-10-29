package com.timvisee.customskulls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class CommandHandler {
	
	public static CustomSkulls plugin;

	public CommandHandler(CustomSkulls instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("customskulls") ||
				commandLabel.equalsIgnoreCase("customskull") ||
				commandLabel.equalsIgnoreCase("cs")) {
			
			PermissionsManager pm = plugin.getPermissionsManager();
			
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}

			if(args[0].equals("status") || args[0].equals("statics") || args[0].equals("stats") || args[0].equals("s")) {
				
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "customskulls.command.status")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Define some variables with plugin data
				PluginManager pluginManager = plugin.getServer().getPluginManager();
				PluginDescriptionFile pdfFile = plugin.getDescription();
				
				sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS STATUS ]==========");
				sender.sendMessage(ChatColor.GRAY + "Plugin Information:");
				
				// Return the used permissions system
				if(pm.isEnabled()) {
					sender.sendMessage(ChatColor.GOLD + "Permissions: " + ChatColor.GREEN + pm.getUsedPermissionsSystemType().getName());
				} else
					sender.sendMessage(ChatColor.GOLD + "Permissions: " + ChatColor.DARK_RED + "Disabled");
				
				// Return the running time
				long diff = new Date().getTime() - plugin.getEnabledDate().getTime();
				int millis = (int) (diff % 1000);
				diff/=1000;
				int seconds = (int) (diff % 60);
				diff/=60;
				int minutes = (int) (diff % 60);
				diff/=60;
				int hours = (int) diff;
				String t = ChatColor.WHITE + String.valueOf(millis) + ChatColor.YELLOW + " Millis";
				if(seconds > 0 || minutes > 0 || hours > 0) {
					t = ChatColor.WHITE + String.valueOf(seconds) + ChatColor.YELLOW + " Secconds & " + t;
					if(minutes > 0 || hours > 0) {
						t = ChatColor.WHITE + String.valueOf(minutes) + ChatColor.YELLOW + " Minutes, " + t;
						if(hours > 0)
							t = ChatColor.WHITE + String.valueOf(hours) + ChatColor.YELLOW + " Hours, " + t;
					}
				}
				sender.sendMessage(ChatColor.GOLD + "Time Running: " + ChatColor.YELLOW + t);
				
				sender.sendMessage(ChatColor.GRAY + "Server Information:");
				
				// Return player info
				sender.sendMessage(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + String.valueOf(plugin.getServer().getOnlinePlayers().length) + " / " + String.valueOf(plugin.getServer().getMaxPlayers()));
				sender.sendMessage(ChatColor.GOLD + "Running Plugins: " + ChatColor.YELLOW + String.valueOf(pluginManager.getPlugins().length));
				
				// Return the version info
				sender.sendMessage(ChatColor.GOLD + "Server Version: " + ChatColor.YELLOW + plugin.getServer().getVersion());
				sender.sendMessage(ChatColor.GOLD + "Bukkit Version: " + ChatColor.YELLOW + plugin.getServer().getBukkitVersion());
				
				// Return the current date
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String d = dateFormat.format(date);
				sender.sendMessage(ChatColor.GOLD + "Server Time: " + ChatColor.YELLOW + d);
				
				sender.sendMessage(ChatColor.GRAY + "Machine Information:");
				
				// Return server/java info
				sender.sendMessage(ChatColor.GOLD + "OS Name: " + ChatColor.YELLOW + System.getProperty("os.name"));
				sender.sendMessage(ChatColor.GOLD + "OS Version: " + ChatColor.YELLOW + System.getProperty("os.version"));
				sender.sendMessage(ChatColor.GOLD + "Java Version: " + ChatColor.YELLOW + System.getProperty("java.version"));
				return true;
				
			} else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load")) {
				
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "customskulls.command.reload")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.YELLOW + "[CustomSkulls] Reloading data...");
				long t = System.currentTimeMillis();
				// Reload all data
				plugin.reloadConfigFiles();
				long duration = System.currentTimeMillis() - t;
				sender.sendMessage(ChatColor.YELLOW + "[CustomSkulls] " + ChatColor.GREEN + "Data succesfuly loaded, took " + ChatColor.GOLD + String.valueOf(duration) + "ms" + ChatColor.GREEN + "!");
				return true;
				
			} else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v") ||
					args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("a")) {
				
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				PluginDescriptionFile pdfFile = plugin.getDescription();
				sender.sendMessage(ChatColor.YELLOW + "This server is running Custom Skulls v" + pdfFile.getVersion());
				sender.sendMessage(ChatColor.YELLOW + "Custom Skulls is made by Tim Visee - timvisee.com");
				return true;
				
			} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {			
				
				// Help commands (/ss help [sub-categories])
				if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
					if(args.length == 1) {
						// View the help
						sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " help [command]" + ChatColor.WHITE + " : View help");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload" + ChatColor.WHITE + " : Reload all data");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " status" + ChatColor.WHITE + " : View all plugin status");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " version" + ChatColor.WHITE + " : View plugin version");
						return true;
						
					}
					if(args[1].equals("reload") || args[1].equals("load")) {
						// View the help
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload" + ChatColor.WHITE + " : Reload all data");
						return true;
					}
					if(args[1].equals("status") || args[1].equals("statics") || args[1].equals("stats") || args[1].equals("s")) {
						// View the help
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " status" + ChatColor.WHITE + " : View major plugin status");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GOLD + "This command is used for troubleshooting problems");
						return true;
					}
					if(args[1].equals("version") || args[1].equals("ver") || args[1].equals("v") ||
							args[1].equals("info") || args[1].equals("i") || 
							args[1].equals("about") || args[1].equals("a")) {
						// View the help
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GREEN + "==========[ CUSTOM SKULLS HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " version" + ChatColor.WHITE + " : View plugin version info");
						return true;
					}
					
					// Check wrong command values
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
			}
		}
		return false;
	}
}
