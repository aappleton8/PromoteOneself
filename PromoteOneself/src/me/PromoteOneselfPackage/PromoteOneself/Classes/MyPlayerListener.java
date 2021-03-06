package me.PromoteOneselfPackage.PromoteOneself.Classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyPlayerListener implements Listener{ 
	private static PromoteOneselfMainClass plugin; 
	private static UpdateAims ua; 
	private static List<String> commands = new ArrayList<String>(); 
	public MyPlayerListener(PromoteOneselfMainClass instance, UpdateAims uai) {
		plugin = instance; 
		ua = uai; 
		updateCommandsList(); 
	}
	
	public static void updateCommandsList() {
		if (plugin.yc.configuration.getBoolean("watchCommands") == true) {
			commands = plugin.yc.configuration.getStringList("commands"); 
		}
		plugin.logger.info("custom", "Commands being watched: " + commands.toString()); 
	}
	public static void updateWatchedCommands() {
		Set<String> aims = plugin.yc.configuration.getConfigurationSection("aims").getKeys(false); 
		Set<String> commandAims = new HashSet<String>(); 
		for (String i : aims) {
			if (plugin.yc.configuration.getString("aims." + i + ".type").equalsIgnoreCase("command")) {
				commandAims.add(plugin.yc.configuration.getString("aims." + i + ".achieve")); 
			}
		}
		List<String> commandsList = new ArrayList<String>(commandAims); 
		plugin.yc.configuration.set("commands", commandsList); 
		plugin.yc.save(); 
		updateCommandsList(); 
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR) 
	public void onPlayerJoin(PlayerJoinEvent event) {
		UUID rpId = event.getPlayer().getUniqueId(); 
		String spId = rpId.toString(); 
		Boolean updateUsername = plugin.yc.configuration.getBoolean("updateUsernames"); 
		String defaultFirstTarget = plugin.yc.configuration.getString("defaultTarget"); 
		Boolean exempt = false; 
		if (plugin.yd.configuration.contains("exempt." + spId) == true) {
			if (plugin.yd.configuration.getString("exempt." + spId + ".exempt").equalsIgnoreCase("true") || plugin.yd.configuration.getString("exempt." + spId + ".exempt").equalsIgnoreCase("temp")) {
				if (updateUsername == true) {
					plugin.yd.configuration.set("exempt." + spId + ".lastUsername", event.getPlayer().getName()); 
					plugin.yd.save(); 
				}
				exempt = true; 
			}
			else {
				plugin.yd.configuration.set("exempt." + spId, null); 
				if (plugin.yd.configuration.contains("players." + spId) == false) {
					ua.addPlayer(defaultFirstTarget, spId, rpId, true); 
				}
				plugin.yd.save(); 
			}
		}
		if (plugin.yd.configuration.contains("players." + spId)) {
			if (updateUsername == true) {
				plugin.yd.configuration.set("players." + spId + ".lastUsername", event.getPlayer().getName()); 
				plugin.yd.save(); 
			}
		}
		else if (plugin.yc.configuration.getBoolean("startInPromotionTree") == true) {
			ua.addPlayer(defaultFirstTarget, spId, rpId, true); 
		}
		if ((exempt == false) && plugin.yc.configuration.getBoolean("remindOnJoin")) {
			if (plugin.yd.configuration.contains("players." + spId) == false) {
				Bukkit.getPlayer(rpId).sendMessage(ChatColor.AQUA + plugin.logger.getName(true) + "You are not in the promotion tree "); 
			}
			else if (plugin.yd.configuration.getBoolean("players." + spId + ".finished") == false) {
				String target = plugin.yd.configuration.getString("players." + spId + ".target"); 
				Bukkit.getPlayer(rpId).sendMessage(ChatColor.AQUA + plugin.logger.getName(true) + "Your current target (" + target + ") requires the aims: " + plugin.yc.configuration.getStringList("targets." + target + ".aims"));
			}
			else if (plugin.yd.configuration.getBoolean("players." + spId + ".finished") == true) {
				Bukkit.getPlayer(rpId).sendMessage(ChatColor.GREEN + plugin.logger.getName(true) + "You have obtained the highest self-promotion possible. "); 
			}
			else {
				plugin.logger.warning("badfinished", Bukkit.getPlayer(rpId).getName()); 
				plugin.logger.messageSender(Bukkit.getPlayer(rpId), "badfinished", null); 
			}
		}
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR) 
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.yc.configuration.getBoolean("detectKills") == true) {
			if (event.getEntity().getKiller() instanceof Player) {
				try {
					UUID rpId = event.getEntity().getKiller().getUniqueId(); 
					String spId = rpId.toString(); 
					if (plugin.yd.configuration.contains("players." + spId)) {
						int kills = plugin.yd.configuration.getInt("players." + spId + ".data.kills"); 
						kills += 1; 
						plugin.yd.configuration.set("players." + spId + ".data.kills", kills); 
						plugin.saveFiles(); 
					}
				}
				catch (Exception e) {
					plugin.logger.warning("custom", "Player death error: " + e.toString()); 
					plugin.logger.exception("death of player error: ", e); 
				}
			}
		}
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR) 
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (plugin.yc.configuration.getBoolean("watchCommands") == true) {
			if ((commands.isEmpty() == false) || ((commands.size() > 0) && (!(commands.get(0).equalsIgnoreCase("none"))))) {
				for (String i : commands) {
					if ((event.getMessage().startsWith("/" + i) == true) || (event.getMessage().startsWith(i) == true)) {
						UUID rpId = event.getPlayer().getUniqueId(); 
						String spId = rpId.toString(); 
						for (String j : plugin.yd.configuration.getStringList("players." + spId + ".data.commands")) {
							if ((plugin.yc.configuration.getString("aims." + j + ".achieve").equalsIgnoreCase(i)) || (("/" + plugin.yc.configuration.getString("aims." + j + ".achieve")).equalsIgnoreCase(i))) {
								plugin.yd.configuration.set("players." + spId + ".aims." + j, true); 
								event.getPlayer().sendMessage("You have now achieved aim " + j + " "); 
								plugin.saveFiles(); 
							}
						}
					}
				}
			}
		}
	}
}
