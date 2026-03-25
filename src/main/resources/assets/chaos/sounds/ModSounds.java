package com.sparkshard.chaos.sound;

import com.sparkshard.chaos.ChaosMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    // FBI Sounds
    public static final SoundEvent FBI_KICK = registerSound("fbi.kick");
    public static final SoundEvent FBI_OPEN_UP = registerSound("fbi.open_up");
    
    // Mafia Sounds
    public static final SoundEvent MAFIA_WHISTLE = registerSound("mafia.whistle");
    public static final SoundEvent SWORD_DRAW = registerSound("mafia.sword_draw");

    // Disaster Sounds
    public static final SoundEvent TORNADO_ROAR = registerSound("disaster.tornado_roar");
    public static final SoundEvent GLASS_SHATTER = registerSound("env.glass_shatter");

    private static SoundEvent registerSound(String name) {
        Identifier id = new Identifier(ChaosMod.MODID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        ChaosMod.LOGGER.info("Registering Sounds for " + ChaosMod.MODID);
    }
}
