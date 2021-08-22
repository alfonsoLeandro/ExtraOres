package com.github.alfonsoleandro.extraores;

import com.github.alfonsoleandro.extraores.commands.MainCommand;
import com.github.alfonsoleandro.extraores.commands.MainCommandTabCompleter;
import com.github.alfonsoleandro.extraores.listeners.BlockBreakListener;
import com.github.alfonsoleandro.extraores.listeners.BlockPlaceListener;
import com.github.alfonsoleandro.extraores.listeners.OreBreakListener;
import com.github.alfonsoleandro.extraores.ores.OrePopulator;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.managers.MessageSender;
import com.github.alfonsoleandro.extraores.utils.Settings;
import com.github.alfonsoleandro.mputils.files.YamlFile;
import com.github.alfonsoleandro.mputils.reloadable.ReloaderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;

public final class ExtraOres extends ReloaderPlugin {
    /**
     * This plugin's unique instance.
     */
    private static ExtraOres instance;

    private final String version = getDescription().getVersion();
    private final char color = 'e';
    private Settings settings;
    private MessageSender messageSender;
    private OresManager oresManager;
    private OrePopulator orePopulator;
    private YamlFile configYaml;
    private YamlFile messagesYaml;
    private YamlFile oresYaml;


    /**
     * Plugin enable logic.
     */
    @Override
    public void onEnable() {
        instance = this;
        reloadFiles();
        this.settings = new Settings(this);
        this.messageSender = new MessageSender(this);
        this.oresManager = new OresManager(this);
        this.orePopulator = new OrePopulator(this);
        this.messageSender.send("&aEnabled&f. Version: &e" + version);
        this.messageSender.send("&fThank you for using my plugin! &" + color + getDescription().getName() + "&f By " + getDescription().getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + getDescription().getWebsite());
        registerEvents();
        registerCommands();
        for(World world : Bukkit.getWorlds()){
            world.getPopulators().add(this.orePopulator);
        }
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
        this.messageSender.send("&cDisabled&f. Version: &e" + version);
        this.messageSender.send("&fThank you for using my plugin! &" + color + getDescription().getName() + "&f By " + getDescription().getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + getDescription().getWebsite());
        for(World world : Bukkit.getWorlds()){
            world.getPopulators().remove(this.orePopulator);
        }
    }


    /**
     * Gets the plugins current version.
     * @return The version string.
     */
    public String getVersion() {
        return this.version;
    }


    /**
     * Registers and reloads plugin files.
     * Please use {@link #reload()} for reloading the plugin.
     */
    public void reloadFiles() {
        configYaml = new YamlFile(this, "config.yml");
        messagesYaml = new YamlFile(this, "messages.yml");
        oresYaml = new YamlFile(this, "default ores.yml");
    }


    /**
     * Reloads the entire plugin.
     */
    @Override
    public void reload(){
        reloadFiles();
        super.reload();
    }

    /**
     * Registers the event listeners.
     */
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new OreBreakListener(this), this);
        pm.registerEvents(new BlockPlaceListener(this), this);
//        pm.registerEvents(new PopulateOresListeners(this), this);
    }


    /**
     * Registers commands and command classes.
     */
    private void registerCommands() {
        PluginCommand mainCommand = getCommand("extraOres");

        if(mainCommand == null){
            this.messageSender.send("&cCommands were not registered properly. Please check your plugin.yml is intact");
            this.messageSender.send("&cDisabling MPExtraOres");
            this.setEnabled(false);
            return;
        }

        mainCommand.setExecutor(new MainCommand(this));
        mainCommand.setTabCompleter(new MainCommandTabCompleter(this));
    }


    /**
     * Gets the settings' manager.
     * @return Gets the Settings unique instance.
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * Gets the messages' manager.
     * @return Gets the MessageSender unique instance.
     */
    public MessageSender getMessageSender() {
        return this.messageSender;
    }

    /**
     * Gets the ores' manager.
     * @return Gets the OresManager unique instance.
     */
    public OresManager getOresManager() {
        return this.oresManager;
    }

    /**
     * Gets the ores' populator.
     * @return Gets the OrePopulator unique instance.
     */
    public OrePopulator getOrePopulator() {
        return this.orePopulator;
    }

    /**
     * Get the config YamlFile.
     * @return The YamlFile containing the config file.
     */
    public YamlFile getConfigYaml(){
        return this.configYaml;
    }

    /**
     * Get the messages YamlFile.
     * @return The YamlFile containing the messages file.
     */
    public YamlFile getMessagesYaml(){
        return this.messagesYaml;
    }

    /**
     * Get the default ores YamlFile.
     * @return The YamlFile containing the ores file.
     */
    public YamlFile getOresYaml(){
        return this.oresYaml;
    }



    public static ExtraOres getInstance(){
        return instance;
    }

}
