/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.hoque.matt.Spincraft;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/**
 *
 * @author Owner
 */
public class Blockdress implements Serializable {

    String world;
    double x;
    double y;
    double z;
    BlockFace face;

    Spincraft plugin;

    public Blockdress(Spincraft instance) {
        plugin = instance;
    }
    
    public Blockdress(Location loc) {
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.face=null;

    }
    
    
    public Blockdress(Location loc,BlockFace face) {
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.face=face;
    }
    
    public Location getLocation() {
        Location l = new Location(Bukkit.getServer().getWorld(world), x, y, z);
        return l;
    }
    
    public boolean compareLoc(Blockdress bd){
        boolean flag=false;
        if(this.x==bd.x&&this.y==bd.y&&this.z==bd.z&&this.world.equals(bd.world)){
            flag=true;
        }
        return flag;
    }
    
    
    public String locS(){
        return world+x+y+z;
    }
}
