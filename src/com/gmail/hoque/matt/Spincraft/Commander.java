/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.hoque.matt.Spincraft;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Owner
 */
public class Commander implements CommandExecutor {

    Spincraft plugin;

    public Commander(Spincraft pl) {
        plugin = pl;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("spin")) {
            ifas(sender, cmd, label, args);
        }
        return false;
    }

    public void ifas(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 2) {
            if (args[0].equalsIgnoreCase("give")) {
                if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                    int amount = Integer.parseInt(args[2]);
                    ItemStack item = new ItemStack(Material.EMERALD, amount);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.YELLOW + "Spincraft Token");
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(plugin.timeToHidden(System.currentTimeMillis()) + "§~" + plugin.nameToHidden(args[1]) + "§`" + ChatColor.GREEN + "Thank You!");
                    meta.setLore(loreList);
                    item.setItemMeta(meta);

                    String special;

                    if (plugin.getServer().getOnlinePlayers().contains(plugin.getServer().getPlayer(args[1]))
                            && plugin.getServer().getPlayer(args[1]).getInventory().firstEmpty() != -1) {
                        plugin.getServer().getPlayer(args[1]).getInventory().addItem(item);//FLAG getLogger().info qeqeqeqe FIX   
                        special = "Delivered";
                    } else {
                        if (!plugin.recover.containsKey(plugin.fetchUUIDS(args[1]))) {
                            plugin.recover.put(plugin.fetchUUIDS(args[1]), new ArrayList<Recover>());
                        } else if (plugin.recover.get(plugin.fetchUUIDS(args[1])) == null) {
                            plugin.recover.put(plugin.fetchUUIDS(args[1]), new ArrayList<Recover>());
                        }
                        ArrayList<Recover> tempRec = plugin.recover.get(plugin.fetchUUIDS(args[1]));
                        tempRec.add(new Recover(item));
                        plugin.recover.put(plugin.fetchUUIDS(args[1]), tempRec);
                        plugin.updateRecover(plugin.recover);
                        special = "ToRecovery";
                    }
                    plugin.formatToLog(item, special, args[1], "ordered", amount);
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("flag")) {
                if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                    if (plugin.getServer().getOnlinePlayers().contains(plugin.getServer().getPlayer(args[1])) || args[1].equalsIgnoreCase("next")) {
                        if (args[1].equalsIgnoreCase("next")) {
                            plugin.upNext = true;
                            sender.sendMessage(ChatColor.GOLD + "Next game has been flagged.");
                        } else {
                            plugin.playerUpNext.put(args[1], true);
                            sender.sendMessage(ChatColor.GOLD + "Next game by " + args[1] + " has been flagged.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "That player is not online!");
                    }

                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                }
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("register")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                        sender.sendMessage(ChatColor.BLUE + "Left click a SIGN first, then the block. Must have 1 WoodPickaxe in hand.");
                        sender.sendMessage(ChatColor.DARK_RED + "Only 1 person can be registering a sign SERVERWIDE at any given moment");
                        plugin.registering.put(sender.getName(), 1);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("display")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                        sender.sendMessage(ChatColor.BLUE + "Left click a transaction sign first, then the announcement sign. Must have 1 WoodPickaxe in hand.");
                        sender.sendMessage(ChatColor.DARK_RED + "Only 1 person can be registering ANYTHING SERVERWIDE at any given moment");
                        plugin.registering.put(sender.getName(), 5);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                        sender.sendMessage(ChatColor.BLUE + "Left click a transaction sign with wooden pickaxe in hand to remove all connections");
                        plugin.registering.put(sender.getName(), 4);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("check")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                        sender.sendMessage(ChatColor.BLUE + "Left click a transaction sign with wooden pickaxe in hand to check for connections");
                        plugin.registering.put(sender.getName(), 3);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("recover")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("spincraft.player") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                        Player p = Bukkit.getServer().getPlayer(sender.getName());
                        if (!plugin.recover.containsKey(p.getUniqueId() + "")) {
                            sender.sendMessage("There are no items in your recovery box.");
                        } else if (plugin.recover.get(p.getUniqueId().toString()) == null) {
                            sender.sendMessage("There are no items in your recovery box.");
                        } else {
                            int loop = plugin.recover.get(p.getUniqueId().toString()).size();
                            for (int j = 0; j < loop; j++) {
                                Recover toRemove = plugin.recover.get(p.getUniqueId().toString()).get(0);
                                ItemStack i = toRemove.getItem();
                                if (p.getInventory().firstEmpty() == -1) {
                                    sender.sendMessage(ChatColor.RED + "NO INVENTORY SPACE, CLEAR INVENTORY AND TRY AGAIN");
                                    j = loop;
                                } else {
                                    p.getInventory().addItem(i);
                                    plugin.recover.get(p.getUniqueId().toString()).remove(toRemove);
                                }
                            }
                            if (plugin.recover.get(p.getUniqueId().toString()).size() == 0) {
                                plugin.recover.remove(p.getUniqueId().toString());
                            }
                            plugin.updateRecover(plugin.recover);
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                if (sender instanceof Player) {
                    String[] message2 = new String[]{ChatColor.BLUE + "" + ChatColor.MAGIC + "AAAAAA" + ChatColor.BLUE + "===============" + ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + "===============" + ChatColor.MAGIC + "AAAAAA",
                        ChatColor.GOLD + "Version: " + plugin.version,
                        ChatColor.GOLD + "Author: Portalz",
                        ChatColor.GOLD + "for command list, type \"/spin\""
                    };
                    for (String s : message2) {
                        sender.sendMessage(s);
                    }
                }
            }
        } else if (args.length == 0) {
            String[] message = new String[]{ChatColor.BLUE + "============" + ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + "=============",
                ChatColor.GOLD + "/spin recover - Recovers Lost Items",
                ChatColor.GOLD + "/spin info - plugin info"};
            for (String s : message) {
                sender.sendMessage(s);
            }
            if (sender.hasPermission("spincraft.admin") || sender.isOp() || sender.getName().equals(plugin.pones) || sender.getName().equals(plugin.ptwos)) {
                String[] message2 = new String[]{ChatColor.BLUE + "==========" + ChatColor.GOLD + "[Admin Commands]" + ChatColor.BLUE + "==========",
                    ChatColor.GOLD + "NOTICE: You will need a wooden pickaxe while managing modules",
                    ChatColor.GOLD + "/spin register - new spincraft module",
                    ChatColor.GOLD + "/spin display - create a display",
                    ChatColor.GOLD + "/spin remove - clear spincraft module",
                    ChatColor.GOLD + "/spin check - check if transactionBlock",
                    ChatColor.GOLD + "/spin give player amount - give Tokens",
                    ChatColor.GOLD + "/spin flag <player> - flag next spin transaction"};
                for (String s : message2) {
                    sender.sendMessage(s);
                }
            }
        }
    }

}
