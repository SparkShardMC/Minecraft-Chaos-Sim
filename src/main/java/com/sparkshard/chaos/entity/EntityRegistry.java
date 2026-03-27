package com.sparkshard.chaos.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.sparkshard.chaos.ChaosMod;
public class EntityRegistry {

    // 1. Define the FBI Agent
    public static final EntityType<EntityFBI> FBI_AGENT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ChaosMod.MODID, "fbi_agent"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntityFBI::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f)).build()
    );

    // 2. Define the Mafia Member
    public static final EntityType<EntityMafia> MAFIA_MEMBER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ChaosMod.MODID, "mafia_member"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntityMafia::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f)).build()
    );

    public static void register() {
        // Register Attributes (Health, Speed, Attack)
        FabricDefaultAttributeRegistry.register(FBI_AGENT, EntityFBI.createMobAttributes());
        FabricDefaultAttributeRegistry.register(MAFIA_MEMBER, EntityMafia.createMobAttributes());
    }
}
