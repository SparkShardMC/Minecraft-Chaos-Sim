package com.sparkshard.chaos.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FBIRenderer extends GeoEntityRenderer<EntityFBI> {
    public FBIRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FBIModel());
        this.shadowRadius = 0.5f; // Adds a shadow under the FBI agent
    }
}
