package com.sparkshard.chaos.naming;

import java.util.Random;
import java.util.List;

public class NamingEngine {
    private static final Random RND = new Random();

    // The logic to pull from your updated Civilian AI lists
    public static String getCivilianName() {
        // Keep the 1% Bob legend, he's the OG
        if (RND.nextInt(100) == 0) return "Bob"; 

        String prefix = rand(ConfigLoader.CIVILIAN_PREFIXES);
        String name = rand(ConfigLoader.CIVILIAN_NAMES);
        String suffix = rand(ConfigLoader.CIVILIAN_SUFFIXES);

        // This creates combinations like "Poopy_YourMom_The_Great_421"
        return prefix + "_" + name + "_" + suffix + "_" + (RND.nextInt(900) + 100);
    }

    // FBI & Mafia logic remains sleek and tactical
    public static String getFBIName() {
        return rand(ConfigLoader.FBI_RANK) + " " + rand(ConfigLoader.FBI_CODENAME) + " " + rand(ConfigLoader.FBI_ID);
    }

    public static String getMafiaName() {
        return rand(ConfigLoader.MAFIA_TITLE) + " '" + rand(ConfigLoader.MAFIA_NICKNAME) + "' " + rand(ConfigLoader.MAFIA_SURNAME);
    }

    // Block names now use your new adjectives and nouns
    public static String getBlockName(String owner, String blockType) {
        String adj = rand(ConfigLoader.BLOCK_ADJ);
        String noun = rand(ConfigLoader.BLOCK_NOUN);
        return owner + "'s " + adj + " " + noun + " (" + blockType + ")";
    }

    private static String rand(List<String> list) {
        return list.get(RND.nextInt(list.size()));
    }
}
