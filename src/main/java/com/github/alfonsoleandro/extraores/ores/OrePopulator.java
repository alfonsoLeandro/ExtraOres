package com.github.alfonsoleandro.extraores.ores;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.utils.Settings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class OrePopulator extends BlockPopulator {

    private final ExtraOres plugin;
    private final OresManager oresManager;
    private final Settings settings;

    public OrePopulator(ExtraOres plugin){
        this.plugin = plugin;
        this.oresManager = plugin.getOresManager();
        this.settings = plugin.getSettings();
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        if(settings.isPopulateChunkMode1()) {
            populateChunkMode1(source, random);
        }else{
            populateChunkMode2(source, random);
        }
    }

    /**
     * Populates a chunk with ores using mode 1.
     * mode 1: loop through every ore.
     * @param chunk The chunk to populate.
     */
    private void populateChunkMode1(Chunk chunk, Random random){
        int max = settings.getMaxOresPerChunk();
        int totalPlaced = 0;
        Bukkit.broadcastMessage("Size: "+oresManager.getRegisteredOres().size());
        ores: for(ExtraOre ore : oresManager.getRegisteredOres()){
            //If the ore is disabled in this world.
            if(!ore.getEnabledInWorlds().contains(chunk.getWorld().getName())) return;
            //Chunk filled with the max amount of ores.
            if(totalPlaced >= max) return;

            //If probability failed
            double prob = random.nextInt(100);
            if(prob > ore.getChunkProbability()) continue;
            int placed = 0;

            //For every block in the chunk, between the ore's Y values.
            for(int y = ore.getMinY(); y <= ore.getMaxY(); y++){
                if(random.nextBoolean()) continue;
                int minX = random.nextInt(7);
                int maxX = random.nextInt(16-7)+7;
                for(int x = minX; x <= maxX; x++){
                    int minZ = random.nextInt(7);
                    int maxZ = random.nextInt(16-7)+7;
                    for(int z = minZ; z <= maxZ; z++){
                        //If the maximum amount of this ore per chunk has already been reached.
                        if(placed >= ore.getMaxOresPerChunk()) continue ores;
                        Block block = chunk.getBlock(x, y, z);
                        //If this block type is not to be replaced by this ore.
                        if(!ore.getReplaces().contains(block.getType().toString())) continue;
                        //If probability failed.
                        double prob2 = random.nextInt(100);
                        if(prob2 > ore.getProbability()) continue;


                        //Finally, Place ore
                        setSkullSkinAndData(block, ore);
                        placed++;
                        totalPlaced++;
                        Bukkit.broadcastMessage("Ore placed: "+ore.getOreName()+
                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+") "+ore.getProbability()+" < "+prob2);
//                        messageSender.debug("Ore placed: "+ore.getOreName()+
//                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+")");
                    }
                }
            }

        }
    }

    /**
     * Populates a chunk with ores using mode 2.
     * mode 2: Select a few (configurable amount) ores from the ores' manager.
     * @param chunk The chunk to populate.
     */
    private void populateChunkMode2(Chunk chunk, Random random){
        int max = settings.getMaxOresPerChunk();
        int totalPlaced = 0;
        List<ExtraOre> registered = new ArrayList<>(oresManager.getRegisteredOres());
        Set<ExtraOre> selected = new HashSet<>();
        int selectedOres = Math.min(settings.getSelectedOresAmount(), registered.size());

        //Select selectedOres amount of ores randomly from the registered ores list.
        for (int i = 0; i < selectedOres; i++) {
            ExtraOre toAdd = registered.get(random.nextInt(registered.size()));
            registered.remove(toAdd);
            selected.add(toAdd);
        }

        Bukkit.broadcastMessage("Size: "+selected.size());

        ores: for(ExtraOre ore : selected){
            //If the ore is disabled in this world.
            if(!ore.getEnabledInWorlds().contains(chunk.getWorld().getName())) return;
            //Chunk filled with the max amount of ores.
            if(totalPlaced >= max) return;

            //If probability failed
            double prob = random.nextInt(100);
            if(prob > ore.getChunkProbability()) continue;
            int placed = 0;

            //For every block in the chunk, between the ore's Y values.
            for(int y = ore.getMinY(); y <= ore.getMaxY(); y++){
                if(random.nextBoolean()) continue;
                int minX = random.nextInt(7);
                int maxX = random.nextInt(16-7)+7;
                for(int x = minX; x <= maxX; x++){
                    int minZ = random.nextInt(7);
                    int maxZ = random.nextInt(16-7)+7;
                    for(int z = minZ; z <= maxZ; z++){
                        //If the maximum amount of this ore per chunk has already been reached.
                        if(placed >= ore.getMaxOresPerChunk()) continue ores;
                        Block block = chunk.getBlock(x, y, z);
                        //If this block type is not to be replaced by this ore.
                        if(!ore.getReplaces().contains(block.getType().toString())) continue;
                        //If probability failed.
                        double prob2 = random.nextInt(100);
                        if(prob2 > ore.getProbability()) continue;


                        //Finally, Place ore
                        setSkullSkinAndData(block, ore);
                        placed++;
                        totalPlaced++;
                        Bukkit.broadcastMessage("Ore placed: "+ore.getOreName()+
                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+") "+ore.getProbability()+" < "+prob2);
//                        messageSender.debug("Ore placed: "+ore.getOreName()+
//                                " ("+block.getX()+", "+block.getY()+", "+block.getZ()+")");
                    }
                }
            }

        }
    }

    private void setSkullSkinAndData(Block block, ExtraOre ore){
        new BukkitRunnable() {
            public void run() {
                String skullUrl = "http://textures.minecraft.net/texture/" + ore.getSkinURL();
                block.setType(Material.PLAYER_WALL_HEAD);
                Skull skull = (Skull) block.getState();

                PersistentDataContainer data = skull.getPersistentDataContainer();
                data.set(new

                                NamespacedKey(plugin, "ExtraOre"), PersistentDataType.STRING,
                        ore.getOreName());

                GameProfile profile = new GameProfile(UUID.fromString("ee9a50bc-4a58-432e-b1a1-52c8fb41f5fb"), null);
                byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skullUrl).getBytes());
                profile.getProperties().

                        put("textures", new Property("textures", new String(encodedData)));
                Field profileField;
                try {
                    profileField = skull.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skull, profile);
                } catch (NoSuchFieldException | IllegalArgumentException |
                        IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                skull.update();
            }
        }.runTask(plugin);
    }
}
