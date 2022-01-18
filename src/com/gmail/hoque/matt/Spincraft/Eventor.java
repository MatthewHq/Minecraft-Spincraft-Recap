package com.gmail.hoque.matt.Spincraft;

import java.util.ArrayList;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Lever;

public class Eventor
        implements Listener {

    Spincraft plugin;
    String enchantGlobal;

    public Eventor(Spincraft instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.recover.containsKey(event.getPlayer().getUniqueId().toString())
                && plugin.recover.get(event.getPlayer().getUniqueId().toString()) != null
                && plugin.recover.get(event.getPlayer().getUniqueId().toString()).size() > 0) {
            onJoinNotice(event.getPlayer(), 20);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.WOOD_PICKAXE) {
            plugin.getLogger().info(this.plugin.registering.toString());
            plugin.getLogger().info(event.getPlayer().getName());
            plugin.getLogger().info(this.plugin.registering.get(event.getPlayer().getName()).toString());
            plugin.getLogger().info(event.getClickedBlock().getType().name());
//            plugin.getLogger().info();
//            plugin.getLogger().info();

            if ((this.plugin.registering.get(event.getPlayer().getName()) != null)
                    && (event.getClickedBlock() != null) && ((event.getClickedBlock().getType() == Material.WALL_SIGN) || (event.getClickedBlock().getType() == Material.LEVER))) {
                if ((this.plugin.registering.get(event.getPlayer().getName())) == 1) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    this.plugin.temp1 = sign.locS();
                    this.plugin.registering.put(event.getPlayer().getName(), 2);
                    event.getPlayer().sendMessage(ChatColor.BLUE + "Sign Registered, now select a block");
                } else if ((this.plugin.registering.get(event.getPlayer().getName())) == 3) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    if (this.plugin.pairs.containsKey(sign.locS())) {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "This sign is paired to a block");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "This sign is" + ChatColor.DARK_RED + "NOT" + ChatColor.BLUE + "paired to a block");
                    }
                    if (this.plugin.pairs.containsKey(sign.locS() + "announce")) {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "This sign is paired to an announcement sign");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "This sign is" + ChatColor.DARK_RED + "NOT" + ChatColor.BLUE + "paired to an announcement sign");
                    }
                    Integer put = this.plugin.registering.put(event.getPlayer().getName(), 0);
                } else if ((this.plugin.registering.get(event.getPlayer().getName())) == 4) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    this.plugin.pairs.remove(sign.locS());
                    this.plugin.pairs.remove(sign.locS() + "announce");
                    this.plugin.registering.put(event.getPlayer().getName(), 0);
                    event.getPlayer().sendMessage(ChatColor.BLUE + "Sign Cleared");
                } else if ((this.plugin.registering.get(event.getPlayer().getName())) == 5) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    this.plugin.temp1 = sign.locS() + "announce";
                    this.plugin.registering.put(event.getPlayer().getName(), 6);
                    event.getPlayer().sendMessage(ChatColor.BLUE + "Sign Registered, now select another sign to announce");
                } else if ((this.plugin.registering.get(event.getPlayer().getName())) == 6) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    this.plugin.temp2 = sign;
                    this.plugin.pairs.put(this.plugin.temp1, this.plugin.temp2);
                    this.plugin.registering.put(event.getPlayer().getName(), 0);
                    this.plugin.updatePairs(this.plugin.pairs);
                    event.getPlayer().sendMessage(ChatColor.BLUE + "AnnouncementSign and TransactionSign have been registered");
                }
            } else if ((this.plugin.registering.get(event.getPlayer().getName()) != null) && ((this.plugin.registering.get(event.getPlayer().getName())) == 2)) {
                Blockdress bl = new Blockdress(event.getClickedBlock().getLocation(), event.getBlockFace());
                this.plugin.temp2 = bl;
                this.plugin.pairs.put(this.plugin.temp1, this.plugin.temp2);
                this.plugin.registering.put(event.getPlayer().getName(), 0);
                this.plugin.updatePairs(this.plugin.pairs);
                event.getPlayer().sendMessage(ChatColor.BLUE + "Block and Sign have been registered");
            }
        }
        if ((event.getPlayer().getItemInHand().getType() == Material.EMERALD) && (event.getClickedBlock() != null)
                && (this.plugin.pairs.containsKey(new Blockdress(event.getClickedBlock().getLocation()).locS()))) {
            if ((event.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) && (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Spincraft Token"))) {
                if (event.getPlayer().getInventory().firstEmpty() != -1) {
                    Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
                    if ((this.plugin.spinMap == null) || (!this.plugin.spinMap.containsKey(sign.locS())) || (!(this.plugin.spinMap.get(sign.locS())))) {
                        this.plugin.spinMap.put(sign.locS(), true);
                        Player p = Bukkit.getServer().getPlayer(event.getPlayer().getName());
                        int tokenCount = p.getItemInHand().getAmount();
                        Set<Material> test = null;
                        Block cb = event.getClickedBlock();
                        Blockdress cbbd = new Blockdress(cb.getLocation());
                        Block mb = ((Blockdress) this.plugin.pairs.get(cbbd.locS())).getLocation().getBlock();
                        int[][] w = faceWeights(cbbd);
                        Block[] bl = {mb.getRelative(w[0][0], w[0][1], w[0][2]), mb.getRelative(w[1][0], w[1][1], w[1][2]), mb.getRelative(w[2][0], w[2][1], w[2][2]), mb.getRelative(w[3][0], w[3][1], w[3][2]), mb.getRelative(w[4][0], w[4][1], w[4][2])};
                        
                        ItemStack[] item = new ItemStack[5];
                        for (int i = 0; i < 5; i++) {
                            item[i] = new ItemStack(Material.AIR);
                        }
                        ItemFrame[] x = new ItemFrame[bl.length];
                        boolean check = false;
                        for (int i = 0; i < bl.length; i++) {//flag1here
                            x[i] = getFrame(bl[i].getLocation());
                            if (x[i] == null) {
                                check = true;
                            }
                        }
                        if (check) {
                            plugin.getLogger().info("FALSE");
                            Boolean put = plugin.spinMap.put(sign.locS(), false);
                            return;
                        }
                        ItemStack inHand = p.getInventory().getItemInHand();
                        plugin.formatToLog(inHand, "Removed", p.getName(), "spent", 1);
                        if (tokenCount > 1) {
                            p.getItemInHand().setAmount(tokenCount - 1);
                        } else if (tokenCount == 1) {
                            p.getInventory().removeItem(new ItemStack[]{p.getInventory().getItemInHand()});
                        }
                        //=============SPIN MECHANIC VARIABLES==================
                        int[] slower = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 4, 5, 6, 7, 9, 12, 16, 20, 24, 29, 34, 40, 50};
                        ItemStack[] matCycle = new ItemStack[30];
                        int[] notes = {14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14};
                        //=============SPIN MECHANIC VARIABLES==================

                        //=============ITEM GENERATION AND FORMATTING==================
                        for (int i = 0; i < 30; i++) {
                            ItemStack tempStack = generate();
                            if (tempStack.getType() == Material.ENCHANTED_BOOK) {
                                ItemMeta meta = tempStack.getItemMeta();
                                meta = generateEnchant(meta, tempStack.getAmount());
                                tempStack.setItemMeta(meta);
                            } else {
                                ItemMeta meta = tempStack.getItemMeta();
                                meta.setDisplayName((meta.getDisplayName() != null && meta.getDisplayName().equals("Enchant Token")) ? ("Enchant Token x" + tempStack.getAmount()) : ("x" + tempStack.getAmount()));
                                tempStack.setItemMeta(meta);
                            }
                            matCycle[i] = tempStack;
                        }
                        if (matCycle[27].getType() == Material.ENCHANTED_BOOK) {
                            EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) matCycle[27].getItemMeta();
                            if (enchMeta.hasStoredEnchants()) {
                                Enchantment tempChant = (Enchantment) enchMeta.getStoredEnchants().keySet().toArray()[0];
                                enchantGlobal = plugin.parseEnum(tempChant.getName()) + " " + enchMeta.getStoredEnchantLevel(tempChant);
                            }
                        } else {
                            enchantGlobal = "";
                        }

                        /*  //=============TESTING==================
                         for (int i = 0; i < 1000; i++) {
                         Material mat = generate();
                         ItemStack tempStack = new ItemStack(mat, 1, data);
                         if (tempStack.getType() == Material.ENCHANTED_BOOK) {
                         ItemMeta meta = tempStack.getItemMeta();
                         meta = generateEnchant(meta, tempStack.getAmount());
                         tempStack.setItemMeta(meta);
                         }
                         plugin.formatToLog(inHand, "Won", p.getName(), tempStack.toString(), 1);
                         }
                        
                         //=============TESTING==================*/
                        //=============ITEM GENERATION AND FORMATTING==================
                        //=============FLAG SYSTEM==================
                        if ((this.plugin.playerUpNext.containsKey(p.getName())) && ((this.plugin.playerUpNext.get(p.getName())))) {
                            matCycle[28] = new ItemStack(plugin.flagItem, plugin.flagItemAmount, (short) plugin.flagItemShort);
                            if (plugin.flagItem == Material.ENCHANTED_BOOK) {
                                matCycle[28].addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                                ItemMeta tempMeta = matCycle[28].getItemMeta();
                                tempMeta.setDisplayName(plugin.parseEnum(plugin.flagItemEnchant) + " " + plugin.flagItemEnchantLevel + " x" + matCycle[28].getAmount());
                                matCycle[28].setItemMeta(tempMeta);
                            } else {
                                ItemMeta meta = matCycle[28].getItemMeta();
                                meta.setDisplayName("x" + matCycle[28].getAmount());
                                matCycle[28].setItemMeta(meta);
                            }
                            Boolean put = this.plugin.playerUpNext.put(p.getName(), false);
                        }
                        if (this.plugin.upNext) {
                            matCycle[28] = new ItemStack(plugin.flagItem, plugin.flagItemAmount, (short) plugin.flagItemShort);
                            if (plugin.flagItem == Material.ENCHANTED_BOOK) {
                                matCycle[28].addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                                ItemMeta tempMeta = matCycle[28].getItemMeta();
                                tempMeta.setDisplayName(plugin.parseEnum(plugin.flagItemEnchant) + " " + plugin.flagItemEnchantLevel + " x" + matCycle[28].getAmount());
                                matCycle[28].setItemMeta(tempMeta);
                            } else {
                                ItemMeta meta = matCycle[28].getItemMeta();
                                meta.setDisplayName("x" + matCycle[28].getAmount());
                                matCycle[28].setItemMeta(meta);
                            }
                            this.plugin.upNext = false;
                        }
                        //=============FLAG SYSTEM==================
                        if (plugin.pairs.containsKey(sign.locS() + "announce")) {
                            announce(sign.locS(), "1", "", "", 1);
                        }
                        for (int i = 0; i < 30; i++) {
                            int time = i * 3 + slower[i];

                            item[0] = matCycle[i];
                            item[1] = (i - 1 > -1 ? matCycle[(i - 1)] : new ItemStack(Material.AIR));
                            item[2] = (i - 2 > -1 ? matCycle[(i - 2)] : new ItemStack(Material.AIR));
                            item[3] = (i - 3 > -1 ? matCycle[(i - 3)] : new ItemStack(Material.AIR));
                            item[4] = (i - 4 > -1 ? matCycle[(i - 4)] : new ItemStack(Material.AIR));
                            itemTimer(x[0], item[0], time);
                            itemTimer(x[1], item[1], time);
                            itemTimer(x[2], item[2], time);
                            itemTimer(x[3], item[3], time);
                            itemTimer(x[4], item[4], time);

                            float truePitch = (float) Math.pow(2.0D, (notes[i] - 12.0D) / 12.0D);
                            soundTimer(bl[0].getLocation(), Sound.NOTE_PLING, 0.1F, truePitch, time);
                            if (i == 29) {
                                endMark(sign.locS(), time + 40);
                                if (event.getClickedBlock().getType() == Material.LEVER) {
                                    flipLever(event.getClickedBlock(), time + 40);
                                }
                                if ((matCycle[27].getType() == matCycle[26].getType()) && (matCycle[27].getType() == matCycle[28].getType())) {
                                    if ((matCycle[26].getType() == matCycle[25].getType()) && (matCycle[28].getType() == matCycle[29].getType())) {
                                        matCycle[27].setAmount(matCycle[27].getAmount() * this.plugin.bonus5x);
                                        ItemStack prize = matCycle[27];
                                        playerDeliver(prize, p, time + 20);
                                        plugin.formatToLog(inHand, "Won", p.getName(), prize.toString(), 1);
                                        if (plugin.pairs.containsKey(sign.locS() + "announce")) {
                                            announce(sign.locS(), p.getName(), prize.getAmount() + " "
                                                    + plugin.parseEnum(prize.getType().name()) + ((prize.getAmount() > 1) ? "S" : ""), enchantGlobal, time + 20);
                                        }
                                        float tP = (float) Math.pow(2.0D, 0.25D);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 20);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 21);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 22);
                                    } else {
                                        matCycle[27].setAmount(matCycle[27].getAmount() * this.plugin.bonus3x);
                                        ItemStack prize = matCycle[27];
                                        playerDeliver(prize, p, time + 20);
                                        plugin.formatToLog(inHand, "Won", p.getName(), prize.toString(), 1);
                                        if (plugin.pairs.containsKey(sign.locS() + "announce")) {
                                            announce(sign.locS(), p.getName(), prize.getAmount() + " "
                                                    + plugin.parseEnum(prize.getType().name()) + ((prize.getAmount() > 1) ? "S" : ""), enchantGlobal, time + 20);
                                        }
                                        float tP = (float) Math.pow(2.0D, 0.25D);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 20);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 21);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 22);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 23);
                                        soundTimer(bl[0].getLocation(), Sound.ORB_PICKUP, 0.1F, tP, time + 24);
                                    }
                                } else {
                                    ItemStack prize = matCycle[27];
                                    playerDeliver(prize, p, time + 20);
                                    plugin.formatToLog(inHand, "Won", p.getName(), prize.toString(), 1);
                                    if (plugin.pairs.containsKey(sign.locS() + "announce")) {
                                        announce(sign.locS(), p.getName(), prize.getAmount() + " "
                                                + plugin.parseEnum(prize.getType().name()) + ((prize.getAmount() > 1) ? "S" : ""), enchantGlobal, time + 20);
                                    }
                                }
                            }
                        }
                    } else {
                        event.getPlayer().sendMessage(ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + " Please wait for the current game to finish!");
                        event.setCancelled(true);
                    }
                } else {
                    event.getPlayer().sendMessage(ChatColor.DARK_RED + "Your inventory is full!");
                }
            } else {
                event.setCancelled(true);
            }
        } else if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LEVER) {
            Blockdress sign = new Blockdress(event.getClickedBlock().getLocation());
            if (plugin.pairs != null && plugin.pairs.containsKey(sign.locS())) {
                event.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() != null && event.getRightClicked().getType() == EntityType.ITEM_FRAME
                && event.getPlayer().getItemInHand().getType() == Material.EMERALD
                && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Spincraft Token")) {
            event.setCancelled(true);
            String[] message = new String[]{ChatColor.BLUE + "============" + ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + "=============",
                ChatColor.GOLD + "Sorry, you can't do that =["};
            for (String s : message) {
                event.getPlayer().sendMessage(s);
            }
        }
    }

    /*@EventHandler
     public void onBlockPlaceEvent(BlockPlaceEvent event) {
     if (event.getItemInHand().getItemMeta().hasDisplayName() && event.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Spincraft Token")) {
     event.setCancelled(true);
     }
    
     }*/
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event
    ) {
        if ((event.getInventory().getType() == InventoryType.MERCHANT
                || event.getInventory().getType() == InventoryType.ANVIL
                || event.getInventory().getType() == InventoryType.BEACON
                || event.getInventory().getType() == InventoryType.WORKBENCH) && event.getCurrentItem() != null
                && event.getCurrentItem().getType() == Material.EMERALD) {
            //event.getCurrentItem().getType() == Material.BEDROCK
            if (event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Spincraft Token")) {
                event.setCancelled(true);
            }
        }
        if (event.getCurrentItem() != null
                && (event.getCurrentItem().getType() == Material.EMERALD
                || event.getCurrentItem().getType() == Material.BEDROCK) && event.getInventory().getType() == InventoryType.ANVIL) {
            event.setCancelled(true);
        }
    }

    public int[][] faceWeights(Blockdress block) {
        int[][] weights = new int[5][3];

        BlockFace face = ((Blockdress) this.plugin.pairs.get(block.locS())).face;
        if (face == BlockFace.NORTH) {
            weights = new int[][]{{2, 0, -1}, {1, 0, -1}, {0, 0, -1}, {-1, 0, -1}, {-2, 0, -1}};
        } else if (face == BlockFace.SOUTH) {
            weights = new int[][]{{-2, 0, 1}, {-1, 0, 1}, {0, 0, 1}, {1, 0, 1}, {2, 0, 1}};
        } else if (face == BlockFace.WEST) {
            weights = new int[][]{{-1, 0, -2}, {-1, 0, -1}, {-1, 0, 0}, {-1, 0, 1}, {-1, 0, 2}};
        } else if (face == BlockFace.EAST) {
            weights = new int[][]{{1, 0, 2}, {1, 0, 1}, {1, 0, 0}, {1, 0, -1}, {1, 0, -2}};
        }
        return weights;
    }

    public Integer soundTimer(final Location loc, final Sound sound, final float pitch, final float some, long time) {
        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Location backup = loc;
                loc.getWorld().playSound(loc, sound, pitch, some);
            }
        }, time);

        return timerID;
    }

    public Integer playerDeliver(final ItemStack itemz, final Player p, long time) {
        final ItemStack item = itemz.clone();
        ItemMeta strippedName = item.getItemMeta();
        strippedName.setDisplayName((strippedName.getDisplayName().contains("Enchant Token")) ? ("§4Enchant Token") : (null));
        item.setItemMeta(strippedName);

        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                if (plugin.getServer().getOnlinePlayers().contains(plugin.getServer().getPlayer(p.getName()))
                        && p.getInventory().firstEmpty() != -1) {
                    p.getInventory().addItem(new ItemStack[]{item});
                } else {
                    if (!plugin.recover.containsKey(p.getUniqueId().toString())) {
                        plugin.recover.put(p.getUniqueId().toString(), new ArrayList<Recover>());
                    } else if (plugin.recover.get(p.getUniqueId().toString()) == null) {
                        plugin.recover.put(p.getUniqueId().toString(), new ArrayList<Recover>());
                    }
                    if (plugin.getServer().getOnlinePlayers().contains(plugin.getServer().getPlayer(p.getName()))) {
                        String[] message = new String[]{ChatColor.BLUE + "" + ChatColor.MAGIC + "AAAAAA" + ChatColor.BLUE + "===============" + ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + "===============" + ChatColor.MAGIC + "AAAAAA",
                            ChatColor.RED + "Oops! Your inventory was full!",
                            ChatColor.GOLD + "Make some room in your inventory",
                            ChatColor.GOLD + "Type \"/spin recover\" when you are ready",
                            ChatColor.BLUE + "==================================================="
                        };
                        for (String s : message) {
                            p.sendMessage(s);
                        }
                    }
                    ArrayList<Recover> tempRec = plugin.recover.get(p.getUniqueId().toString());
                    tempRec.add(new Recover(item));
                    plugin.recover.put(p.getUniqueId().toString(), tempRec);
                    plugin.updateRecover(plugin.recover);
                }
                Eventor.this.firework(p.getLocation());
                // p.getLocation().getWorld().playEffect(p.getLocation().getBlock().getRelative(0,2,0).getLocation(), Effect.EXPLOSION_LARGE, 0);//NOTICEME
            }
        }, time);

        return timerID;
    }

    public Integer endMark(final String key, long time) {
        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Eventor.this.plugin.spinMap.put(key, false);
            }
        }, time);

        return timerID;
    }

    public ItemFrame getFrame(Location loc) {
        ItemFrame nbl = null;
        for (Entity e : loc.getChunk().getEntities()) {
            if (e.getLocation().getBlock().getLocation().distance(loc) == 0.0D) {
                if (e.getType() == EntityType.ITEM_FRAME) {
                    nbl = (ItemFrame) e;
                    return (ItemFrame) nbl;
                }
            }
        }
        return null;
    }

    public void itemTimer(final ItemFrame frame, final ItemStack item, int time) {
        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                frame.setItem(item);
            }
        }, time);
    }

    public void firework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        FireworkEffect.Type type = FireworkEffect.Type.CREEPER;

        Color c1 = Color.GREEN;
        Color c2 = Color.BLACK;
        for (int i = 0; i < 3; i++) {
            FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c1).withFade(c2).with(type).trail(true).build();
            fwm.addEffect(effect);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);
        }
    }

    public ItemStack generate() {
        int[] ranges = new int[this.plugin.MaterialChanceTable.length];
        int sum = 0;
        for (int i = 0; i < this.plugin.MaterialChanceTable.length; i++) {
            sum += this.plugin.MaterialChanceTable[i];
            ranges[i] = sum;
        }
        int counter = 0;
        int i = ranges[0];
        int random = (int) Math.round(Math.random() * 1000.0D);
        while (i < random) {
            counter++;
            i = ranges[counter];
        }
        ItemStack prize = new ItemStack(Material.DIRT);
        if (Material.getMaterial(plugin.MaterialTable[counter]) == null) {
            if (plugin.MaterialTable[counter].equals("ENCHANT_TOKEN")) {
                prize = lavaToken(plugin.MaterialAmountTable[counter]);
            }
        } else {
            prize = new ItemStack(Material.getMaterial(plugin.MaterialTable[counter]), plugin.MaterialAmountTable[counter], plugin.MaterialShortTable[counter]);
            //data = plugin.MaterialShortTable[counter];
        }
        return prize;
    }

    /*public ItemMeta generateEnchant(ItemMeta meta) {
     int[] ranges = new int[this.plugin.EnchantChanceTable.length];
     int sum = 0;
     for (int i = 0; i < this.plugin.EnchantChanceTable.length; i++) {
     sum += this.plugin.EnchantChanceTable[i];
     ranges[i] = sum;
     }
     int counter = 0;
     int i = ranges[0];
     int random = (int) Math.round(Math.random() * 1000.0D);
     while (i < random) {
     counter++;
     i = ranges[counter];
     }
     meta.addEnchant(Enchantment.getByName(this.plugin.EnchantTable[counter]), this.plugin.EnchantLevelTable[counter], true);
     return meta;
     }*/
    public ItemMeta generateEnchant(ItemMeta meta, int amount) {
        int[] ranges = new int[this.plugin.EnchantChanceTable.length];
        int sum = 0;
        for (int i = 0; i < this.plugin.EnchantChanceTable.length; i++) {
            sum += this.plugin.EnchantChanceTable[i];
            ranges[i] = sum;
        }
        int counter = 0;
        int i = ranges[0];
        int random = (int) Math.round(Math.random() * 1000.0D);
        while (i < random) {
            counter++;
            i = ranges[counter];
        }
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) meta;
        ArrayList bookLore = new ArrayList<String>();
        bookMeta.setLore(bookLore);
        bookMeta.addStoredEnchant(Enchantment.getByName(plugin.EnchantTable[counter]), plugin.EnchantLevelTable[counter], true);
        bookMeta.setDisplayName(ChatColor.RESET + plugin.parseEnum(plugin.EnchantTable[counter]) + " " + plugin.EnchantLevelTable[counter] + " x" + amount);
        return bookMeta;
    }

    public void announce(String loc, final String player, final String item, final String enchant, long time) {
        final Sign sign = (Sign) (this.plugin.pairs.get(loc + "announce")).getLocation().getBlock().getState();
        final String playercut;
        final String enchantFin;
        final String hasWon;

        if (player.length() > 15) {
            playercut = player.substring(0, 15);
        } else if (player.length() < 2) {
            playercut = "";
        } else {
            playercut = player;
        }

        if (player.equals("1")) {
            hasWon = "";
        } else {
            hasWon = "Has Won:";
        }

        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                sign.setLine(0, ChatColor.BLUE + "" + ChatColor.BOLD + playercut);
                sign.setLine(1, ChatColor.BOLD + hasWon);
                sign.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + item);
                sign.setLine(3, ChatColor.DARK_RED + "" + ChatColor.BOLD + enchant);
                sign.update();
            }
        }, time);

    }

    public void flipLever(final Block leverBlock, long time) {
        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Lever lever = (Lever) leverBlock.getState().getData();
                BlockFace facing = lever.getFacing();
                BlockFace attached = lever.getAttachedFace();
                byte side = 0;
                if (facing == BlockFace.EAST) {
                    side = 1;
                } else if (facing == BlockFace.WEST) {
                    side = 2;
                } else if (facing == BlockFace.SOUTH) {
                    side = 3;
                } else if (facing == BlockFace.NORTH) {
                    side = 4;
                }

                leverBlock.setData(side);
            }
        }, time);
    }

    public void onJoinNotice(final Player player, long time) {
        int timerID = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                String[] message = new String[]{ChatColor.BLUE + "" + ChatColor.MAGIC + "AAAAAA" + ChatColor.BLUE + "===============" + ChatColor.GOLD + "[Spincraft]" + ChatColor.BLUE + "===============" + ChatColor.MAGIC + "AAAAAA",
                    ChatColor.GOLD + "It appears you lost some items!",
                    ChatColor.GOLD + "Make some room in your inventory",
                    ChatColor.GOLD + "Type '/spin recover' when you are ready to retrieve them",
                    ChatColor.BLUE + "==================================================="
                };
                for (String s : message) {
                    player.sendMessage(s);
                }
            }
        }, time);
    }

    public ItemStack lavaToken(int quantity) {
        ItemStack tokens = new ItemStack(Material.BEDROCK, quantity);
        ItemMeta meta = tokens.getItemMeta();
        meta.setDisplayName("Enchant Token");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§bHold What You Want To Enchant");
        lore.add("§bSpend Using /tokens!");
        meta.setLore(lore);
        tokens.setItemMeta(meta);
        return tokens;
    }

}
