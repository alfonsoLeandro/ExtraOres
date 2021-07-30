package com.github.alfonsoleandro.extraores.utils;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings extends Reloadable{

    private final ExtraOres plugin;
    //Fields
    private int maxOresPerChunk;

    public Settings(ExtraOres plugin){
        super(plugin);
        this.plugin = plugin;
        loadFields();
    }

    private void loadFields(){
        FileConfiguration config = plugin.getConfigYaml().getAccess();

        this.maxOresPerChunk = config.getInt("config.max ores per chunk");
    }

    //<editor-fold desc="Getters">

    public int getMaxOresPerChunk() {
        return maxOresPerChunk;
    }

    //</editor-fold>



    @Override
    public void reload() {
        loadFields();
    }
}
