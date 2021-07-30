package com.github.alfonsoleandro.extraores.commands;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import com.github.alfonsoleandro.extraores.utils.MessageSender;
import com.github.alfonsoleandro.extraores.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public final class MainCommand implements CommandExecutor {

    private final ExtraOres plugin;
    private final MessageSender messageSender;
    private final OresManager oresManager;
    private final Settings settings;
    private final Random random = new Random();

    /**
     * MainCommand class constructor.
     * @param plugin The main class instance.
     */
    public MainCommand(ExtraOres plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.oresManager = plugin.getOresManager();
        this.settings = plugin.getSettings();
    }
    

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.messageSender.send(sender, "&6List of commands");
            this.messageSender.send(sender, "&f/"+label+" help");
            this.messageSender.send(sender, "&f/"+label+" version");
            this.messageSender.send(sender, "&f/"+label+" reload");



        }else if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission(/*TODO plugin name*/"PLUGIN.reload")) {
                this.messageSender.send(sender, MessageSender.Message.NO_PERMISSION);
                return true;
            }
            plugin.reload();
            this.messageSender.send(sender, MessageSender.Message.RELOADED);


        }else if(args[0].equalsIgnoreCase("version")) {
            if(!sender.hasPermission(/*TODO plugin name*/"PLUGIN.version")) {
                this.messageSender.send(sender, MessageSender.Message.NO_PERMISSION);
                return true;
            }
            this.messageSender.send(sender, "&fVersion: &e" + plugin.getVersion() + "&f. &aUp to date!");

        }else if(args[0].equalsIgnoreCase("populate")){
            Player player = (Player) sender;
            populateChunkMode1(player.getLocation().getChunk());
            Bukkit.broadcastMessage("Populando chunk");


            //unknown command
        }else {
            this.messageSender.send(sender, MessageSender.Message.UNKNOWN_COMMAND,
                    "%command%", label);
        }



        return true;
    }

    private void populateChunkMode1(Chunk chunk){
        int max = settings.getMaxOresPerChunk();
        int totalPlaced = 0;
        Bukkit.broadcastMessage("Size: "+oresManager.getRegisteredOres().size());
        ores: for(ExtraOre ore: oresManager.getRegisteredOres()){
            Bukkit.broadcastMessage("Info: "+totalPlaced+"/"+max);
            //If the ore is disabled in this world.
            if(!ore.getEnabledInWorlds().contains(chunk.getWorld().getName())) return;
            Bukkit.broadcastMessage("World is in enabled list");
            //Chunk filled with the max amount of ores.
            if(totalPlaced >= max){
//                messageSender.debug("Chunk at "+chunk.getX()+", "+chunk.getZ()+" filled with ExtraOres");
                Bukkit.broadcastMessage("Chunk at "+chunk.getX()+", "+chunk.getZ()+" filled with ExtraOres");
                return;
            }
            //If probability failed
            double prob = random.nextDouble()*100;
            if(ore.getChunkProbability() < prob) {
                Bukkit.broadcastMessage("Porbability failed "+ore.getChunkProbability()+" < "+prob);
                continue;
            }
            int placed = 0;
            Bukkit.broadcastMessage("max per chunk: "+ore.getMaxOresPerChunk());


            Bukkit.broadcastMessage("Ys: "+ore.getMinY()+" - "+ore.getMaxY());

            //For every block in the chunk, between the ore's Y values.
            for(int y = ore.getMinY(); y <= ore.getMaxY(); y++){
                for(int x = 0; x <= 15; x++){
                    for(int z = 0; z <= 15; z++){
                        if(placed >= ore.getMaxOresPerChunk()) continue ores;
                        Bukkit.broadcastMessage("Not placed max ores");
                        Block block = chunk.getBlock(x, y, z);
                        //If this block type is not to be replaced by this ore.
                        if(!ore.getReplaces().contains(block.getType().toString())) continue;
                        Bukkit.broadcastMessage("Block is in to replace list");
                        //If probability failed.
                        double prob2 = random.nextDouble()*100;
                        if(ore.getProbability() < prob2){
                            Bukkit.broadcastMessage("Probability failed: "+ore.getProbability()+" < "+prob2);
                            continue;
                        }

                        //Finally, Place ore
                        block.setType(Material.PLAYER_WALL_HEAD);
                        block.setMetadata("ExtraOre", new FixedMetadataValue(plugin, ore));
                        placed++;
                        totalPlaced++;
                        Bukkit.broadcastMessage("Ore placed: "+ore.getOreName()+
                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+")");
//                        messageSender.debug("Ore placed: "+ore.getOreName()+
//                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+")");
                    }
                }
            }

        }
    }

}
