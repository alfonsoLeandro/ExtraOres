package com.github.alfonsoleandro.extraores.managers;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageSender extends Reloadable {

    private final ExtraOres plugin;
    private final Map<Message, String> messages = new HashMap<>();
    private boolean isDebug;
    private String prefix;

    /**
     * Creates a new instance of the message sender.
     * This class is supposed to have a unique instance at a time.
     * @param plugin This plugin's main class instance.
     */
    public MessageSender(ExtraOres plugin) {
        super(plugin);
        this.plugin = plugin;
        loadMessages();
    }

    /**
     * Sends a string to the given CommandSender.
     * @param sender The intended receiver for the message.
     * @param msg The String to send.
     */
    public void send(CommandSender sender, String msg){
        sender.sendMessage(StringUtils.colorizeString(prefix+" "+msg));
    }

    /**
     * Sends a string to the console.
     * @param msg The string to send.
     */
    public void send(String msg){
        send(Bukkit.getConsoleSender(), msg);
    }

    /**
     * Sends a message to the given CommandSender.
     * @param sender The intended receiver for the message.
     * @param message The message to send.
     * @param replacements The string to replace from the message and its replacements.
     */
    public void send(CommandSender sender, Message message, String... replacements){
        String msg = messages.get(message);
        for(int i = 0; i < replacements.length; i++){
            msg = msg.replace(replacements[i], replacements[++i]);
        }
        send(sender, msg);
    }

    /**
     * Sends a message to every player online and the console.
     * @param excluded A player to exclude from this message.
     * @param message The message to send.
     * @param replacements The strings to replace from the message and its replacements.
     */
    public void broadcast(Player excluded, Message message, String... replacements){
        String msg = messages.get(message);
        for(int i = 0; i < replacements.length; i++){
            msg = msg.replace(replacements[i], replacements[++i]);
        }
        for(Player toSend : Bukkit.getOnlinePlayers()) {
            if(toSend.equals(excluded)) continue;
            send(toSend, msg);
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.colorizeString(msg));
    }

    /**
     * Sends a title and subtitle to a player.
     * @param player The player that will be receiving this title and subtitle.
     * @param title The title to send (set to "" to not send title).
     * @param subtitle The subtitle to send (set to "" to not send subtitle).
     * @param stay The amount of time the title
     * @param replacements The strings to replace from the title and subtitle and its replacements.
     */
    public void title(Player player, Message title, Message subtitle, int stay, String... replacements){
        String ttl = messages.get(title);
        for(int i = 0; i < replacements.length; i++){
            ttl = ttl.replace(replacements[i], replacements[++i]);
        }
        String sbtt = messages.get(subtitle);
        for(int i = 0; i < replacements.length; i++){
            sbtt = sbtt.replace(replacements[i], replacements[++i]);
        }
        player.sendTitle(StringUtils.colorizeString(ttl),
                StringUtils.colorizeString(sbtt),
                4,
                stay,
                4);
    }

    /**
     * Sends a debug message to the console if debug is set to true.
     * @param msg The string to send.
     */
    public void debug(String msg){
        if(isDebug) send(msg);
    }

    /**
     * Loads every message and some sensitive settings, like prefix and debug mode.
     */
    private void loadMessages() {
        this.messages.clear();
        FileConfiguration config = plugin.getConfigYaml().getAccess();

        this.isDebug = config.getBoolean("config.debug");

        this.prefix = config.getString("config.prefix");

        FileConfiguration messages = plugin.getMessagesYaml().getAccess();

        for(Message message : Message.values()){
            this.messages.put(message,
                    messages.getString("messages."+message.toString().toLowerCase(Locale.ENGLISH).replace("_", " ")));
        }
    }


    @Override
    public void reload() {
        this.loadMessages();
    }


    /**
     * Enum that represents every message available.
     * The name of the message will also be the path in the messages file, for example,
     * for the message "NO_PERMISSION", the path will be "messages.no permission".
     */
    public enum Message{
        NO_PERMISSION,
        UNKNOWN_COMMAND,
        RELOADED,
        CANNOT_SEND_CONSOLE,
        ORE_NOT_FOUND,
        INVALID_NUMBER,
        INVALID_PLAYER,
        ORE_RECEIVED,
        ORE_GIVEN,
        FULL_INV,
        ORE_PLACED

    }
}
