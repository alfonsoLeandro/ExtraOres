package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import com.github.alfonsoleandro.extraores.utils.MessageSender;
import com.github.alfonsoleandro.extraores.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class PopulateOresListeners implements Listener {

    private final ExtraOres plugin;
    private final Settings settings;
    private final MessageSender messageSender;
    private final OresManager oresManager;
    private final Random random;

    public PopulateOresListeners(ExtraOres plugin){
        this.plugin = plugin;
        this.oresManager = plugin.getOresManager();
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
        this.random = new Random();
        Bukkit.broadcastMessage("populate listeners registered");
    }

//    @EventHandler
//    public void onChunkLoad(ChunkLoadEvent event){
//        if(!event.isNewChunk()) return;
//        //todo remove debug.
//        Bukkit.broadcastMessage("IS NEW CHUNK");
//        populateChunkMode1(event.getChunk());
//    }
//
//    @EventHandler
//    public void onChunkPopulate(ChunkPopulateEvent event){
//        //todo
//    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event){
        Bukkit.broadcastMessage("populator added to "+event.getWorld().getName());
        event.getWorld().getPopulators().add(plugin.getOrePopulator());
    }

    /**
     * Populates a chunk with ores using mode 1.
     * mode 1: loop through every ore, check probability
     * @param chunk The chunk to populate.
     */
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
            double prob = random.nextDouble()*1000;
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

    //todo: mode2
//    /**
//     * Populates a chunk with ores using mode 2.
//     * mode 2: choose X random ores from the ores list, check probability
//     * @param chunk The chunk to populate.
//     */
//    private void populateChunk(Chunk chunk){
//
//    }



}
