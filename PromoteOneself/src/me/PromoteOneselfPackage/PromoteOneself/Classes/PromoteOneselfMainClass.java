package me.PromoteOneselfPackage.PromoteOneself.Classes;

import java.util.Collections;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import org.black_ixx.playerpoints.PlayerPoints; 

public class PromoteOneselfMainClass extends JavaPlugin implements CommandExecutor { 
	public static PromoteOneselfMainClass plugin; 
	
	public final LoggingClass logger = new LoggingClass(this, "Minecraft"); 
	public final YamlFiles yc = new YamlFiles(this, logger, "config.yml", "config.yml"); 
	public final YamlFiles yd = new YamlFiles(this, logger, "players.yml", "players.yml"); 
	public final YamlFiles ys = new YamlFiles(this, logger, "signs.yml", "signs.yml"); 
	private final CommandHelp ch = new  CommandHelp(this, logger); 
	
	private final GetPlayerProperties pp = new GetPlayerProperties(this, logger); 
	private final UpdateAims ua = new UpdateAims(this, logger, pp); 
	private final MyPlayerListener pl = new MyPlayerListener(this, ua); 
	private final MySignListener sl = new MySignListener(this, logger, ua); 
	private final CheckPlayers cp = new CheckPlayers(this, logger, ua, pp); 
	
	public Economy econ = null; 
	public Boolean econExists = false; 
	public net.milkbowl.vault.permission.Permission perms = null; 
	public Boolean permsExist = false; 
	public PlayerPoints playerPointsPlugin = null; 
	public Boolean playerPointsExists = false; 
	public Set<String> targetPermissionTargets = Collections.emptySet(); 
	public Set<String> signPermissionSigns = Collections.emptySet(); 
	
	@Override 
	public void onDisable() {
		logger.disable(); 
		saveFiles(); 
	}
	
	@Override 
	public void onEnable() {
		logger.enable(); 
		PluginManager pm = getServer().getPluginManager(); 
		pm.registerEvents(pl, this); 
		pm.registerEvents(sl, this); 
		registerExtraPermissions(pm); 
		setupVault(pm); 
		setupPlayerPoints(pm); 
		YamlFiles.alwaysSaveFiles = yc.configuration.getBoolean("alwaysSaveFiles"); 
		YamlFiles.checkManualAimWorldNames(yc); 
	}
	
	private void registerExtraPermissions(PluginManager pm) {
		Set<String> targets = Collections.emptySet(); 
		Set<String> signs = Collections.emptySet(); 
		try {
			targets = yc.configuration.getConfigurationSection("targets").getKeys(false); 
		}
		catch (NullPointerException e) {
			// No action required 
		}
		try {
			signs = ys.configuration.getConfigurationSection("signs").getKeys(false); 
		}
		catch (NullPointerException e) {
			// No action required 
		}
		YamlFiles.checkAdditionalPermissions(signs, targets, pm); 
	}
	
	private void setupVault(PluginManager pm) {
		ServicesManager sm = getServer().getServicesManager(); 
		if ((pm.getPlugin("Vault") == null) || (sm.getRegistration(Economy.class) == null) || (sm.getRegistration(Economy.class).getProvider() == null)) {
			econExists = false; 
			logger.info("custom", "Cannot hook in to an economy system "); 
		}
		else {
			econExists = true; 
			RegisteredServiceProvider<Economy> rsp = sm.getRegistration(Economy.class); 
			econ = rsp.getProvider(); 
			logger.info("custom", "Hooked in to vault for an economy system "); 
		}
		if ((pm.getPlugin("Vault") == null) || (sm.getRegistration(net.milkbowl.vault.permission.Permission.class) == null) || (sm.getRegistration(net.milkbowl.vault.permission.Permission.class).getProvider() == null)) {
			permsExist = false; 
			logger.info("custom", "Cannot hook in to a permissions system "); 
		}
		else {
			permsExist = true; 
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = sm.getRegistration(net.milkbowl.vault.permission.Permission.class); 
			perms = rsp.getProvider(); 
			logger.info("custom", "Hooked in to vault for a permissions system "); 
		}
	}
	
	private void setupPlayerPoints(PluginManager pm) {
		Plugin playerPoints = pm.getPlugin("PlayerPoints"); 
		if (playerPoints == null) {
			playerPointsExists = false; 
			logger.info("custom", "Cannot hook in to PlayerPoints "); 
		}
		else {
			playerPointsExists = true; 
			playerPointsPlugin = PlayerPoints.class.cast(playerPoints); 
			logger.info("custom", "Hooked in to PlayerPoints for players' points"); 
		}
	}
	
	public void saveFiles() {
		yc.save(); 
		yd.save(); 
		ys.save(); 
	}
	
