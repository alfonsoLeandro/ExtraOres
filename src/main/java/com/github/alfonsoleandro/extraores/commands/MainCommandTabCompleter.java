package com.github.alfonsoleandro.extraores.commands;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommandTabCompleter implements TabCompleter {

    private final OresManager oresManager;

    public MainCommandTabCompleter(ExtraOres plugin){
        this.oresManager = plugin.getOresManager();
    }


    /**
     * Checks if a string is equal (ignoring upper cases) to another string completed or uncompleted.
     * @param input The first string.
     * @param string The string to "cut into pieces" in order to compare every possibility to the first string.
     * @return True if the string is at least partially equal to the input.
     */
    public boolean equalsToStrings(String input, String string){
        return input.length() <= string.length() &&
                string.substring(0, input.length()).equalsIgnoreCase(input);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("")) {
                completions.add("help");
                completions.add("version");
                completions.add("reload");
                completions.add("give");

            } else if(equalsToStrings(args[0], "help")) {
                completions.add("help");

            } else if(equalsToStrings(args[0], "version")) {
                completions.add("version");

            } else if(equalsToStrings(args[0], "reload")) {
                completions.add("reload");

            }else if(equalsToStrings(args[0], "give")) {
                completions.add("give");
            }

        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("give")){
                for(ExtraOre ore : oresManager.getRegisteredOres()){
                    if(equalsToStrings(args[1], ore.getOreName())){
                        completions.add(ore.getOreName());
                    }
                }
            }

        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("give")){
                for(String name : Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList())){
                    if(equalsToStrings(args[2], name)){
                        completions.add(name);
                    }
                }
            }

        }else if(args.length == 4){
            if(args[0].equalsIgnoreCase("give")){
                Collections.addAll(completions,
                        "1", "2", "3", "4", "5", "10", "16", "32", "64");
            }

        }
        return completions;
    }



}
