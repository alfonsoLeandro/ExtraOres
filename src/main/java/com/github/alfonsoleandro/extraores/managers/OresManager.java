package com.github.alfonsoleandro.extraores.managers;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import com.github.alfonsoleandro.extraores.ores.defaultores.*;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class OresManager extends Reloadable {

    private final Set<ExtraOre> ores = new HashSet<>();
    private final ExtraOres plugin;
    private final MessageSender messageSender;

    public OresManager(ExtraOres plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        registerDefaultOres();
    }

    private void registerDefaultOres(){
        FileConfiguration oresFile = plugin.getOresYaml().getAccess();
        registerOre(new ExperienceOre(
                oresFile.getString("ores.experience ore.url"),
                oresFile.getStringList("ores.experience ore.enabled worlds"),
                oresFile.getStringList("ores.experience ore.replaces"),
                oresFile.getInt("ores.experience ore.max amount"),
                oresFile.getDouble("ores.experience ore.chunk probability"),
                oresFile.getDouble("ores.experience ore.probability"),
                oresFile.getInt("ores.experience ore.min y"),
                oresFile.getInt("ores.experience ore.max y"),
                oresFile.getInt("ores.experience ore.experience"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.experience ore.item.name"),
                        oresFile.getStringList("ores.experience ore.item.lore")
                )
        ));
        registerOre(new HealthOre(
                oresFile.getString("ores.health ore.url"),
                oresFile.getStringList("ores.health ore.enabled worlds"),
                oresFile.getStringList("ores.health ore.replaces"),
                oresFile.getInt("ores.health ore.max amount"),
                oresFile.getDouble("ores.health ore.chunk probability"),
                oresFile.getDouble("ores.health ore.probability"),
                oresFile.getInt("ores.health ore.min y"),
                oresFile.getInt("ores.health ore.max y"),
                oresFile.getDouble("ores.health ore.health"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.health ore.item.name"),
                        oresFile.getStringList("ores.health ore.item.lore")
                )
        ));
        registerOre(new CompactDiamondOre(
                oresFile.getString("ores.compact diamond ore.url"),
                oresFile.getStringList("ores.compact diamond ore.enabled worlds"),
                oresFile.getStringList("ores.compact diamond ore.replaces"),
                oresFile.getInt("ores.compact diamond ore.max amount"),
                oresFile.getDouble("ores.compact diamond ore.chunk probability"),
                oresFile.getDouble("ores.compact diamond ore.probability"),
                oresFile.getInt("ores.compact diamond ore.min y"),
                oresFile.getInt("ores.compact diamond ore.max y"),
                oresFile.getInt("ores.compact diamond ore.amount"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.compact diamond ore.item.name"),
                        oresFile.getStringList("ores.compact diamond ore.item.lore")
                )
        ));
        registerOre(new FakeCompactDiamondOre(
                oresFile.getString("ores.fake compact diamond ore.url"),
                oresFile.getStringList("ores.fake compact diamond ore.enabled worlds"),
                oresFile.getStringList("ores.fake compact diamond ore.replaces"),
                oresFile.getInt("ores.fake compact diamond ore.max amount"),
                oresFile.getDouble("ores.fake compact diamond ore.chunk probability"),
                oresFile.getDouble("ores.fake compact diamond ore.probability"),
                oresFile.getInt("ores.fake compact diamond ore.min y"),
                oresFile.getInt("ores.fake compact diamond ore.max y"),
                oresFile.getInt("ores.fake compact diamond ore.power"),
                plugin,
                oresFile.getString("ores.fake compact diamond ore.message"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.fake compact diamond ore.item.name"),
                        oresFile.getStringList("ores.fake compact diamond ore.item.lore")
                )
        ));
        registerOre(new RandomMobOre(
                oresFile.getString("ores.random mob ore.url"),
                oresFile.getStringList("ores.random mob ore.enabled worlds"),
                oresFile.getStringList("ores.random mob ore.replaces"),
                oresFile.getInt("ores.random mob ore.max amount"),
                oresFile.getDouble("ores.random mob ore.chunk probability"),
                oresFile.getDouble("ores.random mob ore.probability"),
                oresFile.getInt("ores.random mob ore.min y"),
                oresFile.getInt("ores.random mob ore.max y"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.random mob ore.item.name"),
                        oresFile.getStringList("ores.random mob ore.item.lore")
                ),
                oresFile.getStringList("ores.random mob ore.mobs")
        ));
        registerOre(new ZombieOre(
                oresFile.getString("ores.zombie ore.url"),
                oresFile.getStringList("ores.zombie ore.enabled worlds"),
                oresFile.getStringList("ores.zombie ore.replaces"),
                oresFile.getInt("ores.zombie ore.max amount"),
                oresFile.getDouble("ores.zombie ore.chunk probability"),
                oresFile.getDouble("ores.zombie ore.probability"),
                oresFile.getInt("ores.zombie ore.min y"),
                oresFile.getInt("ores.zombie ore.max y"),
                MPItemStacks.newItemStack(
                        Material.PLAYER_HEAD,
                        1,
                        oresFile.getString("ores.zombie ore.item.name"),
                        oresFile.getStringList("ores.zombie ore.item.lore")
                )
        ));

    }


    public void unregisterDefaultOres(){
        unregisterOre("experience_ore");
        unregisterOre("heart_ore");
        unregisterOre("compact_diamond_ore");
        unregisterOre("fake_compact_diamond_ore");
        unregisterOre("random_mob_ore");
        unregisterOre("zombie_ore");
    }

    public void registerOre(ExtraOre ore){
        if(isOreRegistered(ore)) throw new IllegalStateException("Ore with name "+ore.getOreName()+" already registered!");
        ores.add(ore);
        messageSender.debug("Registered ore with name " + ore.getOreName());
    }

    public void unregisterOre(String oreName){
        if(!isOreRegistered(oreName)) throw new IllegalStateException("Ore with name "+oreName+" has not been registered yet!");
        ores.remove(getOreByName(oreName));
        messageSender.debug("Unregistered ore with name " + oreName);
    }

    public boolean isOreRegistered(String oreName){
        for (ExtraOre ore : ores){
            if(ore.getOreName().equalsIgnoreCase(oreName)) return true;
        }
        return false;
    }

    public boolean isOreRegistered(ExtraOre ore){
        for (ExtraOre eOre : ores){
            if(eOre.equals(ore) || eOre.getOreName().equalsIgnoreCase(ore.getOreName())) return true;
        }
        return false;
    }

    public ExtraOre getOreByName(String oreName){
        for (ExtraOre ore : ores){
            if(ore.getOreName().equalsIgnoreCase(oreName)) return ore;
        }
        return null;
    }


    public ExtraOre getOreByItem(ItemStack item){
        for (ExtraOre ore : ores){
            if(ore.getOreItem().isSimilar(item)) return ore;
        }
        return null;

    }




    public Set<ExtraOre> getRegisteredOres(){
        return this.ores;
    }



    @Override
    public void reload(){
        unregisterDefaultOres();
        registerDefaultOres();
    }
}
