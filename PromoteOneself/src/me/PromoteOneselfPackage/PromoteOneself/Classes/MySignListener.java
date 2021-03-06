package me.PromoteOneselfPackage.PromoteOneself.Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MySignListener implements Listener {
	private static PromoteOneselfMainClass plugin; 
	private static LoggingClass logger; 
	private static UpdateAims ua; 
	
	private static final String firstLine = "[pos]"; 
	
	public MySignListener(PromoteOneselfMainClass instance, LoggingClass log, UpdateAims uainstance) {
		plugin = instance; 
		logger = log; 
		ua = uainstance; 
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) 
	public void onBlockPlace(BlockPlaceEvent event) {
		if ((event.getBlockAgainst().getType() == Material.SIGN) || (event.getBlockAgainst().getType() == Material.WALL_SIGN) || (event.getBlockAgainst().getType() == Material.SIGN_POST)) {
			if (event.getBlockAgainst().getState() instanceof Sign) {
				Sign sign = (Sign) event.getBlockAgainst().getState(); 
				if (sign.getLine(0).equalsIgnoreCase(firstLine)) {
					if (event.getPlayer().isSneaking() == false) {
						event.setCancelled(true); 
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) 
	public void onSignChange(SignChangeEvent event) {
		if (plugin.yc.configuration.getBoolean("allowSigns") == true) {
			if (event.getLine(0).equalsIgnoreCase(firstLine)) {
				if (event.getLine(1).equalsIgnoreCase("update")) {
					if (event.getPlayer().hasPermission("pos.sign.update.create")) {
					}
					else {
						logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
						event.setCancelled(true); 
					}
				}
				else if (event.getLine(1).equalsIgnoreCase("points")) {
					if (event.getPlayer().hasPermission("pos.sign.points.create")) {
					}
					else {
						logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
						event.setCancelled(true); 
					}
				}
				else if (event.getLine(1).equalsIgnoreCase("target")) {
					if (event.getPlayer().hasPermission("pos.sign.target.create")) {
					}
					else {
						logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
						event.setCancelled(true); 
					}
				}
				else if (event.getLine(1).equalsIgnoreCase("sign")) {
					if (event.getPlayer().hasPermission("pos.sign.sign.create")) {
					}
					else {
						logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
						event.setCancelled(true); 
					}
				}
				else if (event.getLine(1).equalsIgnoreCase("aim")) {
					if (event.getPlayer().hasPermission("pos.sign.aim.create")) {
					}
					else {
						logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
						event.setCancelled(true); 
					}
				}
				else {
					logger.messageSender(event.getPlayer(), "sign", null); 
					event.setCancelled(true); 
				}
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) 
	public void onBlockBreak(BlockBreakEvent event) {
		if (plugin.yc.configuration.getBoolean("allowSigns") == true) {
			if (event.getBlock() instanceof Sign) {
				Sign sign = (Sign) event.getBlock(); 
				if (sign.getLine(0).equalsIgnoreCase(firstLine)) {
					if (sign.getLine(1).equalsIgnoreCase("update")) {
						if (event.getPlayer().hasPermission("pos.sign.update.delete")) {
						}
						else {
							logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
							event.setCancelled(true); 
						}
					}
					else if (sign.getLine(1).equalsIgnoreCase("points")) {
						if (event.getPlayer().hasPermission("pos.sign.points.delete")) {
						}
						else {
							logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
							event.setCancelled(true); 
						}
					}
					else if  (sign.getLine(1).equalsIgnoreCase("target")) {
						if (event.getPlayer().hasPermission("pos.sign.target.delete")) {
						}
						else {
							logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
							event.setCancelled(true); 
						}
					}
					else if (sign.getLine(1).equalsIgnoreCase("sign")) {
						if (event.getPlayer().hasPermission("pos.sign.sign.delete")) {
						}
						else {
							logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
							event.setCancelled(true); 
						}
					}
					else if (sign.getLine(1).equalsIgnoreCase("aim")) {
						if (event.getPlayer().hasPermission("pos.sign.aim.delete")) {
						}
						else {
							logger.messageSender(event.getPlayer(), "nopermissionsign", null); 
							event.setCancelled(true); 
						}
					}
				}
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) 
	public void onPlayerInteract(PlayerInteractEvent event)  {
		if (plugin.yc.configuration.getBoolean("allowSigns") == true) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState(); 
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (sign.getLine(0).equalsIgnoreCase(firstLine)) {
						Player player = event.getPlayer(); 
						UUID rpId = player.getUniqueId(); 
						String spId = rpId.toString(); 
						Set<String> listedSignIds = Collections.emptySet(); 
						Boolean areSigns = false; 
						if (plugin.ys.configuration.contains("signs")) {
							try {
								areSigns = true; 
								listedSignIds = plugin.ys.configuration.getConfigurationSection("signs").getKeys(false); 
							}
							catch (NullPointerException e2) {
								// No action required 
							}
						}
						else {
							logger.warning("missingkey", "signs key from signs.yml"); 
						}
						String signId = sign.getLine(3); 
						if (sign.getLine(1).equalsIgnoreCase("update")) {
							if (event.getPlayer().hasPermission("pos.sign.update.use")) {
								if (signId == null || signId.equalsIgnoreCase("")) {
									if (player.hasPermission("pos.sign.noid")) {
										ua.updatePlayer(player, new String[] {"update", sign.getLine(2)}, true); 
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else if (player.hasPermission("pos.sign.limitexempt.*") || player.hasPermission("pos.sign.limitexempt." + signId)) {
									if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
										ua.updatePlayer(player, new String[] {"update", sign.getLine(2)}, true); 
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else if (listedSignIds.contains(signId)) {
									if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
										try {
											int playerUsage = plugin.yd.configuration.getInt("players." + spId + ".data.signs." + signId); 
											if (playerUsage < plugin.ys.configuration.getInt("signs." + signId + ".usage") || plugin.ys.configuration.getInt("signs." + signId + ".usage") == -1) {
												ua.updatePlayer(player, new String[] {"update", sign.getLine(2)}, true); 
												playerUsage += 1; 
												plugin.yd.configuration.set("players." + spId + ".data.signs." + signId, playerUsage); 
												plugin.yd.save(); 
											}
											else {
												player.sendMessage(ChatColor.RED + "Could not update target: you have run out of sign usage allowance for this sign "); 
												event.setCancelled(true); 
											}
										}
										catch (NullPointerException e) {
											player.sendMessage(ChatColor.RED + "Could not update target: null pointer exception "); 
											event.setCancelled(true); 
										}
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else {
									logger.warning("custom", "A sign of type update has an invalid id of: " + signId); 
									player.sendMessage(ChatColor.RED + "The sign has an invalid sign id "); 
									event.setCancelled(true); 
								}
							}
							else {
								logger.messageSender(player, "nopermission", null); 
								event.setCancelled(true); 
							}
						}
						else if (sign.getLine(1).equalsIgnoreCase("points")) {
							if (player.hasPermission("pos.sign.points.use")) {
								if (signId == null || signId.equalsIgnoreCase("")) {
									if (player.hasPermission("pos.sign.noid")) {
										try {
											int signPoints = Integer.parseInt(sign.getLine(2)); 
											int playerPoints = plugin.yd.configuration.getInt("players." + spId + ".data.points"); 
											playerPoints += signPoints; 
											plugin.yd.configuration.set("players." + spId + ".data.points", playerPoints); 
											player.sendMessage("You now have " + sign.getLine(2) + " more points "); 
											plugin.saveFiles(); 
										}
										catch (NumberFormatException e) {
											player.sendMessage(ChatColor.RED + "The number of points to add must be an integer "); 
											logger.warning("custom", "The number of points must be an integer even though it is not on a sign or a player "); 
										}
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else if (player.hasPermission("pos.sign.limitexempt.*") || player.hasPermission("pos.sign.limitexempt." + signId)) {
									if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
										try {
											int signPoints = Integer.parseInt(sign.getLine(2)); 
											int playerPoints = plugin.yd.configuration.getInt("players." + spId + ".data.points"); 
											playerPoints += signPoints; 
											plugin.yd.configuration.set("players." + spId + ".data.points", playerPoints); 
											player.sendMessage("You now have " + sign.getLine(2) + " more points "); 
											plugin.saveFiles(); 
										}
										catch (NumberFormatException e) {
											player.sendMessage(ChatColor.RED + "The number of points to add must be an integer "); 
											logger.warning("custom", "The number of points must be an integer even though it is not on a sign or a player "); 
										}
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else if(listedSignIds.contains(signId))  {
									if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
										try {
											int playerUsage = plugin.yd.configuration.getInt("players." + spId + ".data.signs." + signId); 
											if (playerUsage < plugin.ys.configuration.getInt("signs." + signId + ".usage") || plugin.ys.configuration.getInt("signs." + signId + ".usage") == -1) {
												try {
													int signPoints = Integer.parseInt(sign.getLine(2)); 
													int playerPoints = plugin.yd.configuration.getInt("players." + spId + ".data.points"); 
													playerPoints += signPoints; 
													plugin.yd.configuration.set("players." + spId + ".data.points", playerPoints); 
													player.sendMessage("You now have " + sign.getLine(2) + " more points "); 
													playerUsage += 1; 
													plugin.yd.configuration.set("players." + spId + ".data.signs." + signId, playerUsage); 
													plugin.saveFiles(); 
												}
												catch (NumberFormatException e) {
													player.sendMessage(ChatColor.RED + "The number of points to add must be an integer "); 
													logger.warning("custom", "The number of points must be an integer even though it is not on a sign or a player "); 
												}
											}
											else {
												player.sendMessage(ChatColor.RED + "Could not update points: you have run out of sign usage allowance for this sign "); 
												event.setCancelled(true); 
											}
										}
										catch (NullPointerException e) {
											player.sendMessage(ChatColor.RED + "Could not update points: null pointer exception "); 
											event.setCancelled(true); 
										}
									}
									else {
										event.setCancelled(true); 
										logger.messageSender(player, "nopermission", null); 
									}
								}
								else {
									logger.warning("custom", "A sign of type points has an invalid id of: " + signId); 
									player.sendMessage(ChatColor.RED + "The sign has an invalid sign id "); 
									event.setCancelled(true); 
								}
							}
							else {
								event.setCancelled(true); 
								logger.messageSender(player, "nopermission", null); 
							}
						}
						else if (sign.getLine(1).equalsIgnoreCase("target")) {
							if (player.hasPermission("pos.sign.target.use")) {
								String target = sign.getLine(2); 
								if (plugin.yc.configuration.contains("targets." + target)) {
									List<String> signIds = new ArrayList<String>(); 
									signIds.addAll(listedSignIds); 
									if (signId == null || signId.equalsIgnoreCase("")) {
										if (player.hasPermission("pos.sign.noid")) {
											plugin.yd.configuration.set("players." + spId + ".target", target); 
											plugin.saveFiles(); 
											YamlFiles.updatePlayerTargets(spId, plugin.yc, plugin.yd, plugin.ys, areSigns, signIds); 
											player.sendMessage("Your target is now " + target + " "); 
											plugin.saveFiles(); 
										}
										else {
											event.setCancelled(true); 
											logger.messageSender(player, "nopermission", null); 
										}
									}
									else if (player.hasPermission("pos.sign.limitexempt.*") || player.hasPermission("pos.sign.limitexempt." + signId)) {
										if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
											plugin.yd.configuration.set("players." + spId + ".target", target); 
											plugin.saveFiles(); 
											YamlFiles.updatePlayerTargets(spId, plugin.yc, plugin.yd, plugin.ys, areSigns, signIds); 
											player.sendMessage("Your target is now " + target + " "); 
											plugin.saveFiles(); 
										}
										else {
											event.setCancelled(true); 
											logger.messageSender(player, "nopermission", null); 
										}
									}
									else if(listedSignIds.contains(signId))  {
										if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
											try {
												int playerUsage = plugin.yd.configuration.getInt("players." + spId + ".data.signs." + signId); 
												if (playerUsage < plugin.ys.configuration.getInt("signs." + signId + ".usage") || plugin.ys.configuration.getInt("signs." + signId + ".usage") == -1) {
													plugin.yd.configuration.set("players." + spId + ".target", target); 
													plugin.saveFiles(); 
													YamlFiles.updatePlayerTargets(spId, plugin.yc, plugin.yd, plugin.ys, areSigns, signIds); 
													player.sendMessage("Your target is now " + target + " "); 
													playerUsage += 1; 
													plugin.yd.configuration.set("players." + spId + ".data.signs." + signId, playerUsage); 
													plugin.saveFiles(); 
												}
												else {
													player.sendMessage(ChatColor.RED + "Could not update target: you have run out of sign usage allowance for this sign "); 
													event.setCancelled(true); 
												}
											}
											catch (NullPointerException e) {
												player.sendMessage(ChatColor.RED + "Could not update target: null pointer exception "); 
												event.setCancelled(true); 
											}
										}
										else {
											event.setCancelled(true); 
											logger.messageSender(player, "nopermission", null); 
										}
									}
									else {
										logger.warning("custom", "A sign of type target has an invalid id of: " + signId); 
										player.sendMessage(ChatColor.RED + "The sign has an invalid sign id "); 
										event.setCancelled(true); 
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "There is no target with the name " + target + " "); 
									logger.warning("custom", "There is a sign using the target " + target + " even though there is no such target "); 
								}
							}
							else {
								event.setCancelled(true); 
								logger.messageSender(player, "nopermission", null); 
							}
						}
						else if (sign.getLine(1).equalsIgnoreCase("aim")) {
							if (player.hasPermission("pos.sign.aim.use")) {
								Boolean changed = false; 
								String aim = sign.getLine(2); 
								Set<String> rawPlayerAims = plugin.yd.configuration.getConfigurationSection("players." + spId + ".aims").getKeys(false); 
								List<String> playerAims = new ArrayList<String>(rawPlayerAims); 
								for (String i : playerAims) {
									if (aim.equalsIgnoreCase(i)) {
										changed = true; 
										if (signId == null || signId.equalsIgnoreCase("")) {
											if (player.hasPermission("pos.sign.noid")) {
												plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
												player.sendMessage("your aim " + aim + " is now completed "); 
											}
											else {
												event.setCancelled(true); 
												logger.messageSender(player, "nopermission", null); 
											}
										}
										else if (player.hasPermission("pos.sign.limitexempt.*") || player.hasPermission("pos.sign.limitexempt." + signId)) {
											if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
												plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
												player.sendMessage("your aim " + aim + " is now completed "); 
											}
											else {
												event.setCancelled(true); 
												logger.messageSender(player, "nopermission", null); 
											}
										}
										else if(listedSignIds.contains(signId))  {
											if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
												try {
													int playerUsage = plugin.yd.configuration.getInt("players." + spId + ".data.signs." + signId); 
													if (playerUsage < plugin.ys.configuration.getInt("signs." + signId + ".usage") || plugin.ys.configuration.getInt("signs." + signId + ".usage") == -1) {
														plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
														player.sendMessage("your aim " + aim + " is now completed "); 
														playerUsage += 1; 
														plugin.yd.configuration.set("players." + spId + ".data.signs." + signId, playerUsage); 
													}
													else {
														player.sendMessage(ChatColor.RED + "Could not update aim: you have run out of sign usage allowance for this sign "); 
														event.setCancelled(true); 
													}
												}
												catch (NullPointerException e) {
													player.sendMessage(ChatColor.RED + "Could not update aim: null pointer exception "); 
													event.setCancelled(true); 
												}
											}
											else {
												event.setCancelled(true); 
												logger.messageSender(player, "nopermission", null); 
											}
										}
										else {
											logger.warning("custom", "A sign of type sign has an invalid id of: " + signId); 
											player.sendMessage(ChatColor.RED + "The sign has an invalid sign id "); 
											event.setCancelled(true); 
										}
									}
								}
								if (changed == false) {
									player.sendMessage(ChatColor.RED + "The aim " + aim +" is not one that you are working towards "); 
								}
								plugin.saveFiles(); 
							}
							else {
								event.setCancelled(true); 
								logger.messageSender(player, "nopermission", null); 
							}
						}
						else if (sign.getLine(1).equalsIgnoreCase("sign")) {
							if (player.hasPermission("pos.sign.sign.use")) {
								Boolean changed = false; 
								String aim = sign.getLine(2); 
								if (plugin.yc.configuration.contains("aims." + aim + ".type")) {
									String aimType = plugin.yc.configuration.getString("aims." + aim + ".type"); 
									if (aimType.equalsIgnoreCase("sign")) {
										Set<String> rawPlayerAims = plugin.yd.configuration.getConfigurationSection("players." + spId + ".aims").getKeys(false); 
										List<String> playerAims = new ArrayList<String>(rawPlayerAims); 
										for (String i : playerAims) {
											if (aim.equalsIgnoreCase(i)) {
												changed = true; 
												if (signId == null || signId.equalsIgnoreCase("")) {
													if (player.hasPermission("pos.sign.noid")) {
														plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
														player.sendMessage("your aim " + aim + " is now completed "); 
													}
													else {
														event.setCancelled(true); 
														logger.messageSender(player, "nopermission", null); 
													}
												}
												else if (player.hasPermission("pos.sign.limitexempt.*") || player.hasPermission("pos.sign.limitexempt." + signId)) {
													if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
														plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
														player.sendMessage("your aim " + aim + " is now completed "); 
													}
													else {
														event.setCancelled(true); 
														logger.messageSender(player, "nopermission", null); 
													}
												}
												else if(listedSignIds.contains(signId))  {
													if (player.hasPermission("pos.sign.id.*") || player.hasPermission("pos.sign.id." + signId)) {
														try {
															int playerUsage = plugin.yd.configuration.getInt("players." + spId + ".data.signs." + signId); 
															if (playerUsage < plugin.ys.configuration.getInt("signs." + signId + ".usage") || plugin.ys.configuration.getInt("signs." + signId + ".usage") == -1) {
																plugin.yd.configuration.set("players." + spId + ".aims." + aim, true); 
																player.sendMessage("your aim " + aim + " is now completed "); 
																playerUsage += 1; 
																plugin.yd.configuration.set("players." + spId + ".data.signs." + signId, playerUsage); 
															}
															else {
																player.sendMessage(ChatColor.RED + "Could not update aim: you have run out of sign usage allowance for this sign "); 
																event.setCancelled(true); 
															}
														}
														catch (NullPointerException e) {
															player.sendMessage(ChatColor.RED + "Could not update aim: null pointer exception "); 
															event.setCancelled(true); 
														}
													}
													else {
														event.setCancelled(true); 
														logger.messageSender(player, "nopermission", null); 
													}
												}
												else {
													logger.warning("custom", "A sign of type sign has an invalid id of: " + signId); 
													player.sendMessage(ChatColor.RED + "The sign has an invalid sign id "); 
													event.setCancelled(true); 
												}
											}
										}
										if (changed == false) {
											player.sendMessage(ChatColor.RED + "The aim " + aim +" is not one that you are working towards "); 
										}
										plugin.saveFiles(); 
									}
									else {
										logger.messageSender(player, "custom", ChatColor.RED + "That type of sign can only be used to achieve 'sign' type aims but the specified aim is a " + aimType + " type aim "); 
									}
								}
								else {
									logger.messageSender(player, "custom", ChatColor.RED + "The aim specified by this sign cannot be found in the config.yml file "); 
								}
							}
							else {
								event.setCancelled(true); 
								logger.messageSender(player, "nopermission", null); 
							}
						}
					}
				}
			}
		}
	}
}
