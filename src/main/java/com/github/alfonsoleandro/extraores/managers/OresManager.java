package com.github.alfonsoleandro.extraores.managers;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import com.github.alfonsoleandro.extraores.ores.defaultores.ExperienceOre;
import com.github.alfonsoleandro.extraores.ores.defaultores.HealthOre;
import com.github.alfonsoleandro.extraores.utils.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;

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
                oresFile.getInt("ores.experience ore.experience")
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
                oresFile.getDouble("ores.health ore.health")
        ));

    }


    public void unregisterDefaultOres(){
        unregisterOre("experience_ore");
        unregisterOre("heart_ore");

    }

    public void registerOre(ExtraOre ore){
        if(ores.contains(ore)) throw new IllegalStateException("Ore with name "+ore.getOreName()+" already registered!");
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
        return ores.contains(ore);
    }

    public ExtraOre getOreByName(String oreName){
        for (ExtraOre ore : ores){
            if(ore.getOreName().equalsIgnoreCase(oreName)) return ore;
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