	@Override
	public void saveConfig() {
		this.saveFiles(); 
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("promote")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					ua.promoteCommand(sender, ((Player)sender).getName());
				}
				else {
					logger.messageSender(sender, "wrongarrangement", ""); 
				}
			}
			else if (args.length == 1) {
				ua.promoteCommand(sender, args[0]); 
			}
			else {
				logger.messageSender(sender, "help", null); 
				return false; 
			}
		}
		else if (commandLabel.equalsIgnoreCase("promoteoneself")) {
			ch.helpPages(sender); 
		}
		else if (commandLabel.equalsIgnoreCase("prom")) {
			if (args.length < 1) {
				ch.helpPages(sender); 
				return false; 
			}
			else if (args[0].equalsIgnoreCase("version")) {
				if (args.length == 1) {
					sender.sendMessage(getDescription().getName() + " is version " + getDescription().getVersion()); 
				}
				else {
					ch.helpPages(sender); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("help")) {
				if (args.length == 1) {
					ch.helpPages(sender); 
				}
				else if (args.length == 2) {
					int page = 0; 
					try {
						page = Integer.parseInt(args[1]); 
					}
					catch (NumberFormatException e) {
						logger.messageSender(sender, "numbererror", args[1]); 
						return false; 
					}
					ch.helpPages(sender, page); 
				}
				else if (args.length == 3) {
					ch.helpCommand(sender, args[1], args[2]); 
				}
				else if (args.length == 4) {
					int page = 0; 
					try {
						page = Integer.parseInt(args[3]); 
					}
					catch (NumberFormatException e) {
						logger.messageSender(sender, "numbererror", args[3]); 
						return false; 
					}
					ch.helpCommand(sender, args[1], args[2], page); 
				}
				else {
					ch.helpPages(sender); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("update")) { 
				if (args.length >= 1 && args.length <= 4) { 
					ua.updatePlayer(sender, args); 
				} 
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("password")) {
				if (args.length == 3 || args.length == 4 || args.length == 5) {
					ua.password(sender, args); 
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("check")) {
				if (args.length == 1 || args.length == 2 || args.length == 3) {
					cp.checkPlayer(sender, args); 
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("list")) {
				if (args.length == 2) {
					ua.listValues(sender, args); 
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else {
				logger.messageSender(sender, "help", null); 
				return false; 
			}
		}
		else if (commandLabel.equalsIgnoreCase("posset")) {
			if (args.length < 1) {
				ch.helpPages(sender); 
				return false; 
			}
			else if (args[0].equalsIgnoreCase("set")) {
				if ((args.length == 4) || (args.length == 5) || (args.length == 6)) {
					cp.setObjects(sender, args); 
				}
				else if (args.length >= 3) {
					if (args[1].equalsIgnoreCase("command")) {
						cp.setObjects(sender, args);
					}
					else {
						logger.messageSender(sender, "help", null); 
						return false; 
					}
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("player")) {
				if (args.length == 2 || args.length == 3) {
					cp.playerAddRemove(sender, args); 
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("exempt")) {
				if (args.length == 3) {
					cp.exemptPlayer(sender, args); 
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				}
			}
			else if (args[0].equalsIgnoreCase("reload")) {
				if (args.length == 1) {
					if (sender.hasPermission("pos.reload.check")) {
						YamlFiles.reloadTheConfiguration(this.yc, this.yd, this.ys); 
					}
					else {
						logger.messageSender(sender, "nopermission", null); 
					}
				}
				else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("check")) {
						if (sender.hasPermission("pos.reload.check")) {
							YamlFiles.reloadTheConfiguration(this.yc, this.yd, this.ys); 
						}
						else {
							logger.messageSender(sender, "nopermission", null); 
						}
					}
					else if (args[1].equalsIgnoreCase("nocheck")) {
						if (sender.hasPermission("pos.reload.nocheck")) {
							Boolean loadErrorFree = true; 
							yc.configuration = YamlFiles.loadAConfiguration(yc.theOutFile); 
							yd.configuration = YamlFiles.loadAConfiguration(yd.theOutFile); 
							ys.configuration = YamlFiles.loadAConfiguration(ys.theOutFile); 
							loadErrorFree = loadErrorFree && yc.checkConfiguration();
							loadErrorFree = loadErrorFree && yd.checkConfiguration();
							loadErrorFree = loadErrorFree && ys.checkConfiguration();
							MyPlayerListener.updateCommandsList(); 
							YamlFiles.alwaysSaveFiles = yc.configuration.getBoolean("alwaysSaveFiles"); 
							if (loadErrorFree == true) {
								logger.broadcastMessageBukkit(getDescription().getName() + " configuration reloaded ");
							}
							else {
								logger.broadcastMessageBukkit(ChatColor.RED + getDescription().getName() + " configuration reloaded, but there were errors "); 
								logger.warning("configparseerroranonymous", "");
							}
						}
						else {
							logger.messageSender(sender, "nopermission", null); 
						}
					}
					else {
						logger.messageSender(sender, "help", null); 
						return false; 
					}
				}
				else {
					logger.messageSender(sender, "help", null); 
					return false; 
				} 
			}
			else if (args[0].equalsIgnoreCase("save")) {
				if (sender.hasPermission("pos.save")) {
					saveFiles(); 
					logger.broadcastMessageServer(sender, "The PromoteOneself config has been saved "); 
				}
				else {
					logger.messageSender(sender, "nopermission", null); 
				}
			}
			else {
				logger.messageSender(sender, "help", null); 
				return false; 
			}
		}
		return true;
	}
}
