package com.github.alfonsoleandro.extraores.utils;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Settings extends Reloadable{

    private final ExtraOres plugin;
    //Fields
    private boolean populateChunkMode1;

    private int maxOresPerChunk;
    private int selectedOresAmount;

    public Settings(ExtraOres plugin){
        super(plugin);
        this.plugin = plugin;
        loadFields();
    }

    private void loadFields(){
        FileConfiguration config = plugin.getConfigYaml().getAccess();
        MessageSender messageSender = plugin.getMessageSender();

        this.populateChunkMode1 = !config.contains("chunk populate mode") ||
                Objects.requireNonNull(config.getString("chunk populate mode")).equalsIgnoreCase("1");

        if(populateChunkMode1){
            messageSender.debug("Using chunk populator mode 1");
        }else{
            messageSender.debug("Using chunk populator mode 2");
        }


        this.maxOresPerChunk = config.getInt("config.max ores per chunk");
        this.selectedOresAmount = config.getInt("config.mode 2 ores");
    }

    //<editor-fold desc="Getters">
    public boolean isPopulateChunkMode1() {
        return populateChunkMode1;
    }


    public int getMaxOresPerChunk() {
        return maxOresPerChunk;
    }

    public int getSelectedOresAmount() {
        return selectedOresAmount;
    }

    //</editor-fold>



    @Override
    public void reload() {
        loadFields();
    }
}
