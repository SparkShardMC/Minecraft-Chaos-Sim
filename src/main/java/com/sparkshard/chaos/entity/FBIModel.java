public class FBIModel extends GeoModel<EntityFBI> {
    @Override
    public Identifier getModelResource(EntityFBI animatable) {
        // Points to assets/chaos/geo/fbi_agent.geo.json
        return new Identifier("chaos", "geo/fbi_agent.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityFBI animatable) {
        // Points to assets/chaos/textures/entity/fbi_agent.png
        return new Identifier("chaos", "textures/entity/fbi_agent.png");
    }

    @Override
    public Identifier getAnimationResource(EntityFBI animatable) {
        // Points to assets/chaos/animations/fbi_agent.animation.json
        return new Identifier("chaos", "animations/fbi_agent.animation.json");
    }
}
