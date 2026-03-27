package com.sparkshard.chaos.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import com.sparkshard.chaos.ChaosMod;

public class FBIModel extends GeoModel<EntityFBI> {
    @Override
    public Identifier getModelResource(EntityFBI animatable) {
        // Points to: assets/chaos/geo/fbi.geo.json
        return Identifier.of("chaos", "geo/fbi.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityFBI animatable) {
        // Points to: assets/chaos/textures/entity/fbi.png
        return Identifier.of("chaos", "textures/entity/fbi.png");
    }

    @Override
    public Identifier getAnimationResource(EntityFBI animatable) {
        // Points to: assets/chaos/animations/fbi.animation.json
        return Identifier.of("chaos", "animations/fbi.animation.json");
    }
}
