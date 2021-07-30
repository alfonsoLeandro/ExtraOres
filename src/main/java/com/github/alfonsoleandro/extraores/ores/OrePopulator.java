package com.github.alfonsoleandro.extraores.ores;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.utils.Settings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

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
        populateChunkMode1(source, random);
    }

    /**
     * Populates a chunk with ores using mode 1.
     * mode 1: loop through every ore, check probability
     * @param chunk The chunk to populate.
     */
    private void populateChunkMode1(Chunk chunk, Random random){
        int max = settings.getMaxOresPerChunk();
        int totalPlaced = 0;
        Bukkit.broadcastMessage("Size: "+oresManager.getRegisteredOres().size());
        ores: for(ExtraOre ore: oresManager.getRegisteredOres()){
            //If the ore is disabled in this world.
            if(!ore.getEnabledInWorlds().contains(chunk.getWorld().getName())) return;
            //Chunk filled with the max amount of ores.
            if(totalPlaced >= max) return;

            //If probability failed
            double prob = random.nextDouble()*1000;
            if(ore.getChunkProbability() < prob) continue;
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
                        double prob2 = random.nextDouble()*1000;
                        if(ore.getProbability() < prob2) continue;


                        //Finally, Place ore
                        setSkullSkin(block, ore);
                        block.setMetadata("ExtraOre", new FixedMetadataValue(plugin, ore));
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

    private void setSkullSkin(Block block, ExtraOre ore){
        String skullUrl = "http://textures.minecraft.net/texture/"+ ore.getSkinURL();
        block.setType(Material.PLAYER_WALL_HEAD);
        Skull skull = (Skull) block.getState();

        GameProfile profile = new GameProfile(UUID.fromString("8561b610-ad5c-390d-ac31-1a1d8ca69fd7"), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skullUrl).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skull, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        skull.update();
    }
}
