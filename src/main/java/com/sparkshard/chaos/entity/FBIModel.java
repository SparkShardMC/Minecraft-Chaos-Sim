package com.sparkshard.chaos.entity;

import com.sparkshard.chaos.ChaosMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class FBIModel extends GeoModel<EntityFBI> {
    @Override
    public Identifier getModelResource(EntityFBI animatable) {
        return new Identifier(ChaosMod.MODID, "geo/fbi_agent.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityFBI animatable) {
        return new Identifier(ChaosMod.MODID, "textures/entity/fbi_agent.png");
    }

    @Override
    public Identifier getAnimationResource(EntityFBI animatable) {
        return new Identifier(ChaosMod.MODID, "animations/fbi_agent.animation.json");
    }
}
