package com.github.alfonsoleandro.extraores.ores;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ExtraOre {

    protected String oreName;
    protected String skinURL;
    protected Set<String> enabledInWorlds;
    protected Set<String> replaces;
    protected double chunkProbability;
    protected double probability;
    protected int maxOresPerChunk;
    protected int maxY;
    protected int minY;
    protected ItemStack oreItem;


    public ExtraOre(String oreName, String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces, int maxOresPerChunk,
                    double chunkProbability, double probability, int minY, int maxY, ItemStack item) {
        this.oreName = oreName;
        this.skinURL = skinURL;
        this.enabledInWorlds = new HashSet<>();
        this.enabledInWorlds.addAll(enabledInWorlds);
        this.replaces = new HashSet<>();
        this.replaces.addAll(replaces);
        this.maxOresPerChunk = maxOresPerChunk;
        this.chunkProbability = chunkProbability;
        this.probability = probability;
        this.maxY = maxY;
        this.minY = minY;
        this.oreItem = item;
        setSkullSkin();
    }

    /**
     * Sets the skin of the skull based on the skin url.
     */
    private void setSkullSkin(){
        if(!oreItem.getType().equals(Material.PLAYER_HEAD)) return;
        String skullUrl = "http://textures.minecraft.net/texture/"+ this.getSkinURL();
        SkullMeta meta = (SkullMeta) oreItem.getItemMeta();

        GameProfile profile = new GameProfile(UUID.fromString("ee9a50bc-4a58-432e-b1a1-52c8fb41f5fb"), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skullUrl).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        assert meta != null;
        try{
            Method setProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            Bukkit.broadcastMessage("1");
            setProfileMethod.setAccessible(true);
            Bukkit.broadcastMessage("2");
            setProfileMethod.invoke(meta, profile);
            Bukkit.broadcastMessage("3");
            Bukkit.broadcastMessage("Set profile!");
        }catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
            try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                Bukkit.broadcastMessage("4");
                profileField.setAccessible(true);
                Bukkit.broadcastMessage("5");
                profileField.set(meta, profile);
                Bukkit.broadcastMessage("6");
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        Bukkit.broadcastMessage("7");
        oreItem.setItemMeta(meta);
    }

    /**
     * Method called when this ore is broken by a player.
     * Unless the {@link com.github.alfonsoleandro.extraores.events.OreBreakEvent} is cancelled.
     * @param player The player breaking this ore.
     * @param location  The location of the ore (block).
     */
    public abstract void onBreak(Player player, Location location);

    public String getOreName() {
        return oreName;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public Set<String> getEnabledInWorlds() {
        return enabledInWorlds;
    }

    public Set<String> getReplaces() {
        return replaces;
    }

    public ItemStack getOreItem(){
        return oreItem;
    }

    public int getMaxOresPerChunk() {
        return maxOresPerChunk;
    }

    public double getChunkProbability() {
        return chunkProbability;
    }

    public double getProbability() {
        return probability;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }
}
